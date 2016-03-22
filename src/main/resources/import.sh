CAMINHO=.
CAMINHO=$CAMINHO:./../lib/asm-1.5.3.jar
CAMINHO=$CAMINHO:./../lib/cglib-2.1.3.jar
CAMINHO=$CAMINHO:./../lib/commons-codec-1.3.jar
CAMINHO=$CAMINHO:./../lib/commons-collections-3.2.jar
CAMINHO=$CAMINHO:./../lib/commons-digester-1.8.jar
CAMINHO=$CAMINHO:./../lib/commons-logging-1.1.jar
CAMINHO=$CAMINHO:./../lib/dom4j-1.6.1.jar
CAMINHO=$CAMINHO:./../lib/hibernate3.jar
CAMINHO=$CAMINHO:./../lib/http.jar
CAMINHO=$CAMINHO:./../lib/iiop.jar
CAMINHO=$CAMINHO:./../lib/jade.jar
CAMINHO=$CAMINHO:./../lib/jadeTools.jar
CAMINHO=$CAMINHO:./../lib/jta.jar
CAMINHO=$CAMINHO:./../lib/junit-3.8.2.jar
CAMINHO=$CAMINHO:./../lib/log4j-1.2.8.jar
CAMINHO=$CAMINHO:./../lib/weka.jar
CAMINHO=$CAMINHO:./../lib/weka-3-4.jar
CAMINHO=$CAMINHO:./../hsqldb/hsqldb.jar
echo $CAMINHO
java -classpath $CAMINHO br.pucpr.ppgia.prototipo.core.ReputationSystem