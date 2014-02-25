from django.test import TestCase
from django.test.client import Client
import simplejson
from django.contrib.auth.models import User
from hikari import now64
from django.core.management import call_command
from hikari_user.models import HsUser
from hikari_quest.models import HsQuestEntry

# Create your tests here.

class SimpleTest(TestCase):
     
    TEST_DEVICE_MODEL = 'test_device_model'
     
    def test_create_user(self):
        
        client = Client()
        
#        response = client.post(reverse('ajax')+"/hikari/hello.json",{
        response = client.post("/ajax/hikari/user__create_user.json", {"arg":simplejson.dumps({
            "device_model":SimpleTest.TEST_DEVICE_MODEL
        })})
        content = response.content
        result = simplejson.loads(content)
        
        self.assertEqual(result['success'], True)
        self.assertIn("username", result['data'])
        self.assertIn("password", result['data'])
        
        user = User.objects.get(username=result['data']['username'])
        hsuser = HsUser.objects.get(user=user)
        self.assertEqual(hsuser.device_model, SimpleTest.TEST_DEVICE_MODEL)
        self.assertTrue(hsuser.create_at <= now64())
        self.assertTrue(hsuser.create_at >= now64() - 1000)

    def test_login(self):
        
        client = Client()
        
#        response = client.post(reverse('ajax')+"/hikari/hello.json",{
        response = client.post("/ajax/hikari/user__create_user.json", {"arg":simplejson.dumps({
            "device_model":SimpleTest.TEST_DEVICE_MODEL
        })})
        content = response.content
        result = simplejson.loads(content)
        
        self.assertEqual(result['success'], True)
        username = result['data']['username']
        password = result['data']['password']

        response = client.post("/ajax/hikari/user__login.json", {"arg":simplejson.dumps({
            "username":username,
            "password":password,
        })})
        content = response.content
        result = simplejson.loads(content)
        
#         print result
        
        self.assertEqual(result['success'], True)
        self.assertIn('_auth_user_id', client.session)

    def test_quest(self):

        call_command('csv_in')
        
        client = Client()
        
        response = client.post("/ajax/hikari/user__create_user.json", {"arg":simplejson.dumps({
            "device_model":SimpleTest.TEST_DEVICE_MODEL
        })})
        content = response.content
        result = simplejson.loads(content)
        
        self.assertEqual(result['success'], True)
        username = result['data']['username']
        password = result['data']['password']
        
        response = client.post("/ajax/hikari/user__login.json", {"arg":simplejson.dumps({
            "username":username,
            "password":password,
        })})
        content = response.content
#         print content
        result = simplejson.loads(content)

        self.assertEqual(result['success'], True)

        response = client.post("/ajax/hikari/quest__quest_start.json", {"arg":simplejson.dumps({
            "quest_entry_key":HsQuestEntry.objects.all()[0].key,
        })})
        content = response.content
#         print content
        result = simplejson.loads(content)
        
        self.assertEqual(result['success'], True)
        quest_instance_id = result['data']['quest_instance']['id']

        response = client.post("/ajax/hikari/quest__quest_end.json", {"arg":simplejson.dumps({
            "quest_instance_id":quest_instance_id,
            'success': True
        })})
        content = response.content
        result = simplejson.loads(content)

        self.assertEqual(result['success'], True)

    def test_quest_Yi7XmDfH(self):

        call_command('csv_in')
        
        client = Client()
        
        response = client.post("/ajax/hikari/user__create_user.json", {"arg":simplejson.dumps({
            "device_model":SimpleTest.TEST_DEVICE_MODEL
        })})
        content = response.content
        result = simplejson.loads(content)
        
        self.assertEqual(result['success'], True)
        username = result['data']['username']
        password = result['data']['password']
        
        response = client.post("/ajax/hikari/user__login.json", {"arg":simplejson.dumps({
            "username":username,
            "password":password,
        })})
        content = response.content
        result = simplejson.loads(content)

        self.assertEqual(result['success'], True)
        
        quest_key = HsQuestEntry.objects.all()[0].key

        response = client.post("/ajax/hikari/quest__quest_start.json", {"arg":simplejson.dumps({
            "quest_entry_key":quest_key,
        })})
        content = response.content
        result = simplejson.loads(content)
        
        self.assertEqual(result['success'], True)
        quest_instance_id0 = result['data']['quest_instance']['id']

        response = client.post("/ajax/hikari/quest__quest_start.json", {"arg":simplejson.dumps({
            "quest_entry_key":quest_key,
        })})
        content = response.content
        result = simplejson.loads(content)
        
        self.assertEqual(result['success'], True)
        quest_instance_id1 = result['data']['quest_instance']['id']

        response = client.post("/ajax/hikari/quest__quest_end.json", {"arg":simplejson.dumps({
            "quest_instance_id":quest_instance_id0,
            'success': True
        })})
        content = response.content
        result = simplejson.loads(content)
        
#         self.assertEqual(result['success'], True)

        response = client.post("/ajax/hikari/quest__quest_end.json", {"arg":simplejson.dumps({
            "quest_instance_id":quest_instance_id1,
            'success': True
        })})
        content = response.content
        result = simplejson.loads(content)

        self.assertEqual(result['success'], True)

    def test_seqid(self):
        
        client = Client()

        response = client.post("/ajax/hikari/system__get_time.json", {"arg":"{}"})
        content = response.content
        simplejson.loads(content)

        seqid = client.cookies['seqid'].value
#         print "client seqid = "+seqid
        
        response = client.post("/ajax/hikari/system__get_time.json", {"arg":"{}"})
        content = response.content
        result = simplejson.loads(content)
        
        self.assertEqual(result['success'], True)
        t0 = result['data']['time']
        
        client.cookies['seqid'] = seqid
        
        response = client.post("/ajax/hikari/system__get_time.json", {"arg":"{}"})
        content = response.content
        result = simplejson.loads(content)
        
        self.assertEqual(result['success'], True)
        t1 = result['data']['time']

        self.assertEqual(t0, t1)
