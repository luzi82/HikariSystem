from django.contrib.auth.models import User
from django.core.management import call_command
from django.test import TestCase
from django.test.client import Client
import simplejson

from hikari_resource.models import HsUserResource


# Create your tests here.
class SimpleTest(TestCase):
     
    TEST_DEVICE_MODEL = 'test_device_model'
    
    def test_set_user_resource_count(self):
        
        call_command('csv_in')

        admin = User(username="admin")
        admin.is_staff = True
        admin.is_superuser = True
        admin.set_password("password")
        admin.save()

        client = Client()
        
        response = client.post("/ajax/hikari_user/create_user.json", {"arg":simplejson.dumps({
            "device_model":SimpleTest.TEST_DEVICE_MODEL
        })})
        content = response.content
        result = simplejson.loads(content)
        
        self.assertEqual(result['success'], True)
        username = result['data']['username']
        
        admin = Client()
        
        response = admin.post("/ajax/hikari_user/login.json", {"arg":simplejson.dumps({
            "username":"admin",
            "password":"password",
        })})
        content = response.content
#         print content
        result = simplejson.loads(content)

        self.assertEqual(result['success'], True)
        
        response = admin.post("/ajax/hikari_resource/set_user_resource_count.json", {"arg":simplejson.dumps({
            "username":username,
            "resource_key":"coin",
            "count":10000,
        })})
        content = response.content
        result = simplejson.loads(content)

        self.assertEqual(result['success'], True)
        
        user_db = User.objects.get(username=username)
        
        user_resource_db = HsUserResource.objects.get(user=user_db,resource_key="coin")
        self.assertEqual(user_resource_db.count,10000)
