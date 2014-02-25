from django.conf import settings
import importlib
import inspect

def get_app_module_dict(module_name):
    
    ret = {}
    
    for installed_app in settings.INSTALLED_APPS:
        full_module_name = "{installed_app}.{module_name}".format(
            installed_app=installed_app,
            module_name=module_name
        )
        module = None
        try:
            module = importlib.import_module(full_module_name)
        except ImportError:
            continue
        ret[installed_app] = module
    
    return ret

def get_func_list(module):
    
    ret = []

    for func_name in dir(module):
        func = getattr(module,func_name)
        if not inspect.isfunction(func):
            continue
        if func.__module__ != module.__name__ :
            continue
        ret.append(func)
    
    return ret

def get_app_module_func_dict(module_name,func_name):

    ret = {}
    
    for installed_app in settings.INSTALLED_APPS:
        full_module_name = "{installed_app}.{module_name}".format(
            installed_app=installed_app,
            module_name=module_name
        )
        module = None
        try:
            module = importlib.import_module(full_module_name)
        except ImportError:
            continue
        if not hasattr(module,func_name):
            continue
        func = getattr(module,func_name)
        if not inspect.isfunction(func):
            continue
        ret[installed_app] = func
    
    return ret
