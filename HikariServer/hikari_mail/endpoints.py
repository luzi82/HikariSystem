from django.contrib.auth.models import User
import json

from ajax.decorators import login_required, stuff_required
from ajax.exceptions import AJAXError
from hikari_mail.models import HsMail
from hikari.item_pack import item_list_map_to_db, db_to_item_list_map,\
    item_redeem

@login_required
def send_mail(request):

    argJson = request.POST['arg']
    arg = json.loads(argJson)
    user = request.user
    now = request.hikari.time
    
    # arg
    
    username = arg["username"]
    title = arg["title"]
    message = arg["message"]
    
    # process
    
    to_user = User.objects.get(username=username)
    HsMail.objects.create(
        time = now,
        from_user = user,
        to_user = to_user,
        title = title,
        message = message,
        read = False
    ).save()
    
    return {}

@login_required
def get_mail_list(request):

    argJson = request.POST['arg']
    arg = json.loads(argJson)
    user = request.user
    
    # arg
     
    read = arg["read"]
    unread = arg["unread"]
    offset = arg["offset"]
    count = arg["count"]
    
    # check
    
    if offset < 0:
        raise AJAXError(400, "offset < 0")
    if count <= 0:
        raise AJAXError(400, "count <= 0")
    
    # process
    
    q = HsMail.objects.filter(to_user=user);
    if not read:
        q = q.filter(read=False)
    if not unread:
        q = q.filter(read=True)
    q = q.all()[offset:offset+count]
    
    ret = []
    
    for mail_db in q:
        ret.append({
            'id': mail_db.id,
            'time': mail_db.time,
            'from_username': mail_db.from_user.username,
            'title': mail_db.title,
            'message': mail_db.message,
            'read': mail_db.read,
            'item_list_map': db_to_item_list_map(mail_db.item_pack)
        })
    
    return ret


@login_required
def set_read(request):
    
    argJson = request.POST['arg']
    arg = json.loads(argJson)
    user = request.user
    
    # arg

    mail_id = arg["mail_id"]
    read = arg["read"]

    # check
    
    q = HsMail.objects.get(id=mail_id)
    if q.to_user != user:
        raise AJAXError(403, "no access")
    
    # process
    
    if read:
        if q.item_pack != None:
            item_redeem(q.item_pack,q.to_user,request)
    
    q.read = read;
    q.save()
    
    request.hikari.status_update_set.add('mail')
    
    return {}


@stuff_required
def send_gift_mail(request):
    argJson = request.POST['arg']
    arg = json.loads(argJson)
    user = request.user
    now = request.hikari.time

    # arg
    
    username = arg["username"]
    title = arg["title"]
    message = arg["message"]
    item_list_map = arg["item_list_map"]

    # check
    
    to_user = User.objects.get(username=username)
    
    # process
    
    item_pack_db = item_list_map_to_db(item_list_map)
    HsMail.objects.create(
        time = now,
        from_user = user,
        to_user = to_user,
        title = title,
        message = message,
        read = False,
        item_pack = item_pack_db
    ).save()
    
    return {}
