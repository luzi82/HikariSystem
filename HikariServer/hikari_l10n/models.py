from django.db import models

class HsL10nLang(models.Model):
    
    HIKARI_STATIC_NAME = "l10n_lang"
    
    key = models.CharField(max_length=64, db_index=True)
    is_default = models.BooleanField()


class HsL10nText(models.Model):
    
    HIKARI_STATIC_NAME = "l10n_text"
    HIKARI_STATIC_PARAM_DICT = {"l10n_lang_key":HsL10nLang}
    HIKARI_STATIC_MEMBER_LIST = ["text_key","text"]
    
    l10n_lang_key = models.CharField(max_length=64, db_index=True)
    text_key = models.CharField(max_length=64, db_index=True)
    text = models.TextField()
