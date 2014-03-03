from django.contrib.auth.models import User
import json

from ajax.decorators import login_required, stuff_required
from hikari_resource.models import HsUserResource, HsResourceConvertChange
from hikari_gacha.models import HsGacha


@login_required
def gacha(request):

    argJson = request.POST['arg']
    arg = json.loads(argJson)
    now = request.hikari.time
    
    # arg
     
    gacha_key = arg["gacha_key"]
    
    # check
    
    gacha_db = HsGacha.objects.get(key=gacha_key)
    gacha_db.check_resource(request.user,now)
    
    # process
    
    user_card_id_list = gacha_db.process(request.user,now)
      
    request.hikari.status_update_set.add('card')
    request.hikari.status_update_set.add('resource')

    return {
        'user_card_id_list': user_card_id_list
    }

    return {}
