import json

from ajax.decorators import login_required
from hikari_quest.models import HsQuestEntry, HsQuestInstance


@login_required
def quest_start(request):
    
    argJson = request.POST['arg']
    arg = json.loads(argJson)
    now = request.hikari.time
    
    quest_entry_key = arg['quest_entry_key']
    
    quest_entry = HsQuestEntry.objects.get(key=quest_entry_key)
    quest_entry.check_resource(request.user,now)
    quest_entry.reduce_resource(request.user,now)
    
    HsQuestInstance.objects.filter(
        user=request.user,
        state=HsQuestInstance.STATE_STARTED
    ).update(
        state=HsQuestInstance.STATE_CANCEL_BY_NEW,
        complete_at=now
    )
    quest_instance = HsQuestInstance.objects.create(
        user=request.user,
        entry_key=quest_entry_key,
        state=HsQuestInstance.STATE_STARTED,
        create_at=now
    )
    quest_instance.save()

    request.hikari.status_update_set.add('resource')

    return {
        'quest_instance': {
            'id': quest_instance.id
        }
    }

@login_required
def quest_end(request):
    
    argJson = request.POST['arg']
    arg = json.loads(argJson)
    now = request.hikari.time

    quest_instance_id = arg['quest_instance_id']
    success = arg['success']

    quest_instance = HsQuestInstance.objects.get(
        id=quest_instance_id,
        user=request.user,
        state=HsQuestInstance.STATE_STARTED
    )
    quest_instance.state = HsQuestInstance.STATE_SUCCESS if success else HsQuestInstance.STATE_FAIL
    quest_instance.complete_at = now
    quest_instance.save()

    if success :
        quest_entry = HsQuestEntry.objects.get(key=quest_instance.entry_key)
        quest_entry.reward_resource(request.user,now)
    
    request.hikari.status_update_set.add('resource')

    return {}
