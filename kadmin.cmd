
set DEBUG=
set args=%*
for %%a in (%*) do (
  if -D == %%a (
    set DEBUG=-Xdebug -Xrunjdwp:transport=dt_socket,address=8001,server=y,suspend=y
    set args=%args:-D=%
  )
)

java %DEBUG% ^
-classpath D:\kerberosServer\JARbins\* ^
-DKERBY_LOGFILE=kerby-kdc ^
org.apache.kerby.kerberos.tool.kadmin.KadminTool %args%
