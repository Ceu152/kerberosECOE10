@echo off

set DEBUG=
set args=%*
for %%a in (%*) do (
  if -D == %%a (
    set DEBUG=-Xdebug -Xrunjdwp:transport=dt_socket,address=8004,server=y,suspend=y
    set args=%args:-D=%
  )
)

java %DEBUG% ^
-classpath D:\kerberosServer\JARbins\* ^
-DKERBY_LOGFILE=klist ^
org.apache.kerby.kerberos.tool.klist.KlistTool %args%
