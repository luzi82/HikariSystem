from hikari_value.models import HsUserValue, HsValue

def on_user_created(user):
    
    value_db_set = HsValue.objects.all()
    
    for value_db in value_db_set:
        
        user_value_db = HsUserValue.objects.create(
            user=user,
            value_key=value_db.key,
            count=value_db.init_count,
            time=0
        )
        
        user_value_db.save()
