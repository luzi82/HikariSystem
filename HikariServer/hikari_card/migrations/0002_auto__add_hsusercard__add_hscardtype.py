# -*- coding: utf-8 -*-
from south.utils import datetime_utils as datetime
from south.db import db
from south.v2 import SchemaMigration
from django.db import models


class Migration(SchemaMigration):

    def forwards(self, orm):
        # Adding model 'HsCardValue'
        db.create_table(u'hikari_card_hscardvalue', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('card_type_key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('card_value_type_key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('value', self.gf('django.db.models.fields.IntegerField')()),
        ))
        db.send_create_signal(u'hikari_card', ['HsCardValue'])

        # Adding model 'HsUserDeskCard'
        db.create_table(u'hikari_card_hsuserdeskcard', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('user', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['auth.User'])),
            ('desk_type_key', self.gf('django.db.models.fields.CharField')(max_length=64)),
            ('desk_id', self.gf('django.db.models.fields.IntegerField')()),
            ('desk_pos', self.gf('django.db.models.fields.IntegerField')()),
            ('card', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['hikari_card.HsUserCard'])),
        ))
        db.send_create_signal(u'hikari_card', ['HsUserDeskCard'])

        # Adding model 'HsInitUserDeskCard'
        db.create_table(u'hikari_card_hsinituserdeskcard', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('desk_type_key', self.gf('django.db.models.fields.CharField')(max_length=64)),
            ('desk_id', self.gf('django.db.models.fields.IntegerField')()),
            ('desk_pos', self.gf('django.db.models.fields.IntegerField')()),
            ('init_user_card_key', self.gf('django.db.models.fields.CharField')(max_length=64)),
        ))
        db.send_create_signal(u'hikari_card', ['HsInitUserDeskCard'])

        # Adding model 'HsCardType'
        db.create_table(u'hikari_card_hscardtype', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
        ))
        db.send_create_signal(u'hikari_card', ['HsCardType'])

        # Adding model 'HsDeskType'
        db.create_table(u'hikari_card_hsdesktype', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('card_tag_type_key', self.gf('django.db.models.fields.CharField')(max_length=64)),
            ('desk_count', self.gf('django.db.models.fields.IntegerField')()),
            ('card_list_length', self.gf('django.db.models.fields.IntegerField')()),
        ))
        db.send_create_signal(u'hikari_card', ['HsDeskType'])

        # Adding model 'HsInitUserCard'
        db.create_table(u'hikari_card_hsinitusercard', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('card_type_key', self.gf('django.db.models.fields.CharField')(max_length=64)),
        ))
        db.send_create_signal(u'hikari_card', ['HsInitUserCard'])

        # Adding model 'HsUserCard'
        db.create_table(u'hikari_card_hsusercard', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('user', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['auth.User'])),
            ('card_type_key', self.gf('django.db.models.fields.CharField')(max_length=64)),
        ))
        db.send_create_signal(u'hikari_card', ['HsUserCard'])

        # Adding model 'HsCardTagType'
        db.create_table(u'hikari_card_hscardtagtype', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
        ))
        db.send_create_signal(u'hikari_card', ['HsCardTagType'])

        # Adding model 'HsCardValueType'
        db.create_table(u'hikari_card_hscardvaluetype', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('card_tag_type_key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
        ))
        db.send_create_signal(u'hikari_card', ['HsCardValueType'])

        # Adding model 'HsCardTag'
        db.create_table(u'hikari_card_hscardtag', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('card_tag_type_key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('card_type_key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
        ))
        db.send_create_signal(u'hikari_card', ['HsCardTag'])


    def backwards(self, orm):
        # Deleting model 'HsCardValue'
        db.delete_table(u'hikari_card_hscardvalue')

        # Deleting model 'HsUserDeskCard'
        db.delete_table(u'hikari_card_hsuserdeskcard')

        # Deleting model 'HsInitUserDeskCard'
        db.delete_table(u'hikari_card_hsinituserdeskcard')

        # Deleting model 'HsCardType'
        db.delete_table(u'hikari_card_hscardtype')

        # Deleting model 'HsDeskType'
        db.delete_table(u'hikari_card_hsdesktype')

        # Deleting model 'HsInitUserCard'
        db.delete_table(u'hikari_card_hsinitusercard')

        # Deleting model 'HsUserCard'
        db.delete_table(u'hikari_card_hsusercard')

        # Deleting model 'HsCardTagType'
        db.delete_table(u'hikari_card_hscardtagtype')

        # Deleting model 'HsCardValueType'
        db.delete_table(u'hikari_card_hscardvaluetype')

        # Deleting model 'HsCardTag'
        db.delete_table(u'hikari_card_hscardtag')


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
        u'hikari_card.hscardtag': {
            'Meta': {'object_name': 'HsCardTag'},
            'card_tag_type_key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'}),
            'card_type_key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'})
        },
        u'hikari_card.hscardtagtype': {
            'Meta': {'object_name': 'HsCardTagType'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'})
        },
        u'hikari_card.hscardtype': {
            'Meta': {'object_name': 'HsCardType'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'})
        },
        u'hikari_card.hscardvalue': {
            'Meta': {'object_name': 'HsCardValue'},
            'card_type_key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'}),
            'card_value_type_key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'value': ('django.db.models.fields.IntegerField', [], {})
        },
        u'hikari_card.hscardvaluetype': {
            'Meta': {'object_name': 'HsCardValueType'},
            'card_tag_type_key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'})
        },
        u'hikari_card.hsdesktype': {
            'Meta': {'object_name': 'HsDeskType'},
            'card_list_length': ('django.db.models.fields.IntegerField', [], {}),
            'card_tag_type_key': ('django.db.models.fields.CharField', [], {'max_length': '64'}),
            'desk_count': ('django.db.models.fields.IntegerField', [], {}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'})
        },
        u'hikari_card.hsinitusercard': {
            'Meta': {'object_name': 'HsInitUserCard'},
            'card_type_key': ('django.db.models.fields.CharField', [], {'max_length': '64'}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'})
        },
        u'hikari_card.hsinituserdeskcard': {
            'Meta': {'object_name': 'HsInitUserDeskCard'},
            'desk_id': ('django.db.models.fields.IntegerField', [], {}),
            'desk_pos': ('django.db.models.fields.IntegerField', [], {}),
            'desk_type_key': ('django.db.models.fields.CharField', [], {'max_length': '64'}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'init_user_card_key': ('django.db.models.fields.CharField', [], {'max_length': '64'})
        },
        u'hikari_card.hsusercard': {
            'Meta': {'object_name': 'HsUserCard'},
            'card_type_key': ('django.db.models.fields.CharField', [], {'max_length': '64'}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['auth.User']"})
        },
        u'hikari_card.hsuserdeskcard': {
            'Meta': {'object_name': 'HsUserDeskCard'},
            'card': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['hikari_card.HsUserCard']"}),
            'desk_id': ('django.db.models.fields.IntegerField', [], {}),
            'desk_pos': ('django.db.models.fields.IntegerField', [], {}),
            'desk_type_key': ('django.db.models.fields.CharField', [], {'max_length': '64'}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['auth.User']"})
        }
    }

    complete_apps = ['hikari_card']