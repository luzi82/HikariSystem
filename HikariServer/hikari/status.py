from hikari import resource as hikari_resource

status_func = {}
status_func['resource'] = hikari_resource.status

def prepare_status_update(request):
    request.hikari.status_update_set = set()


def set_update_all(request):
#     print 'LPQIg20f'
    request.hikari.status_update_set.add('resource')


def put_status_update(request,result):
#     print '8NSdZt3V'
    
    if (len(request.hikari.status_update_set)==0):
        return
    
#     print 'UKmLSwzA'
    
    status_update_list = []

    for pkg_name in request.hikari.status_update_set:
        func = status_func[pkg_name]
#         print 'y0cweKn8'
        ret = func(request)
#         print 'Dp7zoII9'
        status_update_list.append({
            'app_name': pkg_name,
            'status': ret
        })
#         print 'OZtpZe9G'
        
    result['status_update_list'] = status_update_list
