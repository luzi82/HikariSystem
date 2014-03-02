from django.contrib.auth.models import User
import json

from ajax.decorators import login_required, stuff_required
from hikari import now64
from hikari_resource.models import HsUserResource, HsResourceConvertChange


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
     
    resource_convert_change_db_list = HsResourceConvertChange.objects.filter(resource_convert_key=resource_convert_key)
    for resource_convert_change_db in resource_convert_change_db_list:
        resource_convert_change_db.check_resource(user,count,now)
        
    for resource_convert_change_db in resource_convert_change_db_list:
        resource_convert_change_db.process(user,count,now)
    
    request.hikari.status_update_set.add('resource')

    return {}
