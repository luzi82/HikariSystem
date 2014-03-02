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
    
    def test_seqid(self):
        
        client = Client()

        response = client.post("/ajax/hikari/get_time.json", {"arg":"{}"})
        content = response.content
        simplejson.loads(content)

        seqid = client.cookies['seqid'].value
#         print "client seqid = "+seqid
        
        response = client.post("/ajax/hikari/get_time.json", {"arg":"{}"})
        content = response.content
        result = simplejson.loads(content)
        
        self.assertEqual(result['success'], True)
        t0 = result['data']['time']
        
        client.cookies['seqid'] = seqid
        
        response = client.post("/ajax/hikari/get_time.json", {"arg":"{}"})
        content = response.content
        result = simplejson.loads(content)
        
        self.assertEqual(result['success'], True)
        t1 = result['data']['time']

        self.assertEqual(t0, t1)
