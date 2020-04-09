from platten/alpine-oracle-jre8-docker
ADD target/wecube-plugins-alicloud-0.1.0-SNAPSHOT.jar   /application/wecube-plugins-alicloud.jar
ADD build/start.sh /scripts/start.sh
RUN chmod +x /scripts/start.sh
CMD ["/bin/sh","-c","/scripts/start.sh"]
