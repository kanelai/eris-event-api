#!/usr/bin/env bash

docker rmi eris-event-api 2>/dev/null
docker load < eris-event-api.tar.gz
