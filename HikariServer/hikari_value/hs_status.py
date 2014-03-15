from hikari_value.models import HsUserValue, HsValue

def value(request):
    value_dict = {}

    user_value_db_set = HsUserValue.objects.filter(user=request.user)
    for user_value_db in user_value_db_set:
        value_value = {
            'value_key': user_value_db.value_key,
            'max': user_value_db.max(),
        }

        value_type = user_value_db.type()
        if(value_type==HsValue.TYPE_COUNT):
            value_value['count']=user_value_db.count
        elif(value_type==HsValue.TYPE_TIME):
            value_value['time']=user_value_db.time
            
        value_dict[user_value_db.value_key]=value_value
    
    return value_dict

status_update_dict = {
    'value': value
}
