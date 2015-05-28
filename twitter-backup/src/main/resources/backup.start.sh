#!/bin/bash

JAVA=/usr/java/latest/bin/java
HADOOP_LIBS=/usr/lib/hadoop/*:/usr/lib/hadoop/lib/*:/usr/lib/hadoop-hdfs/*:/usr/lib/hadoop-hdfs/lib/*
BASEDIR=$(dirname `which $0`)
CLASSPATH=$BASEDIR:$BASEDIR/backup-tool-1.0-SNAPSHOT-jar-with-dependencies.jar:$HADOOP_LIBS
DEBUG=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=9999
DEBUG=
OPTION="-Xmx10g $DEBUG"
MAIN=com.sysomos.grid.tools.backup.BackupTool

exec $JAVA -cp $CLASSPATH $OPTION  $MAIN  &> /dev/null  &
