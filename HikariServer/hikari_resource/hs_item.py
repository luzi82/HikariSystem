from hikari_resource.models import HsResourceItem, HsUserResource
from hikari import now64

def item_to_db(item_pack_db,item):

    ret = HsResourceItem.objects.create(
        item_pack = item_pack_db,
        resource_key = item["resource_key"],
        value = item["value"],
        change_reason_key = item["change_reason_key"],
        change_reason_msg = item["change_reason_msg"]
    )
    ret.save()
    return ret


def db_to_item_list(item_pack_db):
    
    ret = []
    
    q = HsResourceItem.objects.filter(item_pack=item_pack_db).all()
    for db in q:
        ret.append({
             'resource_key': db.resource_key,
             'value': db.value,
             'change_reason_key': db.change_reason_key,
             'change_reason_msg': db.change_reason_msg
        })
    
    return ret


def redeem(item_pack_db,user_db,request):
    
    q = HsResourceItem.objects.filter(item_pack=item_pack_db).all()
    for db in q:
        HsUserResource.objects.get(
            user=user_db,
            resource_key=db.resource_key
        ).change(
            db.value,
            request.hikari.time,
            db.change_reason_key,
            db.change_reason_msg
        )
    
    if ( len(q) > 0 ) and ( request.user.id == user_db.id ):
        request.hikari.status_update_set.add('resource')

item_to_db_func_dict={
    'resource': item_to_db
}

db_to_item_list_func_dict={
    'resource': db_to_item_list
}

redeem_func_dict={
    'resource': redeem
}
