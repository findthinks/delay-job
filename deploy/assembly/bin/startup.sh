#!/bin/bash
USER_NAME=`whoami`
if [ "$USER_NAME" = "root" ];then
    echo "Root user can not startup app!"
    exit 1
fi

cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`

CONF_DIR=$DEPLOY_DIR/conf
LIB_DIR=$DEPLOY_DIR/lib
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`

JAVA_OPTS="-Djava.awt.headless=true -Djava.net.preferIPv4Stack=true "
JAVA_MEM_OPTS=""
BITS=`java -version 2>&1 | grep -i 64-bit`
if [ -n "$BITS" ]; then
    JAVA_MEM_OPTS=" -server -Xmx2g -Xms2g -Xmn1g -Xss1m -XX:+UseG1GC "
else
    JAVA_MEM_OPTS=" -server -Xms1g -Xmx1g -XX:PermSize=128m -XX:SurvivorRatio=2 -XX:+UseParallelGC "
fi

TOMCAT_DIR=$DEPLOY_DIR/temp
rm -rf $TOMCAT_DIR/*

echo -e "Starting the server..."
nohup java $JAVA_OPTS $JAVA_MEM_OPTS -classpath $CONF_DIR:$LIB_JARS:$DEPLOY_DIR com.findthinks.delay.job.Bootstrap --server.tomcat.basedir=$TOMCAT_DIR --static.resources.dir="classpath:"static/ >/dev/null 2>&1 &
echo -e "Check the logs for more details."

PID=`ps -ef | grep 'java' | grep 'com.findthinks.delay.job.Bootstrap' | grep -v 'grep' | awk '{print $2}'`
echo -e "The PID Is $PID"