from django.contrib.auth.models import User
import json

from ajax.decorators import login_required, stuff_required
from hikari_value.models import HsUserValue, HsValueConvertChange,\
    HsValueConvert, HsValueConvertHistory, HsValueChangeHistory
from ajax.exceptions import AJAXError


@stuff_required
def set_user_value_count(request):
    
    argJson = request.POST['arg']
    arg = json.loads(argJson)

    username = arg["username"]
    value_key = arg["value_key"]
    count = int(arg["count"])
    
    user_db = User.objects.get(username=username)

    user_value_db = HsUserValue.objects.get(user=user_db,value_key=value_key)
    user_value_db.count = count
    user_value_db.save()

    return {}

@login_required
def convert(request):

    argJson = request.POST['arg']
    arg = json.loads(argJson)
    now = request.hikari.time
    user = request.user
     
    value_convert_key = arg["value_convert_key"]
    count = arg["count"]
    
    # check

    if count <= 0:
        raise AJAXError(400, "count <= 0")
    value_convert_db = HsValueConvert.objects.get(key=value_convert_key)
    value_convert_db.check_value(user,now,count)
    
    # process
    
    value_convert_db.process(user,now,count)
    HsValueConvertHistory.objects.create(
        user=user,
        time=now,
        value_convert_key=value_convert_key,
        count=count
    ).save()
     
    request.hikari.status_update_set.add('value')

    return {}


@login_required
def get_convert_history_list(request):

    argJson = request.POST['arg']
    arg = json.loads(argJson)
    user = request.user

    offset = arg["offset"]
    count = arg["count"]

    # check
    
    if offset < 0:
        raise AJAXError(400, "offset < 0")
    if count <= 0:
        raise AJAXError(400, "count <= 0")

    # process

    convert_history_list_q = HsValueConvertHistory.objects.filter(user=user).order_by('-time').all()[offset:offset+count]
    
    ret = []

    for convert_history_list_db in convert_history_list_q:
        pass
        ret.append({
            'time': convert_history_list_db.time,
            'value_convert_key': convert_history_list_db.value_convert_key,
            'count': convert_history_list_db.count,
        })

    return ret

@login_required
def get_change_history_list(request):
    
    argJson = request.POST['arg']
    arg = json.loads(argJson)
    user = request.user

    value_key = arg["value_key"]
    offset = arg["offset"]
    count = arg["count"]

    # check
    
    if offset < 0:
        raise AJAXError(400, "offset < 0")
    if count <= 0:
        raise AJAXError(400, "count <= 0")

    # process
    
    convert_change_list_q = HsValueChangeHistory.objects.filter(user=user,value_key=value_key).order_by('-time').all()[offset:offset+count]
    
    ret = []
    
    for convert_change_list_db in convert_change_list_q:
        pass
        ret.append({
            'time': convert_change_list_db.time,
            'value_key': convert_change_list_db.value_key,
            'value': convert_change_list_db.value,
            'change_reason_key': convert_change_list_db.change_reason_key,
            'change_reason_msg': convert_change_list_db.change_reason_msg,
        })

    return ret
