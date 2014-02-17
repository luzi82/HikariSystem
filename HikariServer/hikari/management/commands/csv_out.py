from django.core.management.base import BaseCommand
from django.conf import settings
import os
import csv
from hikari import models
import re
import shutil

class Command(BaseCommand):
    
    help = 'Output csv files from database to static/csv'

    def handle(self, *args, **options):
        
        output_path = settings.BASE_DIR + "/static/csv_out"
        if(os.path.exists(output_path)):
            shutil.rmtree(output_path)
        os.makedirs(output_path)
        
        for model_name, col_list in settings.HIKARI_CSV_OUTPUT.items():

            csv_name = uncamel(model_name)
            model = getattr(models, model_name)
            
            with open(output_path+'/'+csv_name+'.csv', 'wb') as csvfile:

                csv_writer = csv.writer(csvfile)
                csv_writer.writerow(col_list)
                
                for model_object in model.objects.all():
                    row = []
                    for col in col_list:
                        row.append(getattr(model_object,col))
                    csv_writer.writerow(row)

def uncamel(name):
    s1 = re.sub('(.)([A-Z][a-z]+)', r'\1_\2', name)
    return re.sub('([a-z0-9])([A-Z])', r'\1_\2', s1).lower()
