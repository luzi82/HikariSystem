# -*- coding: utf-8 -*-
from south.utils import datetime_utils as datetime
from south.db import db
from south.v2 import SchemaMigration
from django.db import models


class Migration(SchemaMigration):

    def forwards(self, orm):
        # Adding model 'HsResourceConvert'
        db.create_table(u'hikari_resource_hsresourceconvert', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
        ))
        db.send_create_signal(u'hikari_resource', ['HsResourceConvert'])

        # Adding model 'HsUserResource'
        db.create_table(u'hikari_resource_hsuserresource', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('user', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['auth.User'])),
            ('resource_key', self.gf('django.db.models.fields.CharField')(max_length=64)),
            ('count', self.gf('django.db.models.fields.IntegerField')()),
            ('time', self.gf('django.db.models.fields.BigIntegerField')()),
        ))
        db.send_create_signal(u'hikari_resource', ['HsUserResource'])

        # Adding index on 'HsUserResource', fields ['user', 'resource_key']
        db.create_index(u'hikari_resource_hsuserresource', ['user_id', 'resource_key'])

        # Adding model 'HsResourceChangeHistoryEnable'
        db.create_table(u'hikari_resource_hsresourcechangehistoryenable', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('resource_key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
        ))
        db.send_create_signal(u'hikari_resource', ['HsResourceChangeHistoryEnable'])

        # Adding model 'HsResource'
        db.create_table(u'hikari_resource_hsresource', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('type', self.gf('django.db.models.fields.IntegerField')()),
            ('max', self.gf('django.db.models.fields.BigIntegerField')()),
            ('init_count', self.gf('django.db.models.fields.IntegerField')()),
        ))
        db.send_create_signal(u'hikari_resource', ['HsResource'])

        # Adding model 'HsResourceConvertHistory'
        db.create_table(u'hikari_resource_hsresourceconverthistory', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('user', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['auth.User'])),
            ('time', self.gf('django.db.models.fields.BigIntegerField')(db_index=True)),
            ('resource_convert_key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('count', self.gf('django.db.models.fields.IntegerField')()),
        ))
        db.send_create_signal(u'hikari_resource', ['HsResourceConvertHistory'])

        # Adding index on 'HsResourceConvertHistory', fields ['user', 'time']
        db.create_index(u'hikari_resource_hsresourceconverthistory', ['user_id', 'time'])

        # Adding model 'HsResourceChangeHistory'
        db.create_table(u'hikari_resource_hsresourcechangehistory', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('user', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['auth.User'])),
            ('time', self.gf('django.db.models.fields.BigIntegerField')(db_index=True)),
            ('resource_key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('count', self.gf('django.db.models.fields.IntegerField')()),
            ('change_reason_key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('msg', self.gf('django.db.models.fields.TextField')()),
        ))
        db.send_create_signal(u'hikari_resource', ['HsResourceChangeHistory'])

        # Adding index on 'HsResourceChangeHistory', fields ['user', 'time']
        db.create_index(u'hikari_resource_hsresourcechangehistory', ['user_id', 'time'])

        # Adding index on 'HsResourceChangeHistory', fields ['user', 'resource_key', 'time']
        db.create_index(u'hikari_resource_hsresourcechangehistory', ['user_id', 'resource_key', 'time'])

        # Adding index on 'HsResourceChangeHistory', fields ['user', 'resource_key', 'change_reason_key', 'time']
        db.create_index(u'hikari_resource_hsresourcechangehistory', ['user_id', 'resource_key', 'change_reason_key', 'time'])

        # Adding model 'HsResourceConvertChange'
        db.create_table(u'hikari_resource_hsresourceconvertchange', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('parent_key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('resource_key', self.gf('django.db.models.fields.CharField')(max_length=64)),
            ('change', self.gf('django.db.models.fields.BigIntegerField')()),
        ))
        db.send_create_signal(u'hikari_resource', ['HsResourceConvertChange'])


    def backwards(self, orm):
        # Removing index on 'HsResourceChangeHistory', fields ['user', 'resource_key', 'change_reason_key', 'time']
        db.delete_index(u'hikari_resource_hsresourcechangehistory', ['user_id', 'resource_key', 'change_reason_key', 'time'])

        # Removing index on 'HsResourceChangeHistory', fields ['user', 'resource_key', 'time']
        db.delete_index(u'hikari_resource_hsresourcechangehistory', ['user_id', 'resource_key', 'time'])

        # Removing index on 'HsResourceChangeHistory', fields ['user', 'time']
        db.delete_index(u'hikari_resource_hsresourcechangehistory', ['user_id', 'time'])

        # Removing index on 'HsResourceConvertHistory', fields ['user', 'time']
        db.delete_index(u'hikari_resource_hsresourceconverthistory', ['user_id', 'time'])

        # Removing index on 'HsUserResource', fields ['user', 'resource_key']
        db.delete_index(u'hikari_resource_hsuserresource', ['user_id', 'resource_key'])

        # Deleting model 'HsResourceConvert'
        db.delete_table(u'hikari_resource_hsresourceconvert')

        # Deleting model 'HsUserResource'
        db.delete_table(u'hikari_resource_hsuserresource')

        # Deleting model 'HsResourceChangeHistoryEnable'
        db.delete_table(u'hikari_resource_hsresourcechangehistoryenable')

        # Deleting model 'HsResource'
        db.delete_table(u'hikari_resource_hsresource')

        # Deleting model 'HsResourceConvertHistory'
        db.delete_table(u'hikari_resource_hsresourceconverthistory')

        # Deleting model 'HsResourceChangeHistory'
        db.delete_table(u'hikari_resource_hsresourcechangehistory')

        # Deleting model 'HsResourceConvertChange'
        db.delete_table(u'hikari_resource_hsresourceconvertchange')


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
        u'hikari_resource.hsresource': {
            'Meta': {'object_name': 'HsResource'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'init_count': ('django.db.models.fields.IntegerField', [], {}),
            'key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'}),
            'max': ('django.db.models.fields.BigIntegerField', [], {}),
            'type': ('django.db.models.fields.IntegerField', [], {})
        },
        u'hikari_resource.hsresourcechangehistory': {
            'Meta': {'object_name': 'HsResourceChangeHistory', 'index_together': "[['user', 'time'], ['user', 'resource_key', 'time'], ['user', 'resource_key', 'change_reason_key', 'time']]"},
            'change_reason_key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'}),
            'count': ('django.db.models.fields.IntegerField', [], {}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'msg': ('django.db.models.fields.TextField', [], {}),
            'resource_key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'}),
            'time': ('django.db.models.fields.BigIntegerField', [], {'db_index': 'True'}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['auth.User']"})
        },
        u'hikari_resource.hsresourcechangehistoryenable': {
            'Meta': {'object_name': 'HsResourceChangeHistoryEnable'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'resource_key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'})
        },
        u'hikari_resource.hsresourceconvert': {
            'Meta': {'object_name': 'HsResourceConvert'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'})
        },
        u'hikari_resource.hsresourceconvertchange': {
            'Meta': {'object_name': 'HsResourceConvertChange'},
            'change': ('django.db.models.fields.BigIntegerField', [], {}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'parent_key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'}),
            'resource_key': ('django.db.models.fields.CharField', [], {'max_length': '64'})
        },
        u'hikari_resource.hsresourceconverthistory': {
            'Meta': {'object_name': 'HsResourceConvertHistory', 'index_together': "[['user', 'time']]"},
            'count': ('django.db.models.fields.IntegerField', [], {}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'resource_convert_key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'}),
            'time': ('django.db.models.fields.BigIntegerField', [], {'db_index': 'True'}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['auth.User']"})
        },
        u'hikari_resource.hsuserresource': {
            'Meta': {'object_name': 'HsUserResource', 'index_together': "[['user', 'resource_key']]"},
            'count': ('django.db.models.fields.IntegerField', [], {}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'resource_key': ('django.db.models.fields.CharField', [], {'max_length': '64'}),
            'time': ('django.db.models.fields.BigIntegerField', [], {}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['auth.User']"})
        }
    }

    complete_apps = ['hikari_resource']