from django.db import models
from django.contrib.auth.models import User

DEVICE_MODEL_LENGTH = 1024

class HsUser(models.Model):
    
    user = models.ForeignKey(User,db_index=True)
    device_model = models.TextField(max_length=DEVICE_MODEL_LENGTH)
    create_at = models.BigIntegerField()
