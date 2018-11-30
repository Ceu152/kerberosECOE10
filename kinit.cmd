@echo off

set DEBUG=
set args=%*
for %%a in (%*) do (
  if -D == %%a (
    set DEBUG=-Xdebug -Xrunjdwp:transport=dt_socket,address=8002,server=y,suspend=y
    set args=%args:-D=%
  )
)

java %DEBUG% ^
-classpath D:\kerberosServer\JARbins\* ^
-DKERBY_LOGFILE=kinit ^
org.apache.kerby.kerberos.tool.kinit.KinitTool %args%
pause