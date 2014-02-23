from django.db import models
from django.contrib.auth.models import User

class HsQuestEntry(models.Model):

    key = models.CharField(max_length=64,db_index=True)

    class Meta:
        app_label = 'hikari'


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
