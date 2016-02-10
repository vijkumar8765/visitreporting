SETLOCAL
SET HSQLDB_HOME=H:\hsqldb123
javaw -classpath %HSQLDB_HOME%/lib/hsqldb.jar org.hsqldb.util.DatabaseManager --driver org.hsqldb.jdbcDriver --url jdbc:hsqldb:hsql://localhost/vr_hsqldb --user SA
