from django.db import models
import hs_i18n.models as i18n_models
import hs_level.models as i18n_level
from django.contrib.auth import models as auth_models

class AmResourceType(models.Model):

    name = models.ForeignKey(i18n_models.AmText)
    count_max_leveltype = models.ForeignKey(i18n_level.AmLevelType,related_name="amresourcetype_count_max_leveltype",null=True)
    count_increase_leveltype = models.ForeignKey(i18n_level.AmLevelType,related_name="amresourcetype_count_increase_leveltype",null=True)

class AmResourceCountMax(models.Model):
    
    level = models.IntegerField(db_index=True)
    count_max = models.IntegerField()

class AmResourceCountIncrease(models.Model):
    
    level = models.IntegerField(db_index=True)
    count_increase = models.IntegerField()

class AmUserResourceCount(models.Model):

    user = models.ForeignKey(auth_models.User,db_index=True)
    type = models.ForeignKey(AmResourceType,db_index=True)
    count = models.IntegerField(db_index=True)
    update = models.BigIntegerField()
