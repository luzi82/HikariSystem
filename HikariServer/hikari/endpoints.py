import hikari;
from hikari import status
from ajax.decorators import login_required

def get_time(request):
    return {
        'time': hikari.now64()
    }

@login_required
def sync_status(request):
    status.set_update_all(request)
    return {}
