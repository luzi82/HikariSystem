from django.core.management.base import BaseCommand, CommandError
from django.conf import settings
import os
import string
import importlib
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

#         model_module = importlib.import_module('hikari.models')
#         
#         csv_in_path = settings.BASE_DIR + "/csv_in"
#         for csv_filename in os.listdir(csv_in_path):
#             if not csv_filename.endswith(".csv"):
#                 continue
#             csv_full_filename = csv_in_path + "/" + csv_filename
#             self.stderr.write(csv_full_filename)
#             csv_name = csv_filename[:-4]
#             csv_name_cc = ''.join((n[:1].upper()+n[1:]) for n in string.split(csv_name,'_'))
#             model_name = "Hs"+csv_name_cc
# 
#             self.stderr.write("Import {csv_filename} to {model_name}".format(csv_filename=csv_filename,model_name=model_name))
#             
#             col_name_to_idx = {}
#             csv_reader = csv.reader(open(csv_full_filename))
#             data_list_list = []
#             for row in csv_reader:
#                 data_list_list.append(row)
# #             self.stderr.write(json.dumps(data_list_list))
#             key_list = data_list_list[0]
#             for i in xrange(len(key_list)):
#                 col_name_to_idx[key_list[i]] = i
# #             self.stderr.write(json.dumps(col_name_to_idx))
#             
#             model_class = getattr(model_module,model_name)
#             model_class_field_list = model_class._meta.get_all_field_names()
#             model_class.objects.all().delete()
# 
#             for data_list in data_list_list[1:]:
# #                 self.stderr.write(json.dumps(data_list))
#                 db_row_map = {}
#                 for model_class_field in model_class_field_list:
#                     if not model_class_field in col_name_to_idx:
#                         continue
#                     db_row_map[model_class_field] = data_list[col_name_to_idx[model_class_field]]
#                 
# #                 self.stderr.write(json.dumps(db_row_map))
#                 model_data = model_class.objects.create(**db_row_map)
#                 model_data.save()
#                 
#             self.stderr.write("Done".format(csv_name=csv_name,model_name=model_name))

def uncamel(name):
    s1 = re.sub('(.)([A-Z][a-z]+)', r'\1_\2', name)
    return re.sub('([a-z0-9])([A-Z])', r'\1_\2', s1).lower()
