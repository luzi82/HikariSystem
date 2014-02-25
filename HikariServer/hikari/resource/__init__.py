from hikari_resource.models import HsUserResource, HsResource

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
