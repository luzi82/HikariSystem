from hikari_card.models import HsUserCard

def card_list(request):
    card_dict = {}

    user_card_db_set = HsUserCard.objects.filter(user=request.user)
    for user_card_db in user_card_db_set:
        card_value = {
            'id': user_card_db.id,
            'card_type_key': user_card_db.card_type_key
        }
            
        card_dict[user_card_db.id]=card_value
    
    return card_dict

status_update_dict = {
    'card_list': card_list
}
