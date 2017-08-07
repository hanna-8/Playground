## Demo VB.NET project: access an SQL Server db stored locally or in AWS RDS

Narnia is a windows console application that encompases VB.NET code snippets for the following:
* Connect to an SQL Server database stored in AWS RDS or locally (on-premises);
* Perform an SQL query on it;
* Call a stored procedure.

[SQL Database Migration Wizard](http://sqlazuremw.codeplex.com/) was used to migrate the local DB to RDS. Check-out answers to [this Stackoverflow question](https://stackoverflow.com/questions/10516118/migrate-to-amazon-sql-server-rds).

The Microsoft SQL Server Management Studio (SSMS) can be used to handle both databases.

The [SetupDB.sql](Narnia/SetupDB.sql) sql commands were used to create the local database.

The project was created with Visual Studio 2017.
