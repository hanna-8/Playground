from django.urls import path

from . import views

app_name = 'tracks'
urlpatterns = [
    path('', views.ListView.as_view(), name = 'list'),
    path('<int:pk>/', views.DetailView.as_view(), name = 'details'),
    path('<int:track_id>/edit', views.edit, name = 'edit'),
]