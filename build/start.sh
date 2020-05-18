#!/bin/sh
mkdir -p /log
export tzOffset=$(date -R | awk '{print $6}')
java -Duser.timezone=GMT$tzOffset \
  -jar /application/wecube-plugins-alicloud.jar \
  --server.address=0.0.0.0 \
  --server.port=8080 >> /log/wecube-plugins-alicloud.log 
