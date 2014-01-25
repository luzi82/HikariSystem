from django.db import models

LANG_NAME_LENGTH = 16

# Create your models here.

class AmLang(models.Model):
    
    name = models.CharField(max_length=LANG_NAME_LENGTH,db_index=True)

class AmTextKey(models.Model):
    
    info = models.TextField()

class AmTextI18n(models.Model):
    
    lang = models.ForeignKey(AmLang,db_index=True)
    text_key = models.ForeignKey(AmTextKey,db_index=True)
    text = models.TextField()
