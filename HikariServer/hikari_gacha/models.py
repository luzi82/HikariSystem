from django.db import models
from hikari_resource.models import HsUserResource, HsResourceChangeGroupModel, \
    HsResourceChangeModel
import random
from hikari_card.models import HsUserCard
from ajax.exceptions import AJAXError
import json

# Create your models here.

class HsGachaCostResourceChange(HsResourceChangeModel):

    HIKARI_STATIC_NAME = "gacha_cost_resource_change"

class HsGacha(HsResourceChangeGroupModel):

    HIKARI_STATIC_NAME = "gacha"

    key = models.CharField(max_length=64, db_index=True)
    
    change_model = HsGachaCostResourceChange
    reason_key = "gacha"
    
    def process(self, user, time):
        HsResourceChangeGroupModel.process(self, user, time)
        gacha_result_db_query = HsGachaResult.objects.filter(gacha_key=self.key)
        chance_sum = 0
        result_list = []
        for gacha_result_db in gacha_result_db_query:
            chance_sum += gacha_result_db.chance
            result_list.append({
                'chance_sum': chance_sum,
                'gacha_result_db': gacha_result_db,
            })
        rand_out = random.randint(0, chance_sum - 1)
        for result in result_list:
            if rand_out >= result['chance_sum']:
                continue
            gacha_result_db = result['gacha_result_db']
            user_card_db = HsUserCard.objects.create(
                user=user,
                card_type_key=gacha_result_db.card_type_key,
            )
            user_card_db.save()
            return [user_card_db.id]
   
        raise AJAXError(500, "4v9DNeCI unable to create card")

    def reason_msg(self):
        return json.dumps({'gacha_key':self.key})

class HsGachaResult(models.Model):
    
    gacha_key = models.CharField(max_length=64, db_index=True)
    card_type_key = models.CharField(max_length=64)
    chance = models.IntegerField()
