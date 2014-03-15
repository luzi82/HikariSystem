# -*- coding: utf-8 -*-
from south.utils import datetime_utils as datetime
from south.db import db
from south.v2 import SchemaMigration
from django.db import models


class Migration(SchemaMigration):

    def forwards(self, orm):
        # Adding model 'HsGacha'
        db.create_table(u'hikari_gacha_hsgacha', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
        ))
        db.send_create_signal(u'hikari_gacha', ['HsGacha'])

        # Adding model 'HsGachaResult'
        db.create_table(u'hikari_gacha_hsgacharesult', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('gacha_key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('card_type_key', self.gf('django.db.models.fields.CharField')(max_length=64)),
            ('chance', self.gf('django.db.models.fields.IntegerField')()),
        ))
        db.send_create_signal(u'hikari_gacha', ['HsGachaResult'])

        # Adding model 'HsGachaCostValueChange'
        db.create_table(u'hikari_gacha_hsgachacostvaluechange', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('parent_key', self.gf('django.db.models.fields.CharField')(max_length=64, db_index=True)),
            ('value_key', self.gf('django.db.models.fields.CharField')(max_length=64)),
            ('change', self.gf('django.db.models.fields.BigIntegerField')()),
        ))
        db.send_create_signal(u'hikari_gacha', ['HsGachaCostValueChange'])


    def backwards(self, orm):
        # Deleting model 'HsGacha'
        db.delete_table(u'hikari_gacha_hsgacha')

        # Deleting model 'HsGachaResult'
        db.delete_table(u'hikari_gacha_hsgacharesult')

        # Deleting model 'HsGachaCostValueChange'
        db.delete_table(u'hikari_gacha_hsgachacostvaluechange')


    models = {
        u'hikari_gacha.hsgacha': {
            'Meta': {'object_name': 'HsGacha'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'})
        },
        u'hikari_gacha.hsgachacostvaluechange': {
            'Meta': {'object_name': 'HsGachaCostValueChange'},
            'change': ('django.db.models.fields.BigIntegerField', [], {}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'parent_key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'}),
            'value_key': ('django.db.models.fields.CharField', [], {'max_length': '64'})
        },
        u'hikari_gacha.hsgacharesult': {
            'Meta': {'object_name': 'HsGachaResult'},
            'card_type_key': ('django.db.models.fields.CharField', [], {'max_length': '64'}),
            'chance': ('django.db.models.fields.IntegerField', [], {}),
            'gacha_key': ('django.db.models.fields.CharField', [], {'max_length': '64', 'db_index': 'True'}),
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'})
        }
    }

    complete_apps = ['hikari_gacha']