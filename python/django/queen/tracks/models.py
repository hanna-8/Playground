from django.db import models
from enum import Enum

class RockGenre(Enum):
    ballad = "Ballad"
    classic = "Classic Rock"
    pop = "Pop Rock"
    progressive = "Progressive Rock"
    symphonic = "Symphonic Rock"

class Genre(models.Model):
    name = models.CharField(max_length = 128, choices = [(gen.name, gen.value) for gen in RockGenre])

    def __str__(self):
        return self.get_name_display()

class Track(models.Model):
    title = models.CharField(max_length = 256)
    length = models.FloatField(default = 0)
    genre = models.ForeignKey(Genre, on_delete = models.CASCADE, default = 1)

    def __str__(self):
        return "title = " + self.title + "; length = " + str(self.length) + "; genre = " + str(self.genre)
