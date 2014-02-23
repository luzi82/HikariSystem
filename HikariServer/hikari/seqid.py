from decorator import decorator
from django.http import HttpResponse
from django.http.response import HttpResponseServerError
import json

from hikari import now64


# def to_response(data):
#     if isinstance(data, HttpResponse):
#         return data
#     else:
#         payload = {
#             'success': True,
#             'data': data
#         }
#         v = json.dumps(payload, separators=(',', ':'));
#         return HttpResponse(v)

@decorator
def seqid(f, *args, **kwargs):
    request = args[0]
    if 'seqid' in request.COOKIES:
        seqid = int(request.COOKIES['seqid'])
    else:
        seqid = None
        
    session_seqid = request.session.get('next_seqid')

#     print "seqid "+str(seqid)+" session_seqid "+str(session_seqid)

    if seqid == session_seqid:
#         print "seqid == session_seqid"
        
        result = f(*args, **kwargs)
#         result = to_response(result)
        
        if result.status_code == 200:
            next_seqid = now64()
            request.session['next_seqid'] = next_seqid
            result.set_cookie('seqid', str(next_seqid))
        
        if (seqid != None):
            request.session['last_seqid'] = seqid
            request.session['last_content'] = result.content

        return result
    
    last_seqid = request.session.get('last_seqid')

    if seqid == last_seqid:
#         print "seqid == last_seqid"
        
        last_content = request.session.get('last_content')
        result = HttpResponse(last_content)
        
        next_seqid = request.session['next_seqid']
        result.set_cookie('seqid', str(next_seqid))
        
        return result

    result = HttpResponseServerError("bad seqid")
    return result;
