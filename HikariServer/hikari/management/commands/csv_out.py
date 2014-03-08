from django.core.management.base import BaseCommand
from django.conf import settings
import os
import csv
from django.db import models
import shutil
import inspect
from hikari.app_module_util import get_app_module_dict
import json

class Command(BaseCommand):
    
    help = 'Output csv files from database to static/csv'

    def handle(self, *args, **options):
        
        output_path = settings.BASE_DIR + "/static/csv"
        if(os.path.exists(output_path)):
            shutil.rmtree(output_path)
        os.makedirs(output_path)
        
        for _, models_module in get_app_module_dict("models").items():
            
            for model_name in dir(models_module):
                
                model = getattr(models_module, model_name)
                if not inspect.isclass(model):
                    continue
                if (model.__module__ != models_module.__name__):
                    continue
                if not issubclass(model, models.Model):
                    continue
                if not hasattr(model, "HIKARI_STATIC_NAME"):
                    continue
                
                static_name = model.HIKARI_STATIC_NAME
                member_list = None
                if hasattr(model, "HIKARI_STATIC_MEMBER_LIST"):
                    member_list = model.HIKARI_STATIC_MEMBER_LIST
                else:
                    member_list = model._meta.get_all_field_names()
                
                param_value_list_dict = {}
                param_value_default_dict = {}
                if hasattr(model, "HIKARI_STATIC_PARAM_DICT"):
                    param_list = model.HIKARI_STATIC_PARAM_DICT.keys()
                    for param, model0 in model.HIKARI_STATIC_PARAM_DICT.iteritems():
                        param_value_list_dict[param] = model.objects.only(param).distinct().values_list(param, flat=True)
                        param_value_default_dict[param] = model0.objects.get(is_default=True).key
                else:
                    param_list = []
                
                self._create_csv({},param_list,param_value_list_dict,param_value_default_dict,output_path,static_name,member_list,model)
                    
#                 else:
#                     with open(output_path + '/' + static_name + '.csv', 'wb') as csvfile:
#     
#                         csv_writer = csv.writer(csvfile)
#                         csv_writer.writerow(member_list)
#                         
#                         for model_object in model.objects.all():
#                             row = []
#                             for member in member_list:
#                                 row.append(getattr(model_object, member))
#                             csv_writer.writerow(row)


    def _create_csv(self,filter_dict,param_list,param_value_list_dict,param_value_default_dict,output_path,static_name,member_list,model):
        if len(filter_dict) >= len(param_list):
            param_fn = ""
            for p in param_list:
                if filter_dict[p] == None:
                    param_fn += "-_"
                    filter_dict[p] = param_value_default_dict[p]
                else:
                    param_fn += "-"+filter_dict[p]
            with open(output_path + '/' + static_name + param_fn + '.csv', 'wb') as csvfile:
                csv_writer = csv.writer(csvfile)
                csv_writer.writerow(member_list)
                
                for model_object in model.objects.filter(**filter_dict).all():
                    row = []
                    for member in member_list:
                        row.append(getattr(model_object, member))
                    csv_writer.writerow(row)
        else:
            p = param_list[len(filter_dict)]
            param_value_list = [None]
            param_value_list.extend(param_value_list_dict[p])
            for param_value in param_value_list:
                filter_dict_0 = filter_dict.copy()
                filter_dict_0[p] = param_value
                self._create_csv(filter_dict_0,param_list,param_value_list_dict,param_value_default_dict,output_path,static_name,member_list,model)
