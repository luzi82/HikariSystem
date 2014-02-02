# -*- coding: utf-8 -*-
from south.utils import datetime_utils as datetime
from south.db import db
from south.v2 import SchemaMigration
from django.db import models


class Migration(SchemaMigration):

    def forwards(self, orm):
        # Adding model 'HsTextI18n'
        db.create_table(u'hs_i18n_hstexti18n', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('lang', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['hs_i18n.HsLang'])),
            ('text', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['hs_i18n.HsText'])),
            ('i18n', self.gf('django.db.models.fields.TextField')()),
        ))
        db.send_create_signal(u'hs_i18n', ['HsTextI18n'])

        # Adding model 'HsLang'
        db.create_table(u'hs_i18n_hslang', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('name', self.gf('django.db.models.fields.CharField')(max_length=16, db_index=True)),
        ))
        db.send_create_signal(u'hs_i18n', ['HsLang'])

        # Adding model 'HsText'
        db.create_table(u'hs_i18n_hstext', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('info', self.gf('django.db.models.fields.TextField')()),
        ))
        db.send_create_signal(u'hs_i18n', ['HsText'])


    def backwards(self, orm):
        # Deleting model 'HsTextI18n'
        db.delete_table(u'hs_i18n_hstexti18n')

        # Deleting model 'HsLang'
        db.delete_table(u'hs_i18n_hslang')

        # Deleting model 'HsText'
        db.delete_table(u'hs_i18n_hstext')


    models = {
        u'hs_i18n.hslang': {
            'Meta': {'object_name': 'HsLang'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '16', 'db_index': 'True'})
        },
        u'hs_i18n.hstext': {
            'Meta': {'object_name': 'HsText'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'info': ('django.db.models.fields.TextField', [], {}),
            'key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'})
        },
        u'hs_i18n.hstexti18n': {
            'Meta': {'object_name': 'HsTextI18n'},
            'i18n': ('django.db.models.fields.TextField', [], {}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'lang': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['hs_i18n.HsLang']"}),
            'text': ('django.db.models.fields.related.ForeignKey', [], {'to': u"orm['hs_i18n.HsText']"})
        }
    }

    complete_apps = ['hs_i18n']