from django.db import models
from django.contrib.auth.models import User

class HsResource(models.Model):
    
    TYPE_COUNT = 1
    TYPE_TIME = 2
    
    key = models.CharField(max_length=64, db_index=True)
    type = models.IntegerField()
    max = models.BigIntegerField()
    init_count = models.IntegerField()

    class Meta:
        app_label = 'hikari'

class HsUserResource(models.Model):
    
    user = models.ForeignKey(User,db_index=True)
    resource_key = models.CharField(max_length=64)
    count = models.IntegerField()
    time = models.BigIntegerField()

    class Meta:
        app_label = 'hikari'
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
        if selftype == HsResource.TYPE_TIME:
            v = selfmax + now - self.time
        v = min(v,selfmax)
        return v
