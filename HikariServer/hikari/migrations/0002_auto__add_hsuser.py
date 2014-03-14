# -*- coding: utf-8 -*-
from south.utils import datetime_utils as datetime
from south.db import db
from south.v2 import SchemaMigration
from django.db import models


class Migration(SchemaMigration):

    def forwards(self, orm):
        # Adding model 'HsItemPack'
        db.create_table(u'hikari_hsitempack', (
            (u'id', self.gf('django.db.models.fields.AutoField')(primary_key=True)),
            ('redeem_done', self.gf('django.db.models.fields.BooleanField')()),
        ))
        db.send_create_signal(u'hikari', ['HsItemPack'])


    def backwards(self, orm):
        # Deleting model 'HsItemPack'
        db.delete_table(u'hikari_hsitempack')


    models = {
        u'hikari.hsitempack': {
            'Meta': {'object_name': 'HsItemPack'},
            u'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'redeem_done': ('django.db.models.fields.BooleanField', [], {})
        }
    }

    complete_apps = ['hikari']