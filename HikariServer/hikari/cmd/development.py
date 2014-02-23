import hikari;
import json
from ajax.exceptions import AJAXError

def pass_time(request):
    
    argJson = request.POST['arg']
    arg = json.loads(argJson)
    now = hikari.now64()

    start = arg['start']
    end = arg['end']
    
    if (start!=-1) and (now<start):
        raise AJAXError(500, 'now<start')
    
    if (end!=-1) and (now>end):
        raise AJAXError(500, 'now>end')
    
    return {
        'time': now
    }
