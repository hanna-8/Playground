import sys

print ("Python Fuuuuum!")

if len(sys.argv) > 1:
  for word in sys.argv[1].split():
    if word == word [::-1]:
      print (word)
else:
  print ("Uhm? No argument? :(")
