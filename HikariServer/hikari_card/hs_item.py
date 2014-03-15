from hikari_card.models import HsUserCard, HsCardItem

def item_to_db(item_pack_db,item):

    ret = HsCardItem.objects.create(
        item_pack = item_pack_db,
        card_type_key = item["card_type_key"],
    )
    ret.save()
    return ret


def db_to_item_list(item_pack_db):
    
    ret = []
    
    q = HsCardItem.objects.filter(item_pack=item_pack_db).all()
    for db in q:
        ret.append({
             'card_type_key': db.card_type_key
        })
    
    return ret


def redeem(item_pack_db,user_db,request):
    
    q = HsCardItem.objects.filter(item_pack=item_pack_db).all()
    for db in q:
        HsUserCard.objects.create(
            user=user_db,
            card_type_key=db.card_type_key
        )
    
    if ( len(q) > 0 ) and ( request.user.id == user_db.id ):
        request.hikari.status_update_set.add('card')

item_to_db_func_dict={
    'card': item_to_db
}

db_to_item_list_func_dict={
    'card': db_to_item_list
}

redeem_func_dict={
    'card': redeem
}
