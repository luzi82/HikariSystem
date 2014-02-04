from ajax.exceptions import AJAXError
import hs_user
import json
import django.contrib.auth as auth
import sys
from ajax.decorators import login_required
from hs_profile.models import HsUserProfile

@login_required
def set_name(request):

    argJson = request.POST['arg']
    arg = json.loads(argJson)
    
    user = request.user 
    name = arg['name']
    
    profile, _ = HsUserProfile.objects.get_or_create(user=user)
    profile.name = name
    profile.save();
    
    return {}

@login_required
def get_profile(request):

    argJson = request.POST['arg']
    arg = json.loads(argJson)
    
    user = request.user 
    
    profile, _ = HsUserProfile.objects.get_or_create(user=user)
    
    return {
        "profile": profile.to_dict(),
    }

# def hello(request):
#     print request
#     
#     if len(request.POST):
#         return request.POST
#     else:
#         raise AJAXError(500, 'Nothing to echo back.')

# def create_user(request):
#     
# #     device_id = request.POST["device_id"]
# 
#     user_data = hs_user.create_random_user()
# 
#     return {
#         'username': user_data['username'],
#         'password': user_data['password'],
#     }
# 
# def login(request):
# 
#     argJson = request.POST['arg']
#     arg = json.loads(argJson)
#     
#     username = arg['username']
#     password = arg['password']
#     
#     user = auth.authenticate(username=username, password=password)
#     if user is not None:
#         if user.is_active:
#             try:
#                 auth.login(request, user)
#             except:
#                 print "Unexpected error:", sys.exc_info()[0]
#         else:
#             raise AJAXError(403, 'user not active')
#     else:
#         raise AJAXError(403, 'auth fails')
#         
#     return {}
# 
# @login_required
# def check_login(request):
#     return {}
