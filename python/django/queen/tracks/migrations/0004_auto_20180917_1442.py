# Generated by Django 2.1.1 on 2018-09-17 11:42

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('tracks', '0003_track_genre'),
    ]

    operations = [
        migrations.AlterField(
            model_name='track',
            name='length',
            field=models.FloatField(default=0),
        ),
    ]
