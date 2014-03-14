from hikari.models import HsItemPack
from hikari.app_module_util import get_app_module_dict

item_module_dict = get_app_module_dict("hs_item")
item_to_db_func_dict = {}
db_to_item_list_func_dict = {}
redeem_func_dict = {}

for _, item_module in item_module_dict.items():
    if not hasattr(item_module, 'item_to_db_func_dict'):
        continue
    module_item_to_db_func_dict = item_module.item_to_db_func_dict
    for item_key, module_item_to_db_func in module_item_to_db_func_dict.items():
        if item_key in item_to_db_func_dict:
            raise Exception("IIzxvFKz Repeat item key: {item_key}".format(item_key=item_key))
        item_to_db_func_dict[item_key] = module_item_to_db_func

for _, item_module in item_module_dict.items():
    if not hasattr(item_module, 'db_to_item_list_func_dict'):
        continue
    module_db_to_item_list_func_dict = item_module.db_to_item_list_func_dict
    for item_key, module_db_to_item_list_func in module_db_to_item_list_func_dict.items():
        if item_key in db_to_item_list_func_dict:
            raise Exception("Knat0uKx Repeat item key: {item_key}".format(item_key=item_key))
        db_to_item_list_func_dict[item_key] = module_db_to_item_list_func

for _, item_module in item_module_dict.items():
    if not hasattr(item_module, 'redeem_func_dict'):
        continue
    module_redeem_func_dict = item_module.redeem_func_dict
    for item_key, module_redeem_func in module_redeem_func_dict.items():
        if item_key in redeem_func_dict:
            raise Exception("5d3Q3TWa Repeat item key: {item_key}".format(item_key=item_key))
        redeem_func_dict[item_key] = module_redeem_func

def item_list_map_to_db(item_list_map):
    
    item_pack_db = HsItemPack.objects.create(redeem_done=False)
    item_pack_db.save()
     
    for item_key, item_list in item_list_map.items():
        item_to_db_func = item_to_db_func_dict[item_key]
        for item in item_list:
            item_to_db_func(item_pack_db,item)
     
    return item_pack_db
        

def db_to_item_list_map(item_pack_db):
    
    item_list_map = {}

    for item_key, db_to_item_list_func in db_to_item_list_func_dict.items():
        item_list = db_to_item_list_func(item_pack_db)
        if item_list == None:
            continue
        if len(item_list) == 0:
            continue
        item_list_map[item_key] = item_list
    
    return item_list_map


def item_redeem(item_pack_db,user_db,request):
    
    if item_pack_db.redeem_done:
        return
    
    for item_key, redeem_func in redeem_func_dict.items():
        redeem_func(item_pack_db,user_db,request)
    
    item_pack_db.redeem_done = True
    item_pack_db.save()
