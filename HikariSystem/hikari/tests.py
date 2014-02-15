from django.test import TestCase
from django.test.client import Client
import simplejson
from hikari.models import HsUser
from django.contrib.auth.models import User

# Create your tests here.

class SimpleTest(TestCase):
     
    TEST_DEVICE_MODEL = 'test_device_model'
     
    def test_create_user(self):
        
        client = Client()
        
#        response = client.post(reverse('ajax')+"/hikari/hello.json",{
        response = client.post("/ajax/hikari/create_user.json",{"arg":simplejson.dumps({
            "device_model":SimpleTest.TEST_DEVICE_MODEL
        })})
        content=response.content
        result = simplejson.loads(content)
        
        self.assertEqual(result['success'],True)
        self.assertIn("username",result['data'])
        self.assertIn("password",result['data'])
        
        user = User.objects.get(username = result['data']['username'])
        hsuser = HsUser.objects.get(user = user)
        self.assertEqual(hsuser.device_model,SimpleTest.TEST_DEVICE_MODEL)

    def test_login(self):
        
        client = Client()
        
#        response = client.post(reverse('ajax')+"/hikari/hello.json",{
        response = client.post("/ajax/hikari/create_user.json",{"arg":simplejson.dumps({
            "device_model":SimpleTest.TEST_DEVICE_MODEL
        })})
        content=response.content
        result = simplejson.loads(content)
        
        self.assertEqual(result['success'],True)
        username = result['data']['username']
        password = result['data']['password']

        response = client.post("/ajax/hikari/login.json",{"arg":simplejson.dumps({
            "username":username,
            "password":password,
        })})
        content=response.content
        result = simplejson.loads(content)
        
#         print result
        
        self.assertEqual(result['success'],True)
        self.assertIn('_auth_user_id', client.session)
