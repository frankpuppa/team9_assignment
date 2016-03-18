# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Class',
            fields=[
                ('id', models.AutoField(verbose_name='ID', auto_created=True, serialize=False, primary_key=True)),
                ('qrCode', models.IntegerField()),
                ('week', models.IntegerField(null=True)),
                ('class_type', models.CharField(max_length=256)),
                ('start_time', models.DateTimeField()),
                ('room_id', models.CharField(max_length=10)),
                ('end_time', models.DateTimeField()),
                ('building', models.CharField(max_length=20)),
            ],
        ),
        migrations.CreateModel(
            name='Module',
            fields=[
                ('moduleid', models.IntegerField(serialize=False, primary_key=True)),
                ('module_code', models.CharField(max_length=20)),
                ('module_title', models.CharField(max_length=50)),
            ],
        ),
        migrations.CreateModel(
            name='Staff',
            fields=[
                ('staffid', models.IntegerField(serialize=False, primary_key=True)),
                ('email', models.CharField(max_length=60)),
                ('first_name', models.CharField(max_length=30)),
                ('last_name', models.CharField(max_length=30)),
                ('password', models.CharField(max_length=30)),
                ('hash', models.CharField(null=True, max_length=128)),
            ],
        ),
        migrations.CreateModel(
            name='Student',
            fields=[
                ('email', models.CharField(max_length=60)),
                ('matric_number', models.IntegerField(serialize=False, primary_key=True)),
                ('first_name', models.CharField(max_length=30)),
                ('last_name', models.CharField(max_length=30)),
                ('password', models.CharField(max_length=30)),
                ('hash', models.CharField(null=True, max_length=128)),
            ],
        ),
        migrations.AddField(
            model_name='module',
            name='coordinators',
            field=models.ManyToManyField(related_name='modules', to='android.Staff'),
        ),
        migrations.AddField(
            model_name='module',
            name='students_enrolled',
            field=models.ManyToManyField(to='android.Student'),
        ),
        migrations.AddField(
            model_name='class',
            name='class_register',
            field=models.ManyToManyField(to='android.Student'),
        ),
        migrations.AddField(
            model_name='class',
            name='module',
            field=models.ForeignKey(related_name='classes', to='android.Module'),
        ),
    ]
