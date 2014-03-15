from django.contrib.auth.models import User
from django.db import models
import json

from hikari_value.models import HsValueChangeModel,\
    HsValueChangeGroupModel


class HsQuestCostValueChange(HsValueChangeModel):
    
    HIKARI_STATIC_NAME = "quest_cost_value_change"


class HsQuestRewardValueChange(HsValueChangeModel):
    
    HIKARI_STATIC_NAME = "quest_reward_value_change"


class HsQuestEntry(HsValueChangeGroupModel):
    
    HIKARI_STATIC_NAME = "quest_entry"
    
    key = models.CharField(max_length=64,db_index=True)
    
    change_model = HsQuestCostValueChange
    reason_key = "quest_cost"
    
    def reason_msg(self):
        return json.dumps({'quest_entry_key':self.key})

    def reward_value(self,user,time):
        self.process(
            user, time,
            change_model=HsQuestRewardValueChange,
            reason_key="quest_reward"
        )


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
