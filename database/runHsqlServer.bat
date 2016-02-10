SETLOCAL

DEL vr_hsqldb.log
DEL vr_hsqldb.lck

ECHO CREATE SCHEMA PUBLIC AUTHORIZATION DBA>vr_hsqldb.script
ECHO CREATE USER SA PASSWORD "">>vr_hsqldb.script
ECHO GRANT DBA TO SA>>vr_hsqldb.script
ECHO SET WRITE_DELAY 10>>vr_hsqldb.script

SET HSQLDB_HOME=H:\hsqldb123


START java -classpath %HSQLDB_HOME%/lib/hsqldb.jar org.hsqldb.Server -database.0 file:vr_hsqldb -dbname.0 vr_hsqldb

START javaw -classpath %HSQLDB_HOME%/lib/hsqldb.jar org.hsqldb.util.DatabaseManager --driver org.hsqldb.jdbcDriver --url jdbc:hsqldb:hsql://localhost/vr_hsqldb --user SA --script F:\Vijay\sts\projects\visitreporting\database\001_createTables.sql
