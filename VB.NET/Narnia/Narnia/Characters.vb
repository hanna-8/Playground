Imports System.Data.SqlClient

Public Module Characters

    Sub Main()
        Console.WriteLine("The Chronicles of Narnia: Characters inventory")
        Console.WriteLine("==============================================")
        Console.WriteLine()

        ' Local connection:
        Dim connectionStr As String = "Data Source=(localdb)\MSSQLLocalDB;Initial Catalog=Narnia;"

        ' AWS RDS connection:
        ' Dim connectionStr As String = "Data Source=<RDS-instance-name>;Initial Catalog=Narnia;User Id=<user-name>;Password=<password>"

        Using connection As New SqlConnection(connectionStr)
            connection.Open()

            PrintAll(connection)
            PrintByAppearances(connection, minAppearances:=4)
        End Using
    End Sub

    ' Execute query demo.
    Sub PrintAll(connection As SqlConnection)
        Dim queryStr As String = "select * from Characters;"
        Dim command As New SqlCommand(queryStr, connection)

        Dim reader As SqlDataReader = command.ExecuteReader()

        Console.WriteLine(String.Format("{0,-20} | {1,-15} | {2}", "Name", "Appearances Nr", "Role"))
        Console.WriteLine(String.Format("{0,-20} | {1,-15} | {2}", "--------------------", "---------------", "-----"))
        While reader.Read()
            Dim record As IDataRecord = CType(reader, IDataRecord)
            Console.WriteLine(String.Format("{0,-20} | {1,-15} | {2}", record(0), record(1), record(2)))
        End While
        Console.WriteLine("----------------------------------------------")
        Console.WriteLine()

        reader.Close()
    End Sub

    ' Call stored procedure demo.
    Sub PrintByAppearances(connection As SqlConnection, minAppearances As Int16)
        Dim command As New SqlCommand
        command.Connection = connection
        command.CommandText = "SelectByAppearances"
        command.CommandType = CommandType.StoredProcedure
        command.Parameters.AddWithValue("appNr", minAppearances)

        Console.WriteLine("The following characters appear in more than {0}", minAppearances)
        Console.WriteLine("volumes of Narnia:")
        Dim reader As SqlDataReader = command.ExecuteReader()
        While reader.Read()
            Dim record As IDataRecord = CType(reader, IDataRecord)
            Console.WriteLine(String.Format("* {0}", record(0)))
        End While
        Console.WriteLine("----------------------------------------------")
        Console.WriteLine()

        reader.Close()
    End Sub

End Module
