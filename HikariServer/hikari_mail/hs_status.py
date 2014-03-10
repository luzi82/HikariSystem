from hikari_mail.models import HsMail

def mail(request):
    
    return {
        'read_count' : HsMail.objects.filter(to_user=request.user,read=True).count(),
        'unread_count' : HsMail.objects.filter(to_user=request.user,read=False).count()
    }

status_update_dict = {
    'mail': mail
}
