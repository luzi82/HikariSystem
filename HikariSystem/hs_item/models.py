from django.db import models
import hs_i18n.models as i18n_models
from django.contrib.auth import models as auth_models

# Create your models here.

class HsItemTypeGroup(models.Model):
    
    name = models.ForeignKey(i18n_models.HsText)

class HsItemType(models.Model):
    
    itemtypegroup = models.ForeignKey(HsItemTypeGroup,db_index=True)
    name = models.ForeignKey(i18n_models.HsText)

class HsItemValueType(models.Model):
    
    name = models.ForeignKey(i18n_models.HsText)
    value_max = models.IntegerField()

class HsItemValue(models.Model):
    
    itemvaluetype = models.ForeignKey(HsItemValueType,db_index=True)
    itemtype = models.ForeignKey(HsItemType,db_index=True)
    value = models.IntegerField()

class HsUserItem(models.Model):

    user = user = models.ForeignKey(auth_models.User,db_index=True)
    itemtype = models.ForeignKey(HsItemType,db_index=True)

class HsUserItemValue(models.Model):
    
    useritem = models.ForeignKey(HsUserItem,db_index=True)
    itemvaluetype = models.ForeignKey(HsItemValueType,db_index=True)
    value = models.IntegerField()
