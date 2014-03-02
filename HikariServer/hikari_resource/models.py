from django.db import models
from django.contrib.auth.models import User
from ajax.exceptions import AJAXError
from django.core.exceptions import ObjectDoesNotExist

class HsResource(models.Model):
    
    HIKARI_STATIC_NAME = "resource"
    HIKARI_STATIC_MEMBER_LIST = ["key","type"]
    
    TYPE_COUNT = 1
    TYPE_TIME = 2
    
    key = models.CharField(max_length=64, db_index=True)
    type = models.IntegerField()
    max = models.BigIntegerField()
    init_count = models.IntegerField()


class HsUserResourceManager(models.Manager):
    
    def get(self,user,resource_key,*args,**kwargs):
        try:
            return models.Manager.get(
                self,
                user=user,
                resource_key=resource_key,
                *args,**kwargs
            )
        except ObjectDoesNotExist:
            pass
        resource_db = HsResource.objects.get(key=resource_key)
        user_resource_db = HsUserResource.objects.create(
            user=user,
            resource_key=resource_db.key,
            count=resource_db.init_count,
            time=0
        )
        user_resource_db.save()
        return user_resource_db


class HsUserResource(models.Model):
    
    user = models.ForeignKey(User,db_index=True)
    resource_key = models.CharField(max_length=64)
    count = models.IntegerField()
    time = models.BigIntegerField()
    
    objects = HsUserResourceManager()

    class Meta:
        index_together = [
            ["user", "resource_key"],
        ]

    def type(self):
        resource = HsResource.objects.get(key=self.resource_key)
        return resource.type
    
    def max(self):
        resource = HsResource.objects.get(key=self.resource_key)
        return resource.max

    def value(self,now):
        selftype = self.type()
        selfmax = self.max()
        v = 0
        if selftype == HsResource.TYPE_COUNT:
            v = self.count
        elif selftype == HsResource.TYPE_TIME:
            v = selfmax + now - self.time
        v = min(v,selfmax)
        return v
    
    def check(self,value,time):
        self_value = self.value(time)
        if self_value < value:
            raise AJAXError(400,'bN3XaWtF: self_value={self_value}, value={value}'.format(self_value=self_value,value=value))
    
    def consume(self,value,time):
        self_value = self.value(time)
        if self_value < value:
            # should be 500, dev should check before consume
            raise AJAXError(500,'b7lWLaop: self_value={self_value}, value={value}'.format(self_value=self_value,value=value))
        selftype = self.type()
        if selftype == HsResource.TYPE_COUNT:
            self.count = self_value-value
        elif selftype == HsResource.TYPE_TIME:
            if self.time < time:
                self.time = time
            self.time += value
        self.save()

    def add(self,value,time):
        self_value = self.value(time)
        self_type = self.type()
        if self_type == HsResource.TYPE_COUNT:
            self_max = self.max()
            self.count = self_value+value
            if self.count > self_max:
                self.count = self_max
        elif self_type == HsResource.TYPE_TIME:
            self.time -= value
            if self.time < time:
                self.time = time
        self.save()
        
    def change(self,value,time):
        if(value>0):
            self.add(value,time)
        else:
            self.consume(-value,time)


class HsResourceConvertChange(models.Model):

    HIKARI_STATIC_NAME = "resource_convert_change"

    key = models.CharField(max_length=64, db_index=True)
    resource_key = models.CharField(max_length=64)
    change = models.BigIntegerField()

    def check_resource(self,user,count,now):
        if self.change > 0 :
            return
        user_resource = HsUserResource.objects.get(user=user,resource_key=self.resource_key)
        user_resource.check(count*(-self.change),now)

    def process(self,user,count,now):
        user_resource = HsUserResource.objects.get(user=user,resource_key=self.resource_key)
        user_resource.change(count*self.change,now)
