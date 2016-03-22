set CLASSPATH=.
set CLASSPATH=%CLASSPATH%;./../lib/asm-1.5.3.jar
set CLASSPATH=%CLASSPATH%;./../lib/cglib-2.1.3.jar
set CLASSPATH=%CLASSPATH%;./../lib/commons-codec-1.3.jar
set CLASSPATH=%CLASSPATH%;./../lib/commons-collections-3.2.jar
set CLASSPATH=%CLASSPATH%;./../lib/commons-digester-1.8.jar
set CLASSPATH=%CLASSPATH%;./../lib/commons-logging-1.1.jar
set CLASSPATH=%CLASSPATH%;./../lib/dom4j-1.6.1.jar
set CLASSPATH=%CLASSPATH%;./../lib/hibernate3.jar
set CLASSPATH=%CLASSPATH%;./../lib/http.jar
set CLASSPATH=%CLASSPATH%;./../lib/iiop.jar
set CLASSPATH=%CLASSPATH%;./../lib/jade.jar
set CLASSPATH=%CLASSPATH%;./../lib/jadeTools.jar
set CLASSPATH=%CLASSPATH%;./../lib/jta.jar
set CLASSPATH=%CLASSPATH%;./../lib/junit-3.8.2.jar
set CLASSPATH=%CLASSPATH%;./../lib/log4j-1.2.8.jar
set CLASSPATH=%CLASSPATH%;./../lib/weka.jar
set CLASSPATH=%CLASSPATH%;./../lib/weka-3-4.jar
set CLASSPATH=%CLASSPATH%;./../hsqldb/hsqldb.jar


java -classpath %CLASSPATH% br.pucpr.ppgia.prototipo.io.ImportFile
