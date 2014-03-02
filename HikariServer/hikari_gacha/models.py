from django.db import models

# Create your models here.

class HsGacha(models.Model):

    HIKARI_STATIC_NAME = "gacha"

    key = models.CharField(max_length=64, db_index=True)


class HsGachaCost(models.Model):

    HIKARI_STATIC_NAME = "gacha_cost"

    gacha_key = models.CharField(max_length=64, db_index=True)
    resource_key = models.CharField(max_length=64)
    value = models.BigIntegerField()


class HsGachaResult(models.Model):
    
    gacha_key = models.CharField(max_length=64, db_index=True)
    card_key = models.CharField(max_length=64)
    chance = models.IntegerField()
