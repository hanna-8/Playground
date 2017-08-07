## VB.NET project used as demo for accessing a DB stored locally OR in AWS RDS

Narnia is a windows console application that encompases VB.NET code snippets for the following:
* Connect to local (on-premises) or AWS RDS database;
* Perform an SQL query on it;
* Call a stored procedure.

The [SetupDB.sql](.\SetupDB.sql) sql commands were used to create the local database.

[SQL Database Migration Wizard](http://sqlazuremw.codeplex.com/) was used to migrate the local DB to RDS. Check-out answers to [this Stackoverflow question](http://sqlazuremw.codeplex.com/).

The Microsoft SQL Server Management Studio (SSMS) can be used to access both databases.

