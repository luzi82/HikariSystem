# -*- coding: utf-8 -*-
from south.utils import datetime_utils as datetime
from south.db import db
from south.v2 import SchemaMigration
from django.db import models


class Migration(SchemaMigration):

    def forwards(self, orm):
        # Adding model 'HsQuestEntry'
        db.create_table(u'hikari_quest_hsquestentry', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
        ))
        db.send_create_signal(u'hikari_quest', ['HsQuestEntry'])

        # Adding model 'HsQuestInstance'
        db.create_table(u'hikari_quest_hsquestinstance', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('user', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['auth.User'])),
            ('entry_key', self.gf('django.db.models.fields.CharField')(max_length=64)),
            ('state', self.gf('django.db.models.fields.IntegerField')()),
            ('create_at', self.gf('django.db.models.fields.BigIntegerField')()),
            ('complete_at', self.gf('django.db.models.fields.BigIntegerField')(default=None, null=True)),
        ))
        db.send_create_signal(u'hikari_quest', ['HsQuestInstance'])

        # Adding model 'HsQuestRewardValueChange'
        db.create_table(u'hikari_quest_hsquestrewardvaluechange', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('parent_key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('value_key', self.gf('django.db.models.fields.CharField')(max_length=64)),
            ('change', self.gf('django.db.models.fields.BigIntegerField')()),
        ))
        db.send_create_signal(u'hikari_quest', ['HsQuestRewardValueChange'])

        # Adding model 'HsQuestCostValueChange'
        db.create_table(u'hikari_quest_hsquestcostvaluechange', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('parent_key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('value_key', self.gf('django.db.models.fields.CharField')(max_length=64)),
            ('change', self.gf('django.db.models.fields.BigIntegerField')()),
        ))
        db.send_create_signal(u'hikari_quest', ['HsQuestCostValueChange'])


    def backwards(self, orm):
        # Deleting model 'HsQuestEntry'
        db.delete_table(u'hikari_quest_hsquestentry')

        # Deleting model 'HsQuestInstance'
        db.delete_table(u'hikari_quest_hsquestinstance')

        # Deleting model 'HsQuestRewardValueChange'
        db.delete_table(u'hikari_quest_hsquestrewardvaluechange')

        # Deleting model 'HsQuestCostValueChange'
        db.delete_table(u'hikari_quest_hsquestcostvaluechange')


    models = {
        u'auth.group': {
            'Meta': {'object_name': 'Group'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '80'}),
            'permissions': ('django.db.models.fields.related.ManyToManyField', [], {'to': u"orm['auth.Permission']", 'symmetrical': 'False', 'blank': 'True'})
        },
        u'auth.permission': {
            'Meta': {'ordering': "(u'content_type__app_label', u'content_type__model', u'codename')", 'unique_together': "((u'content_type', u'codename'),)", 'object_name': 'Permission'},
            'codename': ('django.db.models.fields.CharField', [], {'max_length': '100'}),
            'content_type': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['contenttypes.ContentType']"}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '50'})
        },
        u'auth.user': {
            'Meta': {'object_name': 'User'},
            'date_joined': ('django.db.models.fields.DateTimeField', [], {'default': 'datetime.datetime.now'}),
            'email': ('django.db.models.fields.EmailField', [], {'max_length': '75', 'blank': 'True'}),
            'first_name': ('django.db.models.fields.CharField', [], {'max_length': '30', 'blank': 'True'}),
            'groups': ('django.db.models.fields.related.ManyToManyField', [], {'symmetrical': 'False', 'related_name': "u'user_set'", 'blank': 'True', 'to': u"orm['auth.Group']"}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'is_active': ('django.db.models.fields.BooleanField', [], {'default': 'True'}),
            'is_staff': ('django.db.models.fields.BooleanField', [], {'default': 'False'}),
            'is_superuser': ('django.db.models.fields.BooleanField', [], {'default': 'False'}),
            'last_login': ('django.db.models.fields.DateTimeField', [], {'default': 'datetime.datetime.now'}),
            'last_name': ('django.db.models.fields.CharField', [], {'max_length': '30', 'blank': 'True'}),
            'password': ('django.db.models.fields.CharField', [], {'max_length': '128'}),
            'user_permissions': ('django.db.models.fields.related.ManyToManyField', [], {'symmetrical': 'False', 'related_name': "u'user_set'", 'blank': 'True', 'to': u"orm['auth.Permission']"}),
            'username': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '30'})
        },
        u'contenttypes.contenttype': {
            'Meta': {'ordering': "('name',)", 'unique_together': "(('app_label', 'model'),)", 'object_name': 'ContentType', 'db_table': "'django_content_type'"},
            'app_label': ('django.db.models.fields.CharField', [], {'max_length': '100'}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'model': ('django.db.models.fields.CharField', [], {'max_length': '100'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '100'})
        },
        u'hikari_quest.hsquestcostvaluechange': {
            'Meta': {'object_name': 'HsQuestCostValueChange'},
            'change': ('django.db.models.fields.BigIntegerField', [], {}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'parent_key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'}),
            'value_key': ('django.db.models.fields.CharField', [], {'max_length': '64'})
        },
        u'hikari_quest.hsquestentry': {
            'Meta': {'object_name': 'HsQuestEntry'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'})
        },
        u'hikari_quest.hsquestinstance': {
            'Meta': {'object_name': 'HsQuestInstance'},
            'complete_at': ('django.db.models.fields.BigIntegerField', [], {'default': 'None', 'null': 'True'}),
            'create_at': ('django.db.models.fields.BigIntegerField', [], {}),
            'entry_key': ('django.db.models.fields.CharField', [], {'max_length': '64'}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'state': ('django.db.models.fields.IntegerField', [], {}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['auth.User']"})
        },
        u'hikari_quest.hsquestrewardvaluechange': {
            'Meta': {'object_name': 'HsQuestRewardValueChange'},
            'change': ('django.db.models.fields.BigIntegerField', [], {}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'parent_key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'}),
            'value_key': ('django.db.models.fields.CharField', [], {'max_length': '64'})
        }
    }

    complete_apps = ['hikari_quest']