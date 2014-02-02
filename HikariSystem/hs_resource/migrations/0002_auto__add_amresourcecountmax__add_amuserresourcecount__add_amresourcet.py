# -*- coding: utf-8 -*-
from south.utils import datetime_utils as datetime
from south.db import db
from south.v2 import SchemaMigration
from django.db import models


class Migration(SchemaMigration):

    def forwards(self, orm):
        # Adding model 'HsResourceCountMax'
        db.create_table(u'hs_resource_hsresourcecountmax', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('level', self.gf('django.db.models.fields.IntegerField')(db_index=True)),
            ('count_max', self.gf('django.db.models.fields.IntegerField')()),
        ))
        db.send_create_signal(u'hs_resource', ['HsResourceCountMax'])

        # Adding model 'HsUserResourceCount'
        db.create_table(u'hs_resource_hsuserresourcecount', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('user', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['auth.User'])),
            ('type', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['hs_resource.HsResourceType'])),
            ('count', self.gf('django.db.models.fields.IntegerField')(db_index=True)),
            ('update', self.gf('django.db.models.fields.BigIntegerField')()),
        ))
        db.send_create_signal(u'hs_resource', ['HsUserResourceCount'])

        # Adding model 'HsResourceType'
        db.create_table(u'hs_resource_hsresourcetype', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('name', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['hs_i18n.HsText'])),
            ('count_max_leveltype', self.gf('django.db.models.fields.related.ForeignKey')(related_name='hsresourcetype_count_max_leveltype', null=True, to=orm['hs_level.HsLevelType'])),
            ('count_increase_leveltype', self.gf('django.db.models.fields.related.ForeignKey')(related_name='hsresourcetype_count_increase_leveltype', null=True, to=orm['hs_level.HsLevelType'])),
        ))
        db.send_create_signal(u'hs_resource', ['HsResourceType'])

        # Adding model 'HsResourceCountIncrease'
        db.create_table(u'hs_resource_hsresourcecountincrease', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('level', self.gf('django.db.models.fields.IntegerField')(db_index=True)),
            ('count_increase', self.gf('django.db.models.fields.IntegerField')()),
        ))
        db.send_create_signal(u'hs_resource', ['HsResourceCountIncrease'])


    def backwards(self, orm):
        # Deleting model 'HsResourceCountMax'
        db.delete_table(u'hs_resource_hsresourcecountmax')

        # Deleting model 'HsUserResourceCount'
        db.delete_table(u'hs_resource_hsuserresourcecount')

        # Deleting model 'HsResourceType'
        db.delete_table(u'hs_resource_hsresourcetype')

        # Deleting model 'HsResourceCountIncrease'
        db.delete_table(u'hs_resource_hsresourcecountincrease')


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
        u'hs_level.hsleveltype': {
            'Meta': {'object_name': 'HsLevelType'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['hs_i18n.HsText']"})
        },
        u'hs_resource.hsresourcecountincrease': {
            'Meta': {'object_name': 'HsResourceCountIncrease'},
            'count_increase': ('django.db.models.fields.IntegerField', [], {}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'level': ('django.db.models.fields.IntegerField', [], {'db_index': 'True'})
        },
        u'hs_resource.hsresourcecountmax': {
            'Meta': {'object_name': 'HsResourceCountMax'},
            'count_max': ('django.db.models.fields.IntegerField', [], {}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'level': ('django.db.models.fields.IntegerField', [], {'db_index': 'True'})
        },
        u'hs_resource.hsresourcetype': {
            'Meta': {'object_name': 'HsResourceType'},
            'count_increase_leveltype': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'hsresourcetype_count_increase_leveltype'", 'null': 'True', 'to': u"orm['hs_level.HsLevelType']"}),
            'count_max_leveltype': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'hsresourcetype_count_max_leveltype'", 'null': 'True', 'to': u"orm['hs_level.HsLevelType']"}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['hs_i18n.HsText']"})
        },
        u'hs_resource.hsuserresourcecount': {
            'Meta': {'object_name': 'HsUserResourceCount'},
            'count': ('django.db.models.fields.IntegerField', [], {'db_index': 'True'}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'type': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['hs_resource.HsResourceType']"}),
            'update': ('django.db.models.fields.BigIntegerField', [], {}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['auth.User']"})
        }
    }

    complete_apps = ['hs_resource']