#!/bin/bash

for n in {1..10}; do
	echo
	echo $n
	curl -X POST -H "Content-Type: application/json" 'http://127.0.0.1:8080/queue/dequeue'
done
