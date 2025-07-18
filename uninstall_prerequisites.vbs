Option Explicit

Dim kafkaDir, javaDir, postgreSQLDir, mainDrive, command
Dim shell, fso

Set shell = CreateObject("WScript.Shell")
Set fso = CreateObject("Scripting.FileSystemObject")

Function DeleteDirectory(directory)
    If fso.FolderExists(directory) Then
        fso.DeleteFolder directory, True
    End If
End Function

mainDrive = fso.GetDriveName(shell.ExpandEnvironmentStrings("%SystemRoot%"))
kafkaDir = mainDrive & "\Kafka\kafka_2.12-3.9.1"
javaDir = mainDrive & "\Java\jdk-24.0.1"
postgreSQLDir = mainDrive & "\PostgreSQLBinary\pgsql"

' === Start Delete Kafka ===

' Kill all process using port 9092
command = "cmd.exe /c for /f " & Chr(34) & "tokens=5" & Chr(34) & " %a in ('netstat -aon "& Chr(94) &"| findstr :9092') do taskkill /F /PID %a"
shell.Run command, 0, True

' Kill all process using port 2181
command = "cmd.exe /c for /f " & Chr(34) & "tokens=5" & Chr(34) & " %a in ('netstat -aon "& Chr(94) &"| findstr :2181') do taskkill /F /PID %a"
shell.Run command, 0, True

DeleteDirectory kafkaDir

' === End Delete Kafka ===


' === Start Delete Java ===

' Kill all java -jar
command = "powershell -NoProfile -ExecutionPolicy Bypass -Command ""Get-CimInstance Win32_Process | Where-Object { $_.CommandLine -like '*-jar*' } | ForEach-Object { Stop-Process -Id $_.ProcessId -Force } """
shell.Run command, 0, True

DeleteDirectory javaDir
' === End Delete Java ===


' === Start Uninstall PostgreSQL ===

' Stop any postgres instance
command = "cmd.exe /c for /f " & Chr(34) & "tokens=5" & Chr(34) & " %a in ('netstat -aon "& Chr(94) &"| findstr :5432') do taskkill /F /PID %a"
shell.Run command, 0, True

DeleteDirectory postgreSQLDir 

' === End Uninstall PostgreSQL
