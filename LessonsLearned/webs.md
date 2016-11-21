###Web Servers

Tutorials:
  * [Raw (command line)](https://www.ntu.edu.sg/home/ehchua/programming/howto/Tomcat_HowTo.html)
  * [IntelliJ - Apache](https://www.jetbrains.com/help/idea/2016.2/creating-a-local-server-configuration.html)
  * [IntelliJ - Tomcat](https://www.jetbrains.com/help/idea/2016.2/creating-and-running-your-first-web-application.html)

Alternatives:
  * [Apache HTTP Server](https://httpd.apache.org/): Secure, efficient & extensible server that provides HTTP services.  
  **But** not at all friendly with Java dynamic content: need to wrap Java code into (Perl) scripts in order to add them to the Apache server:

  > "What apache is used for is for either a) providing static content (images, sounds, etc.) and b) load balancing, since it is faster than Tomcat. But this is done in coordination with the Tomcat that serves the java-generated content."  

  (from [StackOverflow](http://stackoverflow.com/questions/17034862/how-can-i-run-a-java-app-on-apache-2-2-without-tomcat))

  * [Apache TomCat](https://tomcat.apache.org/tomcat-3.2-doc/tomcat-apache-howto.html): servlets container that delivers dynamic content as JSP pages.  
  = Catalina (servlet container) +   
  Coyote (webserver-ish container that sends requests to Catalina) +  
  Jasper (JSP engine, web pages are dynamically delivered through it)  

  * [nginx](https://en.wikipedia.org/wiki/Nginx): event-driven architecture, one worker process can handle *a lot* of HTTP connections simoultaneously (while Apache => one process/thread per connection). => **scalable, lightweight, performant**, BUT more sophisticated architecture => not simple to add modules (?). (see [this source](https://www.nginx.com/blog/nginx-vs-apache-our-view/))  
  One more motivation: continuously increasing in number of users, whilst Apache keeps decreasing, according to [Netcraft statistics](https://news.netcraft.com/archives/2016/10/21/october-2016-web-server-survey.html).
