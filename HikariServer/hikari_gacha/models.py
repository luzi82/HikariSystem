from django.db import models
from hikari_resource.models import HsUserResource
import random
from hikari_card.models import HsUserCard
from ajax.exceptions import AJAXError

# Create your models here.

class HsGacha(models.Model):

    HIKARI_STATIC_NAME = "gacha"

    key = models.CharField(max_length=64, db_index=True)
    
    def check_resource(self,user,time):
        gacha_cost_db_query = HsGachaCost.objects.filter(gacha_key=self.key)
        for gacha_cost_db in gacha_cost_db_query:
            HsUserResource.objects.get(
                user=user,
                resource_key=gacha_cost_db.resource_key
            ).check(gacha_cost_db.value,time)

    def process(self,user,time):
        gacha_cost_db_query = HsGachaCost.objects.filter(gacha_key=self.key)
        gacha_result_db_query = HsGachaResult.objects.filter(gacha_key=self.key)
        for gacha_cost_db in gacha_cost_db_query:
            HsUserResource.objects.get(
                user=user,
                resource_key=gacha_cost_db.resource_key
            ).consume(gacha_cost_db.value,time)
        chance_sum = 0
        result_list = []
        for gacha_result_db in gacha_result_db_query:
            chance_sum += gacha_result_db.chance
            result_list.append({
                'chance_sum': chance_sum,
                'gacha_result_db': gacha_result_db,
            })
        rand_out = random.randint(0,chance_sum-1)
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


class HsGachaCost(models.Model):

    HIKARI_STATIC_NAME = "gacha_cost"

    gacha_key = models.CharField(max_length=64, db_index=True)
    resource_key = models.CharField(max_length=64)
    value = models.BigIntegerField()


class HsGachaResult(models.Model):
    
    gacha_key = models.CharField(max_length=64, db_index=True)
    card_type_key = models.CharField(max_length=64)
    chance = models.IntegerField()
