from django.db import models
import hs_i18n.models as i18n_models
import hs_level.models as i18n_level
from django.contrib.auth import models as auth_models

class HsResourceType(models.Model):

    name = models.ForeignKey(i18n_models.HsText)
    count_max_leveltype = models.ForeignKey(i18n_level.HsLevelType,related_name="hsresourcetype_count_max_leveltype",null=True)
    count_increase_leveltype = models.ForeignKey(i18n_level.HsLevelType,related_name="hsresourcetype_count_increase_leveltype",null=True)

class HsResourceCountMax(models.Model):
    
    level = models.IntegerField(db_index=True)
    count_max = models.IntegerField()

class HsResourceCountIncrease(models.Model):
    
    level = models.IntegerField(db_index=True)
    count_increase = models.IntegerField()

class HsUserResourceCount(models.Model):

    user = models.ForeignKey(auth_models.User,db_index=True)
    type = models.ForeignKey(HsResourceType,db_index=True)
    count = models.IntegerField(db_index=True)
    update = models.BigIntegerField()
