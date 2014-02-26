import hikari;

def get_time(request):
    return {
        'time': hikari.now64()
    }
