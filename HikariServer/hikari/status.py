from hikari.app_module_util import get_app_module_dict, get_func_list

status_module_dict = get_app_module_dict("hs_status")
status_func_dict = {}

for _, status_module in status_module_dict.items():
    for func in get_func_list(status_module):
        status_key = func.__name__
        if status_key in status_func_dict:
            raise Exception("xD2j06bT Repeat status key: {status_key}".format(status_key=status_key))
        status_func_dict[status_key] = func
        

def prepare_status_update(request):
    request.hikari.status_update_set = set()


def set_update_all(request):
    for status_key, _ in status_func_dict.items():
        request.hikari.status_update_set.add(status_key)


def put_status_update(request,result):
#     print '8NSdZt3V'
    
    if (len(request.hikari.status_update_set)==0):
        return
    
    status_update_dict = {}
    
    for status_key in request.hikari.status_update_set:
        func = status_func_dict[status_key]
        status_update_dict[status_key] = func(request)
    
    result['status_update_dict'] = status_update_dict
