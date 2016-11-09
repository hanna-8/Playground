### Change git bash starting directory:
In file c:\Program Files\Git\etc\bash.bashrc  
add line `cd /d/MyCode;` 
*Side note*: tried to modify the .bashrc file in the ~ directory with no success :-?? .

### Change git bash home dir
In file  c:\Program Files\Git\etc\profile.d\env  
add line `export HOME="$USERPROFILE"`  
(the new git bash will be the windows user home dir)

