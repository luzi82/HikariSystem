from django.db import models
from django.contrib.auth.models import User
from ajax.exceptions import AJAXError
from django.core.exceptions import ObjectDoesNotExist
import json

class HsResourceChangeModel(models.Model):

    parent_key = models.CharField(max_length=64, db_index=True)
    resource_key = models.CharField(max_length=64)
    change = models.BigIntegerField()
    
    class Meta:
        abstract = True

    def check_resource(self,user,now,count):
        if self.change > 0 :
            return
        user_resource = HsUserResource.objects.get(user=user,resource_key=self.resource_key)
        user_resource.check(count*(-self.change),now)

    def process(self,user,now,count,reason_key,reason_msg):
        user_resource = HsUserResource.objects.get(user=user,resource_key=self.resource_key)
        user_resource.change(count*self.change,now,reason_key,reason_msg)


class HsResourceChangeGroupModel(models.Model):
    
    class Meta:
        abstract = True

    def check_resource(self,user,time,count=1,change_model=None,parent_key=None):
        if change_model == None:
            change_model = self.__class__.change_model
        if parent_key == None:
            parent_key = self.key
        change_db_query = change_model.objects.filter(parent_key=parent_key)
        for change_db in change_db_query:
            change_db.check_resource(user,time,count)

    def process(self,user,time,count=1,change_model=None,parent_key=None,reason_key=None,reason_msg=None):
        if change_model == None:
            change_model = self.__class__.change_model
        if parent_key == None:
            parent_key = self.key
        if reason_key == None:
            reason_key = self.__class__.reason_key
        if reason_msg == None:
            reason_msg = self.reason_msg()
        change_db_query = change_model.objects.filter(parent_key=parent_key)
        for change_db in change_db_query:
            change_db.process(user,time,count,reason_key,reason_msg)


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
    
    def change(self,value,time,reason_key,reason_msg):
        if(value>0):
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
        else:
            self_value = self.value(time)
            if self_value < -value:
                # should be 500, dev should check before consume
                raise AJAXError(500,'b7lWLaop: self_value={self_value}, value={value}'.format(self_value=self_value,value=value))
            selftype = self.type()
            if selftype == HsResource.TYPE_COUNT:
                self.count = self_value+value
            elif selftype == HsResource.TYPE_TIME:
                if self.time < time:
                    self.time = time
                self.time -= value
            self.save()
        if HsResourceChangeHistoryEnable.objects.filter(resource_key=self.resource_key).exists():
            HsResourceChangeHistory.objects.create(
                user=self.user,
                time=time,
                resource_key=self.resource_key,
                count=value,
                change_reason_key=reason_key,
                msg=reason_msg
            ).save()


class HsResourceConvertChange(HsResourceChangeModel):

    HIKARI_STATIC_NAME = "resource_convert_change"


class HsResourceConvert(HsResourceChangeGroupModel):
    
    HIKARI_STATIC_NAME = "resource_convert"
    
    key = models.CharField(max_length=64, db_index=True)
    
    change_model = HsResourceConvertChange
    reason_key = "convert"
    
    def reason_msg(self):
        return json.dumps({'resource_convert_key':self.key})


class HsResourceConvertHistory(models.Model):

    user = user = models.ForeignKey(User,db_index=True)
    time = models.BigIntegerField(db_index=True)
    resource_convert_key = models.CharField(max_length=64, db_index=True)
    count = models.IntegerField()

    class Meta:
        index_together = [
            ["user", "time"],
        ]


class HsResourceChangeHistory(models.Model):

    user = user = models.ForeignKey(User,db_index=True)
    time = models.BigIntegerField(db_index=True)
    resource_key = models.CharField(max_length=64, db_index=True)
    count = models.IntegerField()
    change_reason_key = models.CharField(max_length=64, db_index=True)
    msg = models.TextField()

    class Meta:
        index_together = [
            ["user", "time"],
            ["user", "resource_key", "time"],
            ["user", "resource_key", "change_reason_key", "time"],
        ]


class HsResourceChangeHistoryEnable(models.Model):

    resource_key = models.CharField(max_length=64, db_index=True)
