from django.test import TestCase
from django.contrib.auth.models import User
from django.test.client import Client
from django.core.urlresolvers import reverse
from django.utils import simplejson
import simplejson
import time

# Create your tests here.

class SimpleTest(TestCase):
     
    TEST_DEVICE_ID = 'test_device_id'
     
    def test_create_user(self):
        
        client = Client()
        
#        response = client.post(reverse('ajax')+"/hs_user/hello.json",{
        response = client.post("/ajax/hs_user/create_user.json",{"arg":simplejson.dumps({
            "device_id":SimpleTest.TEST_DEVICE_ID
        })})
        content=response.content
        result = simplejson.loads(content)
        
#         print result
        
        self.assertEqual(result['success'],True)
        self.assertIn("username",result['data'])
        self.assertIn("password",result['data'])

    def test_login(self):
        
        client = Client()
        
#        response = client.post(reverse('ajax')+"/hs_user/hello.json",{
        response = client.post("/ajax/hs_user/create_user.json",{"arg":simplejson.dumps({
            "device_id":SimpleTest.TEST_DEVICE_ID
        })})
        content=response.content
        result = simplejson.loads(content)
        
        self.assertEqual(result['success'],True)
        username = result['data']['username']
        password = result['data']['password']

        response = client.post("/ajax/hs_user/login.json",{"arg":simplejson.dumps({
            "username":username,
            "password":password,
        })})
        content=response.content
        result = simplejson.loads(content)
        
#         print result
        
        self.assertEqual(result['success'],True)
        self.assertIn('_auth_user_id', client.session)
