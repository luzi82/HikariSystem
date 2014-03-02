from hikari_card.models import HsInitUserCard, HsUserCard

def on_user_created(user):
    
    init_user_card_db_set = HsInitUserCard.objects.all()
    
    for init_user_card_db in init_user_card_db_set:
        
        user_card_db = HsUserCard.objects.create(
            user=user,
            card_type_key=init_user_card_db.card_type_key,
        )
        
        user_card_db.save()
