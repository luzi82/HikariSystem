from django.contrib.auth.models import User
from django.db import models
from hikari_resource.models import HsUserResource


class HsQuestEntry(models.Model):
    
    HIKARI_STATIC_NAME = "quest_entry"
    
    key = models.CharField(max_length=64,db_index=True)

    def check_resource(self,user,time):
        quest_cost_db_query = HsQuestCost.objects.filter(quest_entry_key=self.key)
        for quest_cost_db in quest_cost_db_query:
            HsUserResource.objects.get(
                user=user,
                resource_key=quest_cost_db.resource_key
            ).check(quest_cost_db.count,time)

    def reduce_resource(self,user,time):
        quest_cost_db_query = HsQuestCost.objects.filter(quest_entry_key=self.key)
        for quest_cost_db in quest_cost_db_query:
            HsUserResource.objects.get(
                user=user,
                resource_key=quest_cost_db.resource_key
            ).consume(quest_cost_db.count,time)


class HsQuestCost(models.Model):
    
    HIKARI_STATIC_NAME = "quest_cost"

    quest_entry_key = models.CharField(max_length=64,db_index=True)
    resource_key = models.CharField(max_length=64)
    count = models.IntegerField()


class HsQuestInstance(models.Model):
    
    user = models.ForeignKey(User,db_index=True)
    entry_key = models.CharField(max_length=64)
    state = models.IntegerField()
    create_at = models.BigIntegerField()
    complete_at = models.BigIntegerField(null=True,default=None)

    STATE_STARTED = 0
    STATE_SUCCESS = 1
    STATE_FAIL = 2
    STATE_CANCEL_BY_NEW = 3
