from django.contrib import admin

from .models import Genre, RockGenre, Track

admin.site.register(Track)
admin.site.register(Genre)