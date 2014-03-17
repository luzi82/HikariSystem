from django.db import models
from django.contrib.auth.models import User
from ajax.exceptions import AJAXError
from django.core.exceptions import ObjectDoesNotExist
import json
from hikari.models import HsItemPack

class HsValueChangeModel(models.Model):

    parent_key = models.CharField(max_length=64, db_index=True)
    value_key = models.CharField(max_length=64)
    change = models.BigIntegerField()
    
    class Meta:
        abstract = True

    def check_value(self,user,now,count):
        if self.change > 0 :
            return
        user_value = HsUserValue.objects.get(user=user,value_key=self.value_key)
        user_value.check(count*(-self.change),now)

    def process(self,user,now,count,reason_key,reason_msg):
        user_value = HsUserValue.objects.get(user=user,value_key=self.value_key)
        user_value.change(count*self.change,now,reason_key,reason_msg)


class HsValueChangeGroupModel(models.Model):
    
    class Meta:
        abstract = True

    def check_value(self,user,time,count=1,change_model=None,parent_key=None):
        if change_model == None:
            change_model = self.__class__.change_model
        if parent_key == None:
            parent_key = self.key
        change_db_query = change_model.objects.filter(parent_key=parent_key)
        for change_db in change_db_query:
            change_db.check_value(user,time,count)

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


class HsValue(models.Model):
    
    HIKARI_STATIC_NAME = "value"
    HIKARI_STATIC_MEMBER_LIST = ["key","type"]
    
    TYPE_COUNT = 1
    TYPE_TIME = 2
    TYPE_LEVEL = 3
    
    key = models.CharField(max_length=64, db_index=True)
    type = models.IntegerField()
    max = models.BigIntegerField()
    init_count = models.IntegerField()
    ref_value_key = models.CharField(max_length=64, null=True)


class HsUserValueManager(models.Manager):
    
    def get(self,user,value_key,*args,**kwargs):
        try:
            return models.Manager.get(
                self,
                user=user,
                value_key=value_key,
                *args,**kwargs
            )
        except ObjectDoesNotExist:
            pass
        value_db = HsValue.objects.get(key=value_key)
        user_value_db = HsUserValue.objects.create(
            user=user,
            value_key=value_db.key,
            count=value_db.init_count,
            time=0
        )
        user_value_db.save()
        return user_value_db


class HsUserValue(models.Model):
    
    user = models.ForeignKey(User,db_index=True)
    value_key = models.CharField(max_length=64)
    count = models.IntegerField()
    time = models.BigIntegerField()
    
    objects = HsUserValueManager()

    class Meta:
        index_together = [
            ["user", "value_key"],
        ]

    def type(self):
        value = HsValue.objects.get(key=self.value_key)
        return value.type
    
    def max(self):
        value = HsValue.objects.get(key=self.value_key)
        return value.max

    def value(self,now):
        selftype = self.type()
        selfmax = self.max()
        v = 0
        if selftype == HsValue.TYPE_COUNT:
            v = self.count
        elif selftype == HsValue.TYPE_TIME:
            v = selfmax + now - self.time
        elif selftype == HsValue.TYPE_LEVEL:
            ref_value_key = HsValue.objects.get(key=self.value_key).ref_value_key
            v0 = HsUserValue.objects.get(self.user, ref_value_key).value(now)
            v = HsValueLevelStair.objects.filter(value_key=self.value_key,from_value_min__lte=v0).count()
        return v
    
    def check(self,value,time):
        self_value = self.value(time)
        if self_value < value:
            raise AJAXError(400,'bN3XaWtF: self_value={self_value}, value={value}'.format(self_value=self_value,value=value))
    
    def change(self,value,time,reason_key,reason_msg):
        if(value>0):
            self_value = self.value(time)
            self_type = self.type()
            if self_type == HsValue.TYPE_COUNT:
                self_max = self.max()
                self.count = self_value+value
                if self.count > self_max:
                    self.count = self_max
            elif self_type == HsValue.TYPE_TIME:
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
            if selftype == HsValue.TYPE_COUNT:
                self.count = self_value+value
            elif selftype == HsValue.TYPE_TIME:
                if self.time < time:
                    self.time = time
                self.time -= value
            self.save()
        if HsValueChangeHistoryEnable.objects.filter(value_key=self.value_key).exists():
            HsValueChangeHistory.objects.create(
                user=self.user,
                time=time,
                value_key=self.value_key,
                value=value,
                change_reason_key=reason_key,
                change_reason_msg=reason_msg
            ).save()


class HsValueConvertChange(HsValueChangeModel):

    HIKARI_STATIC_NAME = "value_convert_change"


class HsValueConvert(HsValueChangeGroupModel):
    
    HIKARI_STATIC_NAME = "value_convert"
    
    key = models.CharField(max_length=64, db_index=True)
    
    change_model = HsValueConvertChange
    reason_key = "convert"
    
    def reason_msg(self):
        return json.dumps({'value_convert_key':self.key})


class HsValueConvertHistory(models.Model):

    user = user = models.ForeignKey(User,db_index=True)
    time = models.BigIntegerField(db_index=True)
    value_convert_key = models.CharField(max_length=64, db_index=True)
    count = models.IntegerField()

    class Meta:
        index_together = [
            ["user", "time"],
        ]


class HsValueChangeHistory(models.Model):

    user = models.ForeignKey(User,db_index=True)
    time = models.BigIntegerField(db_index=True)
    value_key = models.CharField(max_length=64, db_index=True)
    value = models.IntegerField()
    change_reason_key = models.CharField(max_length=64, db_index=True)
    change_reason_msg = models.TextField()

    class Meta:
        index_together = [
            ["user", "time"],
            ["user", "value_key", "time"],
            ["user", "value_key", "change_reason_key", "time"],
        ]


class HsValueChangeHistoryEnable(models.Model):

    value_key = models.CharField(max_length=64, db_index=True)


class HsValueItem(models.Model):
    
    item_pack = models.ForeignKey(HsItemPack,db_index=True)
    value_key = models.CharField(max_length=64, db_index=True)
    value = models.IntegerField()
    change_reason_key = models.CharField(max_length=64, db_index=True)
    change_reason_msg = models.TextField()


class HsValueLevelStair(models.Model):

    value_key = models.CharField(max_length=64, db_index=True)
    from_value_min = models.IntegerField()

    class Meta:
        index_together = [
            ["value_key", "from_value_min"]
        ]
