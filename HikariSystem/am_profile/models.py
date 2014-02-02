from django.db import models
from django.contrib.auth import models as auth_models

PROFILE_NAME_LENGTH = 64

class AmUserProfile(models.Model):

    user = models.ForeignKey(auth_models.User,db_index=True)
    name = models.CharField(max_length=PROFILE_NAME_LENGTH,db_index=True)
