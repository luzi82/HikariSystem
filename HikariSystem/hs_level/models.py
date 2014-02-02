from django.db import models
import hs_i18n.models as i18n_models
from django.contrib.auth import models as auth_models

class HsLevelType(models.Model):
    
    name = models.ForeignKey(i18n_models.HsText)

class HsLevelExp(models.Model):

    type = models.ForeignKey(HsLevelType,db_index=True)
    level = models.IntegerField(db_index=True)
    exp_min = models.IntegerField(db_index=True)

class HsUserLevel(models.Model):
    
    user = models.ForeignKey(auth_models.User,db_index=True)
    type = models.ForeignKey(HsLevelType,db_index=True)
    exp = models.IntegerField(db_index=True)
