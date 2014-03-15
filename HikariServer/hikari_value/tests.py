from django.contrib.auth.models import User
from django.core.management import call_command
from django.test import TestCase
from django.test.client import Client
import simplejson

from hikari_value.models import HsUserValue


# Create your tests here.
class SimpleTest(TestCase):
     
    TEST_DEVICE_MODEL = 'test_device_model'
    
    def test_set_user_value_count(self):
        
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
        
        response = admin.post("/ajax/hikari_value/set_user_value_count.json", {"arg":simplejson.dumps({
            "username":username,
            "value_key":"coin",
            "count":10000,
        })})
        content = response.content
        result = simplejson.loads(content)

        self.assertEqual(result['success'], True)
        
        user_db = User.objects.get(username=username)
        
        user_value_db = HsUserValue.objects.get(user=user_db,value_key="coin")
        self.assertEqual(user_value_db.count,10000)
