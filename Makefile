current_dir:=$(shell pwd)
version:=$(shell bash ./build/version.sh)
date:=$(shell date +%Y%m%d%H%M%S)
project_name:=$(shell basename "${current_dir}")
remote_docker_image_registry=ccr.ccs.tencentyun.com/webankpartners/$(project_name)
PORT_BINDINGS={{ALLOCATE_PORT}}:8080

clean:
	rm -rf $(current_dir)/target

.PHONY:build

build_name=wecube-plugin-ali-build
build:
	mkdir -p repository
	docker run --rm --name $(build_name) -v /data/repository:/usr/src/mymaven/repository   -v $(current_dir)/build/maven_settings.xml:/usr/share/maven/ref/settings-docker.xml  -v $(current_dir):/usr/src/mymaven -w /usr/src/mymaven maven:3.3-jdk-8 mvn -U clean install -Dmaven.test.skip=true -s /usr/share/maven/ref/settings-docker.xml dependency:resolve

image: build
	docker build -t $(project_name):$(version) .

push:
	docker tag  $(project_name):$(version) $(remote_docker_image_registry):$(date)-$(version)
	docker push  $(remote_docker_image_registry):$(date)-$(version)

package: image
	sed 's/{{PLUGIN_VERSION}}/$(version)/' ./build/register.xml.tpl > ./register.xml
	sed -i 's/{{PORTBINDINGS}}/$(PORT_BINDINGS)/' ./register.xml
	sed -i 's/{{IMAGENAME}}/$(project_name):$(version)/g' ./register.xml
	sed -i 's/{{CONTAINERNAME}}/$(project_name)-$(version)/g' ./register.xml 
	docker save -o  image.tar $(project_name):$(version)
	zip  $(project_name)-$(version).zip image.tar register.xml
	rm -rf ./*.tar
	rm -f register.xml
	docker rmi $(project_name):$(version)
	rm -rf $(project_name)

upload: package
	$(eval container_id:=$(shell docker run -v $(current_dir):/package -itd --entrypoint=/bin/sh minio/mc))
	docker exec $(container_id) mc config host add wecubeS3 $(s3_server_url) $(s3_access_key) $(s3_secret_key) wecubeS3
	docker exec $(container_id) mc cp /package/$(project_name)-$(version).zip wecubeS3/wecube-plugin-package-bucket
	docker stop $(container_id)
	docker rm -f $(container_id)
	rm -rf $(project_name)-$(version).zip
