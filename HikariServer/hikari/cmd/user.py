from ajax.exceptions import AJAXError
import hikari
import json
import django.contrib.auth as auth
import sys
from ajax.decorators import login_required
from hikari.models import HsUser
from hikari import now64

def create_user(request):
    
    argJson = request.POST['arg']
    arg = json.loads(argJson)
    now = now64()
    
    device_model = arg["device_model"]
    
    user_data = hikari.create_random_user()
    
    hs_user = HsUser.objects.create(
        user = user_data['user'],
        device_model = device_model,
        create_at = now
    )
    hs_user.save()

    return {
        'username': user_data['username'],
        'password': user_data['password'],
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
        raise AJAXError(403, 'auth fails')
        
    return {}

@login_required
def check_login(request):
    return {}
