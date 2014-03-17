# -*- coding: utf-8 -*-
from south.utils import datetime_utils as datetime
from south.db import db
from south.v2 import SchemaMigration
from django.db import models


class Migration(SchemaMigration):

    def forwards(self, orm):
        # Adding model 'HsValue'
        db.create_table(u'hikari_value_hsvalue', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('type', self.gf('django.db.models.fields.IntegerField')()),
            ('max', self.gf('django.db.models.fields.BigIntegerField')()),
            ('init_count', self.gf('django.db.models.fields.IntegerField')()),
            ('ref_value_key', self.gf('django.db.models.fields.CharField')(max_length=64, null=True)),
        ))
        db.send_create_signal(u'hikari_value', ['HsValue'])

        # Adding model 'HsValueConvert'
        db.create_table(u'hikari_value_hsvalueconvert', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
        ))
        db.send_create_signal(u'hikari_value', ['HsValueConvert'])

        # Adding model 'HsValueConvertHistory'
        db.create_table(u'hikari_value_hsvalueconverthistory', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('user', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['auth.User'])),
            ('time', self.gf('django.db.models.fields.BigIntegerField')(db_index=True)),
            ('value_convert_key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('count', self.gf('django.db.models.fields.IntegerField')()),
        ))
        db.send_create_signal(u'hikari_value', ['HsValueConvertHistory'])

        # Adding index on 'HsValueConvertHistory', fields ['user', 'time']
        db.create_index(u'hikari_value_hsvalueconverthistory', ['user_id', 'time'])

        # Adding model 'HsValueLevelStair'
        db.create_table(u'hikari_value_hsvaluelevelstair', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('value_key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('from_value_min', self.gf('django.db.models.fields.IntegerField')()),
        ))
        db.send_create_signal(u'hikari_value', ['HsValueLevelStair'])

        # Adding index on 'HsValueLevelStair', fields ['value_key', 'from_value_min']
        db.create_index(u'hikari_value_hsvaluelevelstair', ['value_key', 'from_value_min'])

        # Adding model 'HsValueConvertChange'
        db.create_table(u'hikari_value_hsvalueconvertchange', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('parent_key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('value_key', self.gf('django.db.models.fields.CharField')(max_length=64)),
            ('change', self.gf('django.db.models.fields.BigIntegerField')()),
        ))
        db.send_create_signal(u'hikari_value', ['HsValueConvertChange'])

        # Adding model 'HsValueChangeHistory'
        db.create_table(u'hikari_value_hsvaluechangehistory', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('user', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['auth.User'])),
            ('time', self.gf('django.db.models.fields.BigIntegerField')(db_index=True)),
            ('value_key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('value', self.gf('django.db.models.fields.IntegerField')()),
            ('change_reason_key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('change_reason_msg', self.gf('django.db.models.fields.TextField')()),
        ))
        db.send_create_signal(u'hikari_value', ['HsValueChangeHistory'])

        # Adding index on 'HsValueChangeHistory', fields ['user', 'time']
        db.create_index(u'hikari_value_hsvaluechangehistory', ['user_id', 'time'])

        # Adding index on 'HsValueChangeHistory', fields ['user', 'value_key', 'time']
        db.create_index(u'hikari_value_hsvaluechangehistory', ['user_id', 'value_key', 'time'])

        # Adding index on 'HsValueChangeHistory', fields ['user', 'value_key', 'change_reason_key', 'time']
        db.create_index(u'hikari_value_hsvaluechangehistory', ['user_id', 'value_key', 'change_reason_key', 'time'])

        # Adding model 'HsUserValue'
        db.create_table(u'hikari_value_hsuservalue', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('user', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['auth.User'])),
            ('value_key', self.gf('django.db.models.fields.CharField')(max_length=64)),
            ('count', self.gf('django.db.models.fields.IntegerField')()),
            ('time', self.gf('django.db.models.fields.BigIntegerField')()),
        ))
        db.send_create_signal(u'hikari_value', ['HsUserValue'])

        # Adding index on 'HsUserValue', fields ['user', 'value_key']
        db.create_index(u'hikari_value_hsuservalue', ['user_id', 'value_key'])

        # Adding model 'HsValueItem'
        db.create_table(u'hikari_value_hsvalueitem', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('item_pack', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['hikari.HsItemPack'])),
            ('value_key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('value', self.gf('django.db.models.fields.IntegerField')()),
            ('change_reason_key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('change_reason_msg', self.gf('django.db.models.fields.TextField')()),
        ))
        db.send_create_signal(u'hikari_value', ['HsValueItem'])

        # Adding model 'HsValueChangeHistoryEnable'
        db.create_table(u'hikari_value_hsvaluechangehistoryenable', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('value_key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
        ))
        db.send_create_signal(u'hikari_value', ['HsValueChangeHistoryEnable'])


    def backwards(self, orm):
        # Removing index on 'HsUserValue', fields ['user', 'value_key']
        db.delete_index(u'hikari_value_hsuservalue', ['user_id', 'value_key'])

        # Removing index on 'HsValueChangeHistory', fields ['user', 'value_key', 'change_reason_key', 'time']
        db.delete_index(u'hikari_value_hsvaluechangehistory', ['user_id', 'value_key', 'change_reason_key', 'time'])

        # Removing index on 'HsValueChangeHistory', fields ['user', 'value_key', 'time']
        db.delete_index(u'hikari_value_hsvaluechangehistory', ['user_id', 'value_key', 'time'])

        # Removing index on 'HsValueChangeHistory', fields ['user', 'time']
        db.delete_index(u'hikari_value_hsvaluechangehistory', ['user_id', 'time'])

        # Removing index on 'HsValueLevelStair', fields ['value_key', 'from_value_min']
        db.delete_index(u'hikari_value_hsvaluelevelstair', ['value_key', 'from_value_min'])

        # Removing index on 'HsValueConvertHistory', fields ['user', 'time']
        db.delete_index(u'hikari_value_hsvalueconverthistory', ['user_id', 'time'])

        # Deleting model 'HsValue'
        db.delete_table(u'hikari_value_hsvalue')

        # Deleting model 'HsValueConvert'
        db.delete_table(u'hikari_value_hsvalueconvert')

        # Deleting model 'HsValueConvertHistory'
        db.delete_table(u'hikari_value_hsvalueconverthistory')

        # Deleting model 'HsValueLevelStair'
        db.delete_table(u'hikari_value_hsvaluelevelstair')

        # Deleting model 'HsValueConvertChange'
        db.delete_table(u'hikari_value_hsvalueconvertchange')

        # Deleting model 'HsValueChangeHistory'
        db.delete_table(u'hikari_value_hsvaluechangehistory')

        # Deleting model 'HsUserValue'
        db.delete_table(u'hikari_value_hsuservalue')

        # Deleting model 'HsValueItem'
        db.delete_table(u'hikari_value_hsvalueitem')

        # Deleting model 'HsValueChangeHistoryEnable'
        db.delete_table(u'hikari_value_hsvaluechangehistoryenable')


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
        u'hikari.hsitempack': {
            'Meta': {'object_name': 'HsItemPack'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'redeem_done': ('django.db.models.fields.BooleanField', [], {})
        },
        u'hikari_value.hsuservalue': {
            'Meta': {'object_name': 'HsUserValue', 'index_together': "[['user', 'value_key']]"},
            'count': ('django.db.models.fields.IntegerField', [], {}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'time': ('django.db.models.fields.BigIntegerField', [], {}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['auth.User']"}),
            'value_key': ('django.db.models.fields.CharField', [], {'max_length': '64'})
        },
        u'hikari_value.hsvalue': {
            'Meta': {'object_name': 'HsValue'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'init_count': ('django.db.models.fields.IntegerField', [], {}),
            'key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'}),
            'max': ('django.db.models.fields.BigIntegerField', [], {}),
            'ref_value_key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'null': 'True'}),
            'type': ('django.db.models.fields.IntegerField', [], {})
        },
        u'hikari_value.hsvaluechangehistory': {
            'Meta': {'object_name': 'HsValueChangeHistory', 'index_together': "[['user', 'time'], ['user', 'value_key', 'time'], ['user', 'value_key', 'change_reason_key', 'time']]"},
            'change_reason_key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'}),
            'change_reason_msg': ('django.db.models.fields.TextField', [], {}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'time': ('django.db.models.fields.BigIntegerField', [], {'db_index': 'True'}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['auth.User']"}),
            'value': ('django.db.models.fields.IntegerField', [], {}),
            'value_key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'})
        },
        u'hikari_value.hsvaluechangehistoryenable': {
            'Meta': {'object_name': 'HsValueChangeHistoryEnable'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'value_key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'})
        },
        u'hikari_value.hsvalueconvert': {
            'Meta': {'object_name': 'HsValueConvert'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'})
        },
        u'hikari_value.hsvalueconvertchange': {
            'Meta': {'object_name': 'HsValueConvertChange'},
            'change': ('django.db.models.fields.BigIntegerField', [], {}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'parent_key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'}),
            'value_key': ('django.db.models.fields.CharField', [], {'max_length': '64'})
        },
        u'hikari_value.hsvalueconverthistory': {
            'Meta': {'object_name': 'HsValueConvertHistory', 'index_together': "[['user', 'time']]"},
            'count': ('django.db.models.fields.IntegerField', [], {}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'time': ('django.db.models.fields.BigIntegerField', [], {'db_index': 'True'}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['auth.User']"}),
            'value_convert_key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'})
        },
        u'hikari_value.hsvalueitem': {
            'Meta': {'object_name': 'HsValueItem'},
            'change_reason_key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'}),
            'change_reason_msg': ('django.db.models.fields.TextField', [], {}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'item_pack': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['hikari.HsItemPack']"}),
            'value': ('django.db.models.fields.IntegerField', [], {}),
            'value_key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'})
        },
        u'hikari_value.hsvaluelevelstair': {
            'Meta': {'object_name': 'HsValueLevelStair', 'index_together': "[['value_key', 'from_value_min']]"},
            'from_value_min': ('django.db.models.fields.IntegerField', [], {}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'value_key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'})
        }
    }

    complete_apps = ['hikari_value']