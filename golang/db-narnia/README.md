## Demo golang project to access an SQL Server

**db-narnia** is a demo project to practice db connections from go, as well as running parallel operations on the db. It:
* connects to an SQL Server database, e.g. an Amazon RDS instance;
* calls a stored procedure to print all entries in the Characters table;
* performs an SQL query on it to compute the average of the total number of appearances of the named characters;
* performs the last two operations in paralel through goroutines.

### Example output:

```
Name: Aslan; appearances: 7; role: Major.
Avg: 4
Name: Digory Kirke; appearances: 3; role: Major.
Name: Lucy Pevensie; appearances: 5; role: Major.
Name: Reepicheep the Mouse; appearances: 3; role: Minor.
panic: show me the stack

goroutine 1 [running]:
main.main()
```

### Dependencies:

* An MS SQL [golang driver](https://github.com/golang/go/wiki/SQLDrivers): [go-mssqldb](https://github.com/denisenkom/go-mssqldb).  
  Run `go get github.com/denisenkom/go-mssqldb` to install it.
* The driver itself has another dependency, the context package.  
  Run `go get golang.org/x/net/context` to install it.
