from hikari_value.models import HsValueItem, HsUserValue

def item_to_db(item_pack_db,item):

    ret = HsValueItem.objects.create(
        item_pack = item_pack_db,
        value_key = item["value_key"],
        value = item["value"],
        change_reason_key = item["change_reason_key"],
        change_reason_msg = item["change_reason_msg"]
    )
    ret.save()
    return ret


def db_to_item_list(item_pack_db):
    
    ret = []
    
    q = HsValueItem.objects.filter(item_pack=item_pack_db).all()
    for db in q:
        ret.append({
             'value_key': db.value_key,
             'value': db.value,
             'change_reason_key': db.change_reason_key,
             'change_reason_msg': db.change_reason_msg
        })
    
    return ret


def redeem(item_pack_db,user_db,request):
    
    q = HsValueItem.objects.filter(item_pack=item_pack_db).all()
    for db in q:
        HsUserValue.objects.get(
            user=user_db,
            value_key=db.value_key
        ).change(
            db.value,
            request.hikari.time,
            db.change_reason_key,
            db.change_reason_msg
        )
    
    if ( len(q) > 0 ) and ( request.user.id == user_db.id ):
        request.hikari.status_update_set.add('value')

item_to_db_func_dict={
    'value': item_to_db
}

db_to_item_list_func_dict={
    'value': db_to_item_list
}

redeem_func_dict={
    'value': redeem
}
