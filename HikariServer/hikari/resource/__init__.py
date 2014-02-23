from hikari.models.resource import HsUserResource, HsResource

def status(request):
    
    resource_value_list = []

    user_resource_db_set = HsUserResource.objects.filter(user=request.user)
    for user_resource_db in user_resource_db_set:
        resource_value = {
            'resource_key': user_resource_db.resource_key,
        }

        resource_type = user_resource_db.type()
        if(resource_type==HsResource.TYPE_COUNT):
            resource_value['count']=user_resource_db.count
        elif(resource_type==HsResource.TYPE_TIME):
            resource_value['time']=user_resource_db.time
        resource_value['max']=user_resource_db.max()
            
        resource_value_list.append(resource_value)
    
    return {
        'resource_value_list':resource_value_list
    }

def init_user(user):
    
    resource_db_set = HsResource.objects.all()
    
    for resource_db in resource_db_set:
        
        user_resource_db = HsUserResource.objects.create(
            user=user,
            resource_key=resource_db.key,
            count=resource_db.init_count,
            time=0
        )
        
        user_resource_db.save()
