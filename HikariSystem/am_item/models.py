from django.db import models
import am_i18n.models as i18n_models
from django.contrib.auth import models as auth_models

# Create your models here.

class AmItemTypeGroup(models.Model):
    
    name = models.ForeignKey(i18n_models.AmText)

class AmItemType(models.Model):
    
    itemtypegroup = models.ForeignKey(AmItemTypeGroup,db_index=True)
    name = models.ForeignKey(i18n_models.AmText)

class AmItemValueType(models.Model):
    
    name = models.ForeignKey(i18n_models.AmText)
    value_max = models.IntegerField()

class AmItemValue(models.Model):
    
    itemvaluetype = models.ForeignKey(AmItemValueType,db_index=True)
    itemtype = models.ForeignKey(AmItemType,db_index=True)
    value = models.IntegerField()

class AmUserItem(models.Model):

    user = user = models.ForeignKey(auth_models.User,db_index=True)
    itemtype = models.ForeignKey(AmItemType,db_index=True)

class AmUserItemValue(models.Model):
    
    useritem = models.ForeignKey(AmUserItem,db_index=True)
    itemvaluetype = models.ForeignKey(AmItemValueType,db_index=True)
    value = models.IntegerField()
