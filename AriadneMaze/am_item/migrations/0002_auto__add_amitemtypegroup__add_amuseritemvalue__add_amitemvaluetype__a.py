# -*- coding: utf-8 -*-
from south.utils import datetime_utils as datetime
from south.db import db
from south.v2 import SchemaMigration
from django.db import models


class Migration(SchemaMigration):

    def forwards(self, orm):
        # Adding model 'AmItemTypeGroup'
        db.create_table(u'am_item_amitemtypegroup', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('name', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['am_i18n.AmText'])),
        ))
        db.send_create_signal(u'am_item', ['AmItemTypeGroup'])

        # Adding model 'AmUserItemValue'
        db.create_table(u'am_item_amuseritemvalue', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('useritem', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['am_item.AmUserItem'])),
            ('itemvaluetype', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['am_item.AmItemValueType'])),
            ('value', self.gf('django.db.models.fields.IntegerField')()),
        ))
        db.send_create_signal(u'am_item', ['AmUserItemValue'])

        # Adding model 'AmItemValueType'
        db.create_table(u'am_item_amitemvaluetype', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('name', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['am_i18n.AmText'])),
            ('value_max', self.gf('django.db.models.fields.IntegerField')()),
        ))
        db.send_create_signal(u'am_item', ['AmItemValueType'])

        # Adding model 'AmItemValue'
        db.create_table(u'am_item_amitemvalue', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('itemvaluetype', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['am_item.AmItemValueType'])),
            ('value', self.gf('django.db.models.fields.IntegerField')()),
        ))
        db.send_create_signal(u'am_item', ['AmItemValue'])

        # Adding model 'AmUserItem'
        db.create_table(u'am_item_amuseritem', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('user', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['auth.User'])),
            ('itemtype', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['am_item.AmItemType'])),
        ))
        db.send_create_signal(u'am_item', ['AmUserItem'])

        # Adding model 'AmItemType'
        db.create_table(u'am_item_amitemtype', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('itemgroup', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['am_item.AmItemTypeGroup'])),
            ('name', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['am_i18n.AmText'])),
        ))
        db.send_create_signal(u'am_item', ['AmItemType'])


    def backwards(self, orm):
        # Deleting model 'AmItemTypeGroup'
        db.delete_table(u'am_item_amitemtypegroup')

        # Deleting model 'AmUserItemValue'
        db.delete_table(u'am_item_amuseritemvalue')

        # Deleting model 'AmItemValueType'
        db.delete_table(u'am_item_amitemvaluetype')

        # Deleting model 'AmItemValue'
        db.delete_table(u'am_item_amitemvalue')

        # Deleting model 'AmUserItem'
        db.delete_table(u'am_item_amuseritem')

        # Deleting model 'AmItemType'
        db.delete_table(u'am_item_amitemtype')


    models = {
        u'am_i18n.amtext': {
            'Meta': {'object_name': 'AmText'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'info': ('django.db.models.fields.TextField', [], {}),
            'key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'})
        },
        u'am_item.amitemtype': {
            'Meta': {'object_name': 'AmItemType'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'itemgroup': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['am_item.AmItemTypeGroup']"}),
            'name': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['am_i18n.AmText']"})
        },
        u'am_item.amitemtypegroup': {
            'Meta': {'object_name': 'AmItemTypeGroup'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['am_i18n.AmText']"})
        },
        u'am_item.amitemvalue': {
            'Meta': {'object_name': 'AmItemValue'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'itemvaluetype': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['am_item.AmItemValueType']"}),
            'value': ('django.db.models.fields.IntegerField', [], {})
        },
        u'am_item.amitemvaluetype': {
            'Meta': {'object_name': 'AmItemValueType'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['am_i18n.AmText']"}),
            'value_max': ('django.db.models.fields.IntegerField', [], {})
        },
        u'am_item.amuseritem': {
            'Meta': {'object_name': 'AmUserItem'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'itemtype': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['am_item.AmItemType']"}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['auth.User']"})
        },
        u'am_item.amuseritemvalue': {
            'Meta': {'object_name': 'AmUserItemValue'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'itemvaluetype': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['am_item.AmItemValueType']"}),
            'useritem': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['am_item.AmUserItem']"}),
            'value': ('django.db.models.fields.IntegerField', [], {})
        },
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
        }
    }

    complete_apps = ['am_item']