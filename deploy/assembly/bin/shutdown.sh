#!/bin/bash
cd `dirname $0`
BIN_DIR=`pwd`/

PID=`ps -ef | grep 'java' | grep 'delay-job.jar' | grep -v 'grep' | awk '{print $2}'`
if [ ! $PID ]; then
    echo 'Server is not running.'
    exit 0
fi

echo -e 'Stoping the server ...'
kill $PID
echo -e 'Check logs for more details.'