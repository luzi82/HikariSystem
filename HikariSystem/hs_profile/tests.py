from django.test import TestCase
from django.test.client import Client
from django.contrib.auth.models import User
from hs_test import HsTest
import simplejson

# Create your tests here.

class SimpleTest(HsTest):

    TEST_DEVICE_ID = 'test_device_id'
     
    def test_set_name(self):
         
        client = Client()
        username, password = self.create_user(client)
        self.login(client, username, password)
        
        response = client.post("/ajax/hs_profile/set_name.json", {"arg":simplejson.dumps({
            "name":"helloword",
        })})
        content = response.content
        result = simplejson.loads(content)
        self.assertEqual(result['success'],True,content)

        response = client.post("/ajax/hs_profile/get_profile.json", {"arg":simplejson.dumps({
        })})
        content = response.content
        result = simplejson.loads(content)
        self.assertEqual(result['success'],True,content)

        self.assertEqual(result['data']['profile']['name'],"helloword")
