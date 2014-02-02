from django.db import models

LANG_NAME_LENGTH = 16
KEY_NAME_LENGTH = 64

# Create your models here.

class AmLang(models.Model):
    
    name = models.CharField(max_length=LANG_NAME_LENGTH,db_index=True)

class AmText(models.Model):
    
    key = models.CharField(max_length=KEY_NAME_LENGTH,db_index=True)
    info = models.TextField()

class AmTextI18n(models.Model):
    
    lang = models.ForeignKey(AmLang,db_index=True)
    text = models.ForeignKey(AmText,db_index=True)
    i18n = models.TextField()