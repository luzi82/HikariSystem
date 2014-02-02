# -*- coding: utf-8 -*-
from south.utils import datetime_utils as datetime
from south.db import db
from south.v2 import SchemaMigration
from django.db import models


class Migration(SchemaMigration):

    def forwards(self, orm):
        # Adding model 'HsUserItem'
        db.create_table(u'hs_item_hsuseritem', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('user', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['auth.User'])),
            ('itemtype', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['hs_item.HsItemType'])),
        ))
        db.send_create_signal(u'hs_item', ['HsUserItem'])

        # Adding model 'HsItemValueType'
        db.create_table(u'hs_item_hsitemvaluetype', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('name', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['hs_i18n.HsText'])),
            ('value_max', self.gf('django.db.models.fields.IntegerField')()),
        ))
        db.send_create_signal(u'hs_item', ['HsItemValueType'])

        # Adding model 'HsUserItemValue'
        db.create_table(u'hs_item_hsuseritemvalue', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('useritem', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['hs_item.HsUserItem'])),
            ('itemvaluetype', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['hs_item.HsItemValueType'])),
            ('value', self.gf('django.db.models.fields.IntegerField')()),
        ))
        db.send_create_signal(u'hs_item', ['HsUserItemValue'])

        # Adding model 'HsItemType'
        db.create_table(u'hs_item_hsitemtype', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('itemtypegroup', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['hs_item.HsItemTypeGroup'])),
            ('name', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['hs_i18n.HsText'])),
        ))
        db.send_create_signal(u'hs_item', ['HsItemType'])

        # Adding model 'HsItemTypeGroup'
        db.create_table(u'hs_item_hsitemtypegroup', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('name', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['hs_i18n.HsText'])),
        ))
        db.send_create_signal(u'hs_item', ['HsItemTypeGroup'])

        # Adding model 'HsItemValue'
        db.create_table(u'hs_item_hsitemvalue', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('itemvaluetype', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['hs_item.HsItemValueType'])),
            ('itemtype', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['hs_item.HsItemType'])),
            ('value', self.gf('django.db.models.fields.IntegerField')()),
        ))
        db.send_create_signal(u'hs_item', ['HsItemValue'])


    def backwards(self, orm):
        # Deleting model 'HsUserItem'
        db.delete_table(u'hs_item_hsuseritem')

        # Deleting model 'HsItemValueType'
        db.delete_table(u'hs_item_hsitemvaluetype')

        # Deleting model 'HsUserItemValue'
        db.delete_table(u'hs_item_hsuseritemvalue')

        # Deleting model 'HsItemType'
        db.delete_table(u'hs_item_hsitemtype')

        # Deleting model 'HsItemTypeGroup'
        db.delete_table(u'hs_item_hsitemtypegroup')

        # Deleting model 'HsItemValue'
        db.delete_table(u'hs_item_hsitemvalue')


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
        u'hs_i18n.hstext': {
            'Meta': {'object_name': 'HsText'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'info': ('django.db.models.fields.TextField', [], {}),
            'key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'})
        },
        u'hs_item.hsitemtype': {
            'Meta': {'object_name': 'HsItemType'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'itemtypegroup': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['hs_item.HsItemTypeGroup']"}),
            'name': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['hs_i18n.HsText']"})
        },
        u'hs_item.hsitemtypegroup': {
            'Meta': {'object_name': 'HsItemTypeGroup'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['hs_i18n.HsText']"})
        },
        u'hs_item.hsitemvalue': {
            'Meta': {'object_name': 'HsItemValue'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'itemtype': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['hs_item.HsItemType']"}),
            'itemvaluetype': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['hs_item.HsItemValueType']"}),
            'value': ('django.db.models.fields.IntegerField', [], {})
        },
        u'hs_item.hsitemvaluetype': {
            'Meta': {'object_name': 'HsItemValueType'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['hs_i18n.HsText']"}),
            'value_max': ('django.db.models.fields.IntegerField', [], {})
        },
        u'hs_item.hsuseritem': {
            'Meta': {'object_name': 'HsUserItem'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'itemtype': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['hs_item.HsItemType']"}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['auth.User']"})
        },
        u'hs_item.hsuseritemvalue': {
            'Meta': {'object_name': 'HsUserItemValue'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'itemvaluetype': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['hs_item.HsItemValueType']"}),
            'useritem': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['hs_item.HsUserItem']"}),
            'value': ('django.db.models.fields.IntegerField', [], {})
        }
    }

    complete_apps = ['hs_item']