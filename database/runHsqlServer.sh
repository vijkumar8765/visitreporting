SETLOCAL

del vr_hsqldb.log
del vr_hsqldb.lck

echo CREATE SCHEMA PUBLIC AUTHORIZATION DBA>vr_hsqldb.script
echo CREATE USER SA PASSWORD "">>vr_hsqldb.script
echo GRANT DBA TO SA>>vr_hsqldb.script
echo SET WRITE_DELAY 10>>vr_hsqldb.script

export HSQLDB_HOME=H:\\hsqldb123
java -cp $HSQLDB_HOME//lib//hsqldb.jar org.hsqldb.Server -database.0 file:vr_hsqldb -dbname.0 vr_hsqldb
#javaw -cp $HSQLDB_HOME//lib//hsqldb.jar org.hsqldb.util.DatabaseManager --driver org.hsqldb.jdbcDriver --url jdbc:hsqldb:hsql://localhost/vr_hsqldb --user SA --script 001_createTables.sql --port 9002
java -cp $HSQLDB_HOME//lib//hsqldb.jar org.hsqldb.util.DatabaseManager --driver org.hsqldb.jdbcDriver --url jdbc:hsqldb:hsql://localhost/vr_hsqldb --user SA --port 9002
