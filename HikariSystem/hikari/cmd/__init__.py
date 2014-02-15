import imp
import os
import inspect
import hikari.endpoints as endpoints
import importlib

_, hikari_filename, _ = imp.find_module("hikari")
_, hikaricmd_filename, _ = imp.find_module("cmd", [hikari_filename])
 
for module_filename in os.listdir(hikaricmd_filename):
    if module_filename == "__init__.py":
        continue
    if not module_filename.endswith(".py"):
        continue
    module_filename_noext, _ = os.path.splitext(module_filename)
    module_name = "hikari.cmd." + module_filename_noext
    module = importlib.import_module(module_name, "hikari.cmd")
    for module_func in inspect.getmembers(module, inspect.isfunction):
        name, func = module_func
        if func.__module__ != module_name :
            continue
        setattr(endpoints, module_filename_noext + "__" + name, func)
