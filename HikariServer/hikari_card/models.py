from django.db import models
from django.contrib.auth.models import User
from django.core.exceptions import ObjectDoesNotExist

# Create your models here.

class HsCardCategory(models.Model):
    
    HIKARI_STATIC_NAME = "card_category"
    
    key = models.CharField(max_length=64, db_index=True)

class HsCardType(models.Model):
    
    HIKARI_STATIC_NAME = "card_type"

    key = models.CharField(max_length=64, db_index=True)
    card_category_key = models.CharField(max_length=64, db_index=True)


class HsCardValueType(models.Model):
    
    HIKARI_STATIC_NAME = "card_value_type"
    
    key = models.CharField(max_length=64, db_index=True)
    card_category_key = models.CharField(max_length=64, db_index=True)


class HsCardValueManager(models.Manager):
    
    def get(self,card_type_key,card_value_type_key,*args,**kwargs):
        try:
            return models.Manager.get(
                self,
                card_type_key=card_type_key,
                card_value_type_key=card_value_type_key,
                *args,**kwargs
            )
        except ObjectDoesNotExist:
            pass
        card_value_db = HsCardValue.objects.create(
            card_type_key=card_type_key,
            card_value_type_key=card_value_type_key,
            value=0
        )
        card_value_db.save()
        return card_value_db


class HsCardValue(models.Model):
    
    card_type_key = models.CharField(max_length=64, db_index=True)
    card_value_type_key = models.CharField(max_length=64, db_index=True)
    value = models.IntegerField()

    HIKARI_STATIC_NAME = "card_value"
    objects = HsCardValueManager()


class HsUserCard(models.Model):

    user = models.ForeignKey(User,db_index=True)
    card_type_key = models.CharField(max_length=64)
    
    def category(self):
        return HsCardType.objects.get(key=self.card_type_key).card_category_key
    
    def value_dict(self):
        category = self.category()
        ret = {}
        card_value_type_db_set = HsCardValueType.objects.filter(card_category_key=category)
        for card_value_type_db in card_value_type_db_set:
            v = HsCardValue.objects.get(
                card_type_key=self.card_type_key,
                card_value_type_key=card_value_type_db.key,
            ).value
            ret[card_value_type_db.key] = v
        return ret


class HsInitUserCard(models.Model):

    card_type_key = models.CharField(max_length=64)
