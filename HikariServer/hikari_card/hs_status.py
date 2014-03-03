from hikari_card.models import HsUserCard

def card(request):
    ret = {}

    user_card_db_set = HsUserCard.objects.filter(user=request.user)
    for user_card_db in user_card_db_set:
        card_value = {
            'id': user_card_db.id,
            'card_type_key': user_card_db.card_type_key
        }
            
        ret[user_card_db.id]=card_value
    
    return ret

status_update_dict = {
    'card': card
}
