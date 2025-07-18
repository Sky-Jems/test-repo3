Option Explicit

Dim shell, fso
Dim postgresDataDir, postgresBinDir, postgresDir


Set shell = CreateObject("WScript.Shell")
Set fso = CreateObject("Scripting.FileSystemObject")
mainDrive = fso.GetDriveName(shell.ExpandEnvironmentStrings("%SystemRoot%"))

postgresDir = mainDrive & "\PostgreSQLBinary"
postgresBinDir = postgresDir & "\pgsql\bin"
postgresDataDir = postgresDir & "\data"
