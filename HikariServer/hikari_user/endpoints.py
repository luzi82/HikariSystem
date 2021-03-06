from ajax.exceptions import AJAXError
import hikari
import json
import django.contrib.auth as auth
import sys
from ajax.decorators import login_required
from hikari import status
from hikari_user.models import HsUser
from hikari_user import on_user_created_func_list
from django.contrib.auth.models import User
from django.conf import settings

def create_user(request):
    
    argJson = request.POST['arg']
    arg = json.loads(argJson)
    now = request.hikari.time
    
    device_model = arg["device_model"]
    
    username = None

    while(True):
        username = hikari.create_random_string(settings.RANDOM_USERNAME_LENGTH)
        if not User.objects.filter(username=username).exists() :
            break
    
    password = hikari.create_random_string(settings.RANDOM_PASSWORD_LENGTH)
    
    user = User.objects.create_user(username=username,password=password)
    user.save()

    hs_user = HsUser.objects.create(
        user = user,
        device_model = device_model,
        create_at = now
    )
    hs_user.save()
    
    for on_user_created_func in on_user_created_func_list:
        on_user_created_func(user)
    
    return {
        'username': username,
        'password': password,
    }

def login(request):
    
    argJson = request.POST['arg']
    arg = json.loads(argJson)
    
    username = arg['username']
    password = arg['password']
    
    user = auth.authenticate(username=username, password=password)
    if user is not None:
        if user.is_active:
            try:
                auth.login(request, user)
            except:
                print "Unexpected error:", sys.exc_info()[0]
        else:
            raise AJAXError(403, 'user not active')
    else:
        raise AJAXError(401, 'auth fails')

    status.set_update_all(request)
    
    return {}

@login_required
def check_login(request):
    return {}
