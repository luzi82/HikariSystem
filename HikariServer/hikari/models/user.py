from django.db import models as djmodels
from django.contrib.auth.models import User

DEVICE_MODEL_LENGTH = 1024

class HsUser(djmodels.Model):
    
    user = djmodels.ForeignKey(User,db_index=True)
    device_model = djmodels.TextField(max_length=DEVICE_MODEL_LENGTH)
    create_at = djmodels.BigIntegerField()

    class Meta:
        app_label = 'hikari'
