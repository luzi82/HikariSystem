from hikari_card.models import HsInitUserCard, HsUserCard, HsInitUserDeskCard,\
    HsUserDeskCard

def on_user_created(user):
    
    init_user_card_db_set = HsInitUserCard.objects.all()
    
    init_user_card_dict = {}
    
    for init_user_card_db in init_user_card_db_set:
        
        user_card_db = HsUserCard.objects.create(
            user=user,
            card_type_key=init_user_card_db.card_type_key,
        )
        
        user_card_db.save()
        
        init_user_card_dict[init_user_card_db.key] = user_card_db

    init_user_desk_card_db_set = HsInitUserDeskCard.objects.all()

    for init_user_desk_card_db in init_user_desk_card_db_set:
        
        user_desk_card_db = HsUserDeskCard.objects.create(
            user=user,
            desk_type_key=init_user_desk_card_db.desk_type_key,
            desk_id=init_user_desk_card_db.desk_id,
            desk_pos=init_user_desk_card_db.desk_pos,
            card=init_user_card_dict[init_user_desk_card_db.init_user_card_key]
#             desk_type_key="pet",
#             desk_id=0,
#             desk_pos=0,
#             card=init_user_card_dict[0]
        )
        user_desk_card_db.save()
