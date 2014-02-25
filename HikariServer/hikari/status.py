from hikari.app_module_util import get_app_module_dict, get_func_list

status_module_dict = get_app_module_dict("hs_status")
status_func_list = []

for _, status_module in status_module_dict.items():
    for func in get_func_list(status_module):
        status_func_list.append(func)
        

def prepare_status_update(request):
    request.hikari.status_update_set = set()


def set_update_all(request):
    for status_func in status_func_list:
        request.hikari.status_update_set.add(status_func.__name__)


def put_status_update(request,result):
#     print '8NSdZt3V'
    
    if (len(request.hikari.status_update_set)==0):
        return
    
    status_update_dict = {}
    
    for func in status_func_list:
        if not func.__name__ in request.hikari.status_update_set:
            continue
        status_update_dict[func.__name__] = func(request)
        
    result['status_update_dict'] = status_update_dict
