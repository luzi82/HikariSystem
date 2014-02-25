from django.core.management.base import BaseCommand
from django.conf import settings
import os
import csv
from django.db import models
import shutil
import importlib
import inspect

class Command(BaseCommand):
    
    help = 'Output csv files from database to static/csv'

    def handle(self, *args, **options):
        
        output_path = settings.BASE_DIR + "/static/csv"
        if(os.path.exists(output_path)):
            shutil.rmtree(output_path)
        os.makedirs(output_path)
        
        for installed_app in settings.INSTALLED_APPS:
            models_module_name = "{installed_app}.models".format(installed_app=installed_app)
            models_module = importlib.import_module(models_module_name)
            
            for model_name in dir(models_module):
                
                model = getattr(models_module,model_name)
                if not inspect.isclass(model):
                    continue
                if ( model.__module__ != models_module_name ):
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
                    
                with open(output_path+'/'+static_name+'.csv', 'wb') as csvfile:

                    csv_writer = csv.writer(csvfile)
                    csv_writer.writerow(member_list)
                    
                    for model_object in model.objects.all():
                        row = []
                        for member in member_list:
                            row.append(getattr(model_object,member))
                        csv_writer.writerow(row)
