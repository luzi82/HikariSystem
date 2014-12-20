import random
import string
import time

# dummy object add to request
class Hikari(object):
    pass

def create_random_string(length):
    return ''.join(random.choice('abcdefghjkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ23456789') for _ in range(length))

# I hate time zone handling
def now64():
    ret = time.time()
    ret *= 1000
    ret = int(ret)
    return ret
