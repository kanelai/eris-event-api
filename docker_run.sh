#!/usr/bin/env bash

# The ":z" is for fixing permission denied problem in SELinux Docker image

docker run \
-it \
--rm \
-p 8080:8080 \
-p 8443:8443 \
-v `pwd`/config:/root/config:z \
-v `pwd`/log:/root/log:z \
eris-event-api
