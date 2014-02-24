from django.db import models
from django.contrib.auth.models import User
from hikari.models.resource import HsUserResource
from ajax.exceptions import AJAXError

class HsQuestEntry(models.Model):

    key = models.CharField(max_length=64,db_index=True)

    class Meta:
        app_label = 'hikari'
    
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

    quest_entry_key = models.CharField(max_length=64,db_index=True)
    resource_key = models.CharField(max_length=64)
    count = models.IntegerField()

    class Meta:
        app_label = 'hikari'


class HsQuestInstance(models.Model):
    
    user = models.ForeignKey(User,db_index=True)
    entry_key = models.CharField(max_length=64)
    state = models.IntegerField()
    create_at = models.BigIntegerField()
    complete_at = models.BigIntegerField(null=True,default=None)

    class Meta:
        app_label = 'hikari'

    STATE_STARTED = 0
    STATE_SUCCESS = 1
    STATE_FAIL = 2
    STATE_CANCEL_BY_NEW = 3
