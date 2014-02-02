from ajax.exceptions import AJAXError
import hs_user

# def hello(request):
#     print request
#     
#     if len(request.POST):
#         return request.POST
#     else:
#         raise AJAXError(500, 'Nothing to echo back.')

def create_user(request):
    
#     device_id = request.POST["device_id"]

    user_data = hs_user.create_random_user()

    return {
        'username': user_data['username'],
        'password': user_data['password'],
    }
