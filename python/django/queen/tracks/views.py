from django.http import HttpResponseRedirect
from django.shortcuts import get_object_or_404, get_list_or_404, render
from django.urls import reverse
from django.views import generic

from .models import Track


class ListView(generic.ListView):
    model = Track
    # all_tracks = get_list_or_404(Track)
    # return render(request, 'all.html', { 'all_tracks': all_tracks } )


class DetailView(generic.DetailView):
    model = Track
    # track = get_object_or_404(Track, pk = track_id)
    # return render(request, 'track.html', { 'track': track } )


def edit(request, track_id):
    track = get_object_or_404(Track, pk = track_id)
    new_len = request.POST['length']
    track.length = new_len
    track.save()
    return HttpResponseRedirect(reverse('tracks:list'))
