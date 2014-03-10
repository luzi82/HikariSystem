from django.db import models
from django.contrib.auth.models import User

# Create your models here.

class HsMail(models.Model):

    time = models.BigIntegerField(db_index=True)
    from_user = models.ForeignKey(User,db_index=True,related_name='hsmail_from_user')
    to_user = models.ForeignKey(User,db_index=True,related_name='hsmail_to_user')
    title = models.TextField()
    message = models.TextField()
    read = models.BooleanField()

    class Meta:
        index_together = [
            ['to_user','time'],
            ['to_user','read','time']
        ]
