... Install Apache HTTP Server:

[Doc](https://docs.moodle.org/29/en/Manual_install_on_Windows_7_with_Apache_and_MySQL#Step_3:_Install_PHP)

  * Go to http://www.apachelounge.com/download/. Scroll down the page until you find the download for the 'Apache 2.4 win32 binaries' and download. You need to be careful that the module dll in PHP matches the version of Apache you install. Apache won't load otherwise.
  * Unzip the file into C:\. You should end up with a directory 'Apache24' (or whatever the latest version is).
  * Find Start > All programs > Accessories > Command Prompt...... BUT, right click, and select 'Run as administrator'.
  * Enter the following commands
   cd \Apache24\bin
   httpd -k install
   httpd -k start
...you may well get a warning about the server name. Don't worry about it. Don't close this window, you will need it again in a minute.

To test it worked type 'http://localhost' into your browser. You should get a screen up to the effect that Apache is installed and working.

---> Troubleshooting:
Cmd:  
`C:\Apache24\bin>httpd.exe -k start`  
`(OS 10048)Only one usage of each socket address (protocol/network address/port)  
is normally permitted.  : AH00072: make_sock: could not bind to address 0.0.0.0:80`  
`AH00451: no listening sockets available, shutting down`  
`AH00015: Unable to open logs`  

Solution: 
`C:\Apache24\bin>netstat -ano`  
Find process that uses 0.0.0.0:80.  
Kill it.  
Retry.  
