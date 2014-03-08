from django.contrib.auth.models import User
import json

from ajax.decorators import login_required, stuff_required
from hikari_resource.models import HsUserResource, HsResourceConvertChange,\
    HsResourceConvert, HsResourceConvertHistory, HsResourceChangeHistory
from ajax.exceptions import AJAXError


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

    if count <= 0:
        raise AJAXError(400, "count <= 0")
    resource_convert_db = HsResourceConvert.objects.get(key=resource_convert_key)
    resource_convert_db.check_resource(user,now,count)
    
    # process
    
    resource_convert_db.process(user,now,count)
    HsResourceConvertHistory.objects.create(
        user=user,
        time=now,
        resource_convert_key=resource_convert_key,
        count=count
    ).save()
     
    request.hikari.status_update_set.add('resource')

    return {}


@login_required
def get_convert_history_list(request):

    argJson = request.POST['arg']
    arg = json.loads(argJson)
    user = request.user

    offset = arg["offset"]
    count = arg["count"]

    # check
    
    if count <= 0:
        raise AJAXError(400, "count <= 0")

    # process

    convert_history_list_q = HsResourceConvertHistory.objects.filter(user=user).order_by('-time').all()[offset:offset+count]
    
    ret = []

    for convert_history_list_db in convert_history_list_q:
        pass
        ret.append({
            'time': convert_history_list_db.time,
            'resource_convert_key': convert_history_list_db.resource_convert_key,
            'count': convert_history_list_db.count,
        })

    return ret

@login_required
def get_change_history_list(request):
    
    argJson = request.POST['arg']
    arg = json.loads(argJson)
    user = request.user

    resource_key = arg["resource_key"]
    offset = arg["offset"]
    count = arg["count"]

    # check
    
    if offset < 0:
        raise AJAXError(400, "count <= 0")
    if count <= 0:
        raise AJAXError(400, "count <= 0")

    # process
    
    convert_change_list_q = HsResourceChangeHistory.objects.filter(user=user,resource_key=resource_key).order_by('-time').all()[offset:offset+count]
    
    ret = []
    
    for convert_change_list_db in convert_change_list_q:
        pass
        ret.append({
            'time': convert_change_list_db.time,
            'resource_key': convert_change_list_db.resource_key,
            'count': convert_change_list_db.count,
            'change_reason_key': convert_change_list_db.change_reason_key,
            'msg': convert_change_list_db.msg,
        })

    return ret
