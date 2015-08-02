#!/bin/bash

JAVA=/usr/java/latest/bin/java
HADOOP_LIBS=/usr/lib/hadoop/*:/usr/lib/hadoop/lib/*:/usr/lib/hadoop-hdfs/*:/usr/lib/hadoop-hdfs/lib/*
BASEDIR=$(dirname `which $0`)
CLASSPATH=$BASEDIR:$BASEDIR/backup-tool-1.0-SNAPSHOT-jar-with-dependencies.jar:$HADOOP_LIBS
DEBUG=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=9999
DEBUG=
JMX='-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9991 -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false'
OPTION="-Xmx10g $DEBUG $JMX"
MAIN=com.sysomos.grid.tools.backup.BackupTool

exec $JAVA -cp $CLASSPATH $OPTION  $MAIN  &> /dev/null  &
