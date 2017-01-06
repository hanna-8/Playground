#Small Hello World projects.
---

###Enter the (**MySQL Server**)[https://www.ntu.edu.sg/home/ehchua/programming/sql/MySQL_HowTo.html]!

Start the server:  
`cd [...]/mysql/bin`  
`mysqld --console`  

Start the client:  
`cd [...]/mysql/bin`  
`mysql -u root -p`  

Default port: 3306.

Some usual SQL commands:  
`show databases;`  
`use database_name;`  
`show tables;`  
`select * from table_name`  


---

###Here's to the (**Tomcat Server**)[https://www.ntu.edu.sg/home/ehchua/programming/howto/Tomcat_HowTo.html]!

Start the server:  
`cd [...]/tomcat/bin`  
`startup.bat`

Stop the server:
`shutdown.bat`

Default port: 9999  
My custom port: 8042

---

###Tomcat directory structure

`ibarcan@EN617085 MINGW64 /d/Programs/tomcat/webapps/hike  
$ tree .  
.  
|-- WEB-INF  
|   |-- classes  
|   |   \`-- servlets  
|   |       \`-- SummaryServlet.class  
|   |-- src  
|   |   \`-- servlets  
|   |       \`-- SummaryServlet.java  
|   \`-- web.xml  
|-- education.html  
|-- experience.html  
|-- images  
|-- index.html  
|-- projects.html  
|-- skills.html  
|-- styles.css  
\`-- summary.html  `


---

###Java compiler cmd line

`javac -cp [tomcat]\lib\servlet-api.jar src\servlets\SummaryServlet.java`
