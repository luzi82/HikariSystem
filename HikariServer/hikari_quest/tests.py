from django.test import TestCase
from django.test.client import Client
import simplejson
from django.contrib.auth.models import User
from django.core.management import call_command
from hikari_user.models import HsUser
from hikari_quest.models import HsQuestEntry

# Create your tests here.

class SimpleTest(TestCase):
     
    TEST_DEVICE_MODEL = 'test_device_model'
    
    def test_quest(self):

        call_command('csv_in')
        
        client = Client()
        
        response = client.post("/ajax/hikari_user/create_user.json", {"arg":simplejson.dumps({
            "device_model":SimpleTest.TEST_DEVICE_MODEL
        })})
        content = response.content
        result = simplejson.loads(content)
        
        self.assertEqual(result['success'], True)
        username = result['data']['username']
        password = result['data']['password']
        
        response = client.post("/ajax/hikari_user/login.json", {"arg":simplejson.dumps({
            "username":username,
            "password":password,
        })})
        content = response.content
#         print content
        result = simplejson.loads(content)

        self.assertEqual(result['success'], True)

        response = client.post("/ajax/hikari_quest/quest_start.json", {"arg":simplejson.dumps({
            "quest_entry_key":HsQuestEntry.objects.all()[0].key,
        })})
        content = response.content
#         print content
        result = simplejson.loads(content)
        
        self.assertEqual(result['success'], True)
        quest_instance_id = result['data']['quest_instance']['id']

        response = client.post("/ajax/hikari_quest/quest_end.json", {"arg":simplejson.dumps({
            "quest_instance_id":quest_instance_id,
            'success': True
        })})
        content = response.content
        result = simplejson.loads(content)

        self.assertEqual(result['success'], True)

    def test_quest_Yi7XmDfH(self):

        call_command('csv_in')
        
        client = Client()
        
        response = client.post("/ajax/hikari_user/create_user.json", {"arg":simplejson.dumps({
            "device_model":SimpleTest.TEST_DEVICE_MODEL
        })})
        content = response.content
        result = simplejson.loads(content)
        
        self.assertEqual(result['success'], True)
        username = result['data']['username']
        password = result['data']['password']
        
        response = client.post("/ajax/hikari_user/login.json", {"arg":simplejson.dumps({
            "username":username,
            "password":password,
        })})
        content = response.content
        result = simplejson.loads(content)

        self.assertEqual(result['success'], True)
        
        quest_key = HsQuestEntry.objects.all()[0].key

        response = client.post("/ajax/hikari_quest/quest_start.json", {"arg":simplejson.dumps({
            "quest_entry_key":quest_key,
        })})
        content = response.content
        result = simplejson.loads(content)
        
        self.assertEqual(result['success'], True)
        quest_instance_id0 = result['data']['quest_instance']['id']

        response = client.post("/ajax/hikari_quest/quest_start.json", {"arg":simplejson.dumps({
            "quest_entry_key":quest_key,
        })})
        content = response.content
        result = simplejson.loads(content)
        
        self.assertEqual(result['success'], True)
        quest_instance_id1 = result['data']['quest_instance']['id']

        response = client.post("/ajax/hikari_quest/quest_end.json", {"arg":simplejson.dumps({
            "quest_instance_id":quest_instance_id0,
            'success': True
        })})
        content = response.content
        result = simplejson.loads(content)
        
#         self.assertEqual(result['success'], True)

        response = client.post("/ajax/hikari_quest/quest_end.json", {"arg":simplejson.dumps({
            "quest_instance_id":quest_instance_id1,
            'success': True
        })})
        content = response.content
        result = simplejson.loads(content)

        self.assertEqual(result['success'], True)
