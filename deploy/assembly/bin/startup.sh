#!/bin/bash
cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`
TOMCAT_DIR=$DEPLOY_DIR/temp
LOG_DIR=$DEPLOY_DIR/log

JAVA_OPTS="-Djava.awt.headless=true -Djava.net.preferIPv4Stack=true "
JAVA_MEM_OPTS=""
BITS=`java -version 2>&1 | grep -i 64-bit`
if [ -n "$BITS" ]; then
    JAVA_MEM_OPTS=" -Xmx2g -Xms2g -Xmn1g -Xss1m -XX:+UseG1GC "
else
    JAVA_MEM_OPTS=" -Xms1g -Xmx1g -Xmn512m -Xss512k -XX:PermSize=128m -XX:SurvivorRatio=2 -XX:+UseParallelGC "
fi

echo -e "Starting the server..."
nohup java $JAVA_OPTS $JAVA_MEM_OPTS -jar ${DEPLOY_DIR}/delay-job.jar --server.tomcat.basedir=${TOMCAT_DIR} &>${LOG_DIR}/delay-job.log &
echo -e "Check the logs for more details."

PID=`ps -ef | grep 'java' | grep 'delay-job.jar' | grep -v 'grep' | awk '{print $2}'`
echo -e "The PID Is $PID"