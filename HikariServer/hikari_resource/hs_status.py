from hikari_resource.models import HsUserResource, HsResource

def resource(request):
    resource_dict = {}

    user_resource_db_set = HsUserResource.objects.filter(user=request.user)
    for user_resource_db in user_resource_db_set:
        resource_value = {
            'resource_key': user_resource_db.resource_key,
            'max': user_resource_db.max(),
        }

        resource_type = user_resource_db.type()
        if(resource_type==HsResource.TYPE_COUNT):
            resource_value['count']=user_resource_db.count
        elif(resource_type==HsResource.TYPE_TIME):
            resource_value['time']=user_resource_db.time
            
        resource_dict[user_resource_db.resource_key]=resource_value
    
    return resource_dict

status_update_dict = {
    'resource': resource
}
