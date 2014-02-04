from django.test import TestCase
import simplejson


class HsTest(TestCase):
    
    TEST_DEVICE_ID = 'test_device_id'
    
    def create_user(self,client):
        
        response = client.post("/ajax/hs_user/create_user.json",{"arg":simplejson.dumps({
            "device_id":HsTest.TEST_DEVICE_ID
        })})
        content=response.content
        result = simplejson.loads(content)

        self.assertEqual(result['success'],True)
        username = result['data']['username']
        password = result['data']['password']
        
        return (username,password)

    def login(self,client,username,password):

        response = client.post("/ajax/hs_user/login.json",{"arg":simplejson.dumps({
            "username":username,
            "password":password,
        })})
        content=response.content
        result = simplejson.loads(content)

        self.assertEqual(result['success'],True)
        self.assertIn('_auth_user_id', client.session)
