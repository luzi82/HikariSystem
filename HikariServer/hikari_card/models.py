from django.db import models
from django.contrib.auth.models import User

# Create your models here.

class HsCardType(models.Model):
    
    HIKARI_STATIC_NAME = "card_type"

    key = models.CharField(max_length=64, db_index=True)


class HsUserCard(models.Model):

    user = models.ForeignKey(User,db_index=True)
    card_type_key = models.CharField(max_length=64)


class HsInitUserCard(models.Model):

    card_type_key = models.CharField(max_length=64)
