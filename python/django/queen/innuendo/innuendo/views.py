from django.http import HttpResponse

def home(request):
    return HttpResponse("Home, I guess ¯\_(ツ)_/¯")
