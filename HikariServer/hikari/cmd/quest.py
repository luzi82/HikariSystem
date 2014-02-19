import json
from hikari.models.quest import HsQuestEntry, HsQuestInstance
from ajax.decorators import login_required
from hikari import now64
from django.core.exceptions import FieldError

@login_required
def quest_start(request):
    
    argJson = request.POST['arg']
    arg = json.loads(argJson)
    now = now64()

    quest_entry_key = arg['quest_entry_key']
    
    quest_entry = HsQuestEntry.objects.get(key=quest_entry_key)
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

    return {
        'quest_instance': {
            'id': quest_instance.id
        }
    }

@login_required
def quest_end(request):
    
    argJson = request.POST['arg']
    arg = json.loads(argJson)
    now = now64()

    quest_instance_id = arg['quest_instance_id']
    success = arg['success']

    quest_instance = HsQuestInstance.objects.get(
        id=quest_instance_id,
        user=request.user,
        state=HsQuestInstance.STATE_STARTED
    )
    quest_instance.state=HsQuestInstance.STATE_SUCCESS if success else HsQuestInstance.STATE_FAIL
    quest_instance.complete_at=now
    quest_instance.save()

    return {}
