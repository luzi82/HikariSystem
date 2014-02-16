from django.db import models

class HsQuestEntry(models.Model):

    key = models.CharField(max_length=64,db_index=True)

    class Meta:
        app_label = 'hikari'
