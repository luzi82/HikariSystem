import random
import string
from django.contrib.auth.models import User, UserManager

def create_random_user():

    while(True):
        username = create_random_string(8)
        if not User.objects.filter(username=username).exists() :
            break
    
    password = create_random_string(8)
    
    user = User.objects.create_user(username=username,password=password)
    user.save()
    
    return {
        "username": username,
        "password": password,
        "user": user,
    }


def create_random_string(length):
    return ''.join(random.choice(string.ascii_uppercase) for _ in range(length))
