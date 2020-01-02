#!/usr/bin/env bash

docker build -t eris-event-api .
docker save -o eris-event-api.tar eris-event-api
gzip -f eris-event-api.tar

echo Done
