#!/bin/sh
mkdir -p /log
java -jar /application/wecube-plugins-alicloud.jar \
  --server.address=0.0.0.0 \
  --server.port=8080 >> /log/wecube-plugins-alicloud.log 
