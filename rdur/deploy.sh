#!/bin/bash
set -x 
ps -efwww|grep jenkins |grep -v grep|awk '{print $2}' > ./mypid.txt


if [ -s ./mypid.txt ]; then 
   kill -9 `cat ./mypid.txt`; 
fi; 

rm -f ./mypid.txt
java -jar jenkins.war &

