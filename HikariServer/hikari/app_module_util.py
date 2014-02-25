from django.conf import settings
import importlib

def get_app_module_dict(module_name):
    
    ret = {}
    
    for installed_app in settings.INSTALLED_APPS:
        full_module_name = "{installed_app}.{module_name}".format(
            installed_app=installed_app,
            module_name=module_name
        )
        module = importlib.import_module(full_module_name)
        ret[full_module_name] = module
    
    return ret
