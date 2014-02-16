from ajax.decorators import stuff_required

@stuff_required
def check_admin(request):
    return {}
