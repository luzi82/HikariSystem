from django.conf import settings
import json
from hikari import now64
from ajax.exceptions import AJAXError
import hikari
from ajax.decorators import stuff_required

def create_admin_user(request):

    argJson = request.POST['arg']
    arg = json.loads(argJson)
    now = now64()
    
    secret = arg['secret']
    
    backdoor_filename = settings.BASE_DIR+"/backdoor.json"
    backdoor_input = open(backdoor_filename)
    backdoor_data = json.load(backdoor_input)
    backdoor_input.close()

    if secret == None :
        raise AJAXError(403)
    if backdoor_data['deadline'] == None :
        raise AJAXError(403)
    if backdoor_data['secret'] == None :
        raise AJAXError(403)
    if now > backdoor_data['deadline'] :
        raise AJAXError(403)
    if secret != backdoor_data['secret'] :
        raise AJAXError(403)
    
    user_data = hikari.create_random_user(username_length=32,password_length=32)
    user_data['user'].is_staff = True
    user_data['user'].is_superuser = True
    user_data['user'].save()

    return {
        'username': user_data['username'],
        'password': user_data['password'],
    }

@stuff_required
def check_admin(request):
    return {}
