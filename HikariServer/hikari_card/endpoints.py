from django.contrib.auth.models import User
import json

from ajax.decorators import login_required, stuff_required
from hikari_resource.models import HsUserResource, HsResourceConvertChange, \
    HsResourceConvert
from hikari_card.models import HsDeskType, HsUserCard, HsUserDeskCard
from ajax.exceptions import AJAXError


@login_required
def set_desk(request):
    
    argJson = request.POST['arg']
    arg = json.loads(argJson)

    desk_type_key = arg["desk_type_key"]
    desk_id = arg["desk_id"]
    card_list = arg["card_list"]

    desk_type_db = HsDeskType.objects.get(key=desk_type_key)
    if desk_id >= desk_type_db.desk_count:
        raise AJAXError(400, "desk_id >= desk_type_db.size")
    if len(card_list) != desk_type_db.card_list_length:
        raise AJAXError(400, "len(card_list) != desk_type_db.card_list_length")
    for i in xrange(len(card_list)):
        for j in xrange(len(card_list)):
            if i == j:
                continue
            if card_list[i] == card_list[j]:
                raise AJAXError(400, "card_list[i] == card_list[j]")
    for card_id in card_list:
        exist = HsUserCard.objects.filter(user=request.user,id=card_id).exists()
        if not exist:
            raise AJAXError(400, "not exist or not own")
        
    
    for i in xrange(desk_type_db.card_list_length):
        user_card_db = HsUserCard.objects.get(user=request.user,id=card_list[i])
        user_desk_card_db, created = HsUserDeskCard.objects.get_or_create(
            user=request.user,
            desk_type_key=desk_type_key,
            desk_id=desk_id,
            desk_pos=i,
#             card=user_card_db
            defaults={
                'card': user_card_db
            }
        )
        if not created:
            user_desk_card_db.card = user_card_db
        user_desk_card_db.save()
        
    request.hikari.status_update_set.add('desk')
