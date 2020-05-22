from platten/alpine-oracle-jre8-docker
ADD target/wecube-plugins-alicloud-1.0.0-RELEASE.jar   /application/wecube-plugins-alicloud.jar
ADD build/start.sh /scripts/start.sh
RUN chmod +x /scripts/start.sh
CMD ["/bin/sh","-c","/scripts/start.sh"]
