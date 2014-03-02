from django.contrib.auth.models import User
import json

from ajax.decorators import login_required, stuff_required
from hikari_resource.models import HsUserResource, HsResourceConvertChange,\
    HsResourceConvert


@stuff_required
def set_user_resource_count(request):
    
    argJson = request.POST['arg']
    arg = json.loads(argJson)

    username = arg["username"]
    resource_key = arg["resource_key"]
    count = int(arg["count"])
    
    user_db = User.objects.get(username=username)

    user_resource_db = HsUserResource.objects.get(user=user_db,resource_key=resource_key)
    user_resource_db.count = count
    user_resource_db.save()

    return {}

@login_required
def convert(request):

    argJson = request.POST['arg']
    arg = json.loads(argJson)
    now = request.hikari.time
    user = request.user
     
    resource_convert_key = arg["resource_convert_key"]
    count = arg["count"]
    
    # check
    
    resource_convert_db = HsResourceConvert.objects.get(key=resource_convert_key)
    resource_convert_db.check_resource(user,now,count)
    
    # process
    
    resource_convert_db.process(user,now,count)
     
    request.hikari.status_update_set.add('resource')

    return {}
