from django.contrib.auth.models import User
import json

from ajax.decorators import login_required, stuff_required
from hikari_resource.models import HsUserResource, HsResourceConvertChange
from hikari_gacha.models import HsGacha
from ajax.exceptions import AJAXError
from hikari_mail.models import HsMail

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
            'read': mail_db.read
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
    
    q.read = read;
    q.save()
    
    request.hikari.status_update_set.add('mail')
    
    return {}
