import json
from ajax.exceptions import AJAXError

def pass_time(request):
    
    argJson = request.POST['arg']
    arg = json.loads(argJson)
    now = request.hikari.time

    start = arg['start']
    end = arg['end']
    
    if (start!=-1) and (now<start):
        raise AJAXError(400, 'now<start')
    
    if (end!=-1) and (now>end):
        raise AJAXError(400, 'now>end')
    
    return {
        'time': now
    }
