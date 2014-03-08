# -*- coding: utf-8 -*-
from south.utils import datetime_utils as datetime
from south.db import db
from south.v2 import SchemaMigration
from django.db import models


class Migration(SchemaMigration):

    def forwards(self, orm):
        # Adding model 'HsL10nLang'
        db.create_table(u'hikari_l10n_hsl10nlang', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('is_default', self.gf('django.db.models.fields.BooleanField')()),
        ))
        db.send_create_signal(u'hikari_l10n', ['HsL10nLang'])

        # Adding model 'HsL10nText'
        db.create_table(u'hikari_l10n_hsl10ntext', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('l10n_lang_key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('text_key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('text', self.gf('django.db.models.fields.TextField')()),
        ))
        db.send_create_signal(u'hikari_l10n', ['HsL10nText'])


    def backwards(self, orm):
        # Deleting model 'HsL10nLang'
        db.delete_table(u'hikari_l10n_hsl10nlang')

        # Deleting model 'HsL10nText'
        db.delete_table(u'hikari_l10n_hsl10ntext')


    models = {
        u'hikari_l10n.hsl10nlang': {
            'Meta': {'object_name': 'HsL10nLang'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'is_default': ('django.db.models.fields.BooleanField', [], {}),
            'key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'})
        },
        u'hikari_l10n.hsl10ntext': {
            'Meta': {'object_name': 'HsL10nText'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'l10n_lang_key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'}),
            'text': ('django.db.models.fields.TextField', [], {}),
            'text_key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'})
        }
    }

    complete_apps = ['hikari_l10n']