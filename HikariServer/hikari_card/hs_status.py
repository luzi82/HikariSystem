from hikari_card.models import HsUserCard, HsDeskType, HsUserDeskCard

def card(request):
    ret = {}

    user_card_db_set = HsUserCard.objects.filter(user=request.user)
    for user_card_db in user_card_db_set:
        card_value = {
            'id': user_card_db.id,
            'card_type_key': user_card_db.card_type_key,
            'value_dict': user_card_db.value_dict()
        }
            
        ret[user_card_db.id] = card_value
    
    return ret


def desk(request):
    ret = {}
    
    desk_type_db_set = HsDeskType.objects.all()
    for desk_type_db in desk_type_db_set:
        ret[desk_type_db.key] = [
            [
                None for _ in xrange(desk_type_db.card_list_length)
            ] for _ in xrange(desk_type_db.desk_count)
        ]
    
    user_desk_card_db_set = HsUserDeskCard.objects.filter(user=request.user)
    for user_desk_card_db in user_desk_card_db_set:
        ret[user_desk_card_db.desk_type_key][user_desk_card_db.desk_id][user_desk_card_db.desk_pos] = user_desk_card_db.card.id
    
    return ret

status_update_dict = {
    'card': card,
    'desk': desk,
}
