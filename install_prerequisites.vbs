Option Explicit

Dim kafkaUrl, javaUrl, net8Url, dbUrl, kafkaDir, net8Dir, javaDir, postgresDir, modifiedKafkaFile, tempDownloadKafkaPath, tempDownloadJavaPath, tempDownloadDbPath, kafkaBinPath, javaBinPath
Dim xmlHttp, adoStream, fso, shell, command, mainDrive

' === FUNCTIONS ===
Function DownloadFile(url, savePath)
    Dim xmlHttp, adoStream

    On Error Resume Next

    Set xmlHttp = CreateObject("MSXML2.XMLHTTP")
    xmlHttp.Open "GET", url, False
    xmlHttp.Send

    If xmlHttp.Status = 200 Then
        Set adoStream = CreateObject("ADODB.Stream")
        adoStream.Type = 1 ' Binary
        adoStream.Open
        adoStream.Write xmlHttp.responseBody
        adoStream.SaveToFile savePath, 2 ' Overwrite
        adoStream.Close
        DownloadFile = True
    Else
        MsgBox "Download failed with status: " & xmlHttp.Status
        DownloadFile = False
    End If

    On Error GoTo 0
End Function

Function CleanUpTemporaryFiles(arr)
    Dim i
    For i = 0 To UBound(arr)
        If fso.FileExists(arr(i)) Then
            fso.DeleteFile arr(i), True
        End If
    Next
End Function
'=== END ===


'=== CONFIGURATION ===
Set shell = CreateObject("WScript.Shell")
Set fso = CreateObject("Scripting.FileSystemObject")
mainDrive = fso.GetDriveName(shell.ExpandEnvironmentStrings("%SystemRoot%"))

kafkaUrl = "https://dlcdn.apache.org/kafka/3.9.1/kafka_2.12-3.9.1.tgz"
javaUrl = "https://download.java.net/java/GA/jdk24.0.1/24a58e0e276943138bf3e963e6291ac2/9/GPL/openjdk-24.0.1_windows-x64_bin.zip"
dbUrl = "https://get.enterprisedb.com/postgresql/postgresql-17.5-2-windows-x64.exe"
kafkaDir = mainDrive & "\Kafka"
javaDir = mainDrive & "\Java"
postgresDir = mainDrive & "\Program Files\PostgreSQL\17\bin"
kafkaBinPath = mainDrive & "\Kafka\kafka_2.12-3.9.1\bin"
javaBinPath = mainDrive & "\Java\jdk-24.0.1\bin"
modifiedKafkaFile = mainDrive & "\Program Files\Skydev Solutions Inc\ServerPOS\kafka-server-start.bat"
tempDownloadJavaPath = CreateObject("Scripting.FileSystemObject").GetSpecialFolder(2) & "\java.zip"
tempDownloadDbPath = CreateObject("Scripting.FileSystemObject").GetSpecialFolder(2) & "\postgres.exe"
tempDownloadKafkaPath = CreateObject("Scripting.FileSystemObject").GetSpecialFolder(2) & "\kafka.tgz"
'=== END ===


'=== KAFKA ===
If Not fso.FolderExists(kafkaBinPath) Then
    fso.CreateFolder kafkaDir
    If DownloadFile(kafkaUrl, tempDownloadKafkaPath) Then
        command = "tar -xvzf """ & tempDownloadKafkaPath & """ -C """ & kafkaDir & """"
        shell.Run command, 0, True
    Else
        WScript.Quit
    End If
End If
'=== END ===


' === Copy Modified Kafka Start ===
kafkaBinPath = kafkaBinPath & "\windows"
command = "powershell -ExecutionPolicy Bypass -Command ""Copy-Item -Path '" & modifiedKafkaFile & "' -Destination '" & kafkaBinPath & "' -Force"""
shell.Run command, 0, True
'=== END ===


'=== JAVA ===
If Not fso.FolderExists(javaBinPath) Then
    fso.CreateFolder javaDir
    If DownloadFile(javaUrl, tempDownloadJavaPath) Then
        command = "powershell -WindowStyle Hidden -nologo -noprofile -command Expand-Archive -LiteralPath '" & tempDownloadJavaPath & "' -DestinationPath '" & javaDir & "' -Force"
        shell.Run command, 0, True

    Else
        WScript.Quit
    End If
End If
'=== END ===


'=== PostgreSQL ===
If Not fso.FolderExists(postgresDir) Then
    If DownloadFile(dbUrl, tempDownloadDbPath) Then
        command = "powershell -NoProfile -ExecutionPolicy Bypass -Command ""Start-Process '" & tempDownloadDbPath & "'"""
        shell.Run tempDownloadDbPath, 1, True
    End If
End If
'=== END ===


' === Optional: Clean up installer ===
CleanUpTemporaryFiles(Array(tempDownloadJavaPath, tempDownloadKafkaPath, tempDownloadDbPath))
'=== END ===


MsgBox "Kafka, Java, .NET 8 and PostgreSQL installed", vbInformation, "Done"
