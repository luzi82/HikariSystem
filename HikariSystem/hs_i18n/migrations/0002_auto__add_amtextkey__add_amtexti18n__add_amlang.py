# -*- coding: utf-8 -*-
from south.utils import datetime_utils as datetime
from south.db import db
from south.v2 import SchemaMigration
from django.db import models


class Migration(SchemaMigration):

    def forwards(self, orm):
        # Adding model 'AmText'
        db.create_table(u'hs_i18n_amtext', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('info', self.gf('django.db.models.fields.TextField')()),
        ))
        db.send_create_signal(u'hs_i18n', ['AmText'])

        # Adding model 'AmTextI18n'
        db.create_table(u'hs_i18n_amtexti18n', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('lang', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['hs_i18n.AmLang'])),
            ('text', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['hs_i18n.AmText'])),
            ('i18n', self.gf('django.db.models.fields.TextField')()),
        ))
        db.send_create_signal(u'hs_i18n', ['AmTextI18n'])

        # Adding model 'AmLang'
        db.create_table(u'hs_i18n_amlang', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('name', self.gf('django.db.models.fields.CharField')(max_length=16, db_index=True)),
        ))
        db.send_create_signal(u'hs_i18n', ['AmLang'])


    def backwards(self, orm):
        # Deleting model 'AmText'
        db.delete_table(u'hs_i18n_amtext')

        # Deleting model 'AmTextI18n'
        db.delete_table(u'hs_i18n_amtexti18n')

        # Deleting model 'AmLang'
        db.delete_table(u'hs_i18n_amlang')


    models = {
        u'hs_i18n.amlang': {
            'Meta': {'object_name': 'AmLang'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '16', 'db_index': 'True'})
        },
        u'hs_i18n.amtext': {
            'Meta': {'object_name': 'AmText'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'info': ('django.db.models.fields.TextField', [], {}),
            'key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'})
        },
        u'hs_i18n.amtexti18n': {
            'Meta': {'object_name': 'AmTextI18n'},
            'i18n': ('django.db.models.fields.TextField', [], {}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'lang': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['hs_i18n.AmLang']"}),
            'text': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['hs_i18n.AmText']"})
        }
    }

    complete_apps = ['hs_i18n']