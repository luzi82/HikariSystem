from django.contrib.auth.models import User
import json

from ajax.decorators import login_required, stuff_required
from hikari_resource.models import HsUserResource, HsResourceConvertChange,\
    HsResourceConvert
from hikari_card.models import HsDeskType
from ajax.exceptions import AJAXError


@stuff_required
def set_desk(request):
    
    argJson = request.POST['arg']
    arg = json.loads(argJson)

    desk_type_key = arg["desk_type_key"]
    desk_id = arg["desk_id"]
    card_list = arg["card_list"]

    desk_type_db = HsDeskType.objects.get(key=desk_type_key)
    if desk_id >= desk_type_db.desk_count:
        raise AJAXError(400,"desk_id >= desk_type_db.size")
    if len(card_list) != desk_type_db.card_list_length:
        raise AJAXError(400,"len(card_list) != desk_type_db.card_list_length")
    
    