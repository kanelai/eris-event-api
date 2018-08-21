'use strict';

const fs = require('fs');
const http = require('http');
const contracts = require('eris-contracts');
const express = require('express');
const address = require('./contractAddress.json').eventQueue;

// @formatter:off
const abi = [{"constant":false,"inputs":[{"name":"topic","type":"string"}],"name":"removeFirstItem","outputs":[],"type":"function"},{"constant":true,"inputs":[],"name":"getBlockNumber","outputs":[{"name":"blockNumber","type":"uint256"}],"type":"function"},{"constant":true,"inputs":[{"name":"topic","type":"string"}],"name":"getCapacity","outputs":[{"name":"","type":"uint256"}],"type":"function"},{"constant":true,"inputs":[{"name":"topic","type":"string"}],"name":"isFull","outputs":[{"name":"","type":"bool"}],"type":"function"},{"constant":false,"inputs":[{"name":"topic","type":"string"},{"name":"data","type":"string"}],"name":"enqueue","outputs":[],"type":"function"},{"constant":false,"inputs":[{"name":"topic","type":"string"},{"name":"count","type":"uint256"}],"name":"removeFirstItems","outputs":[],"type":"function"},{"constant":true,"inputs":[{"name":"topic","type":"string"},{"name":"offset","type":"uint256"}],"name":"peek","outputs":[{"name":"sender","type":"address"},{"name":"data","type":"string"}],"type":"function"},{"constant":true,"inputs":[{"name":"topic","type":"string"}],"name":"getCount","outputs":[{"name":"","type":"uint256"}],"type":"function"},{"constant":false,"inputs":[{"name":"topic","type":"string"}],"name":"removeAllItems","outputs":[],"type":"function"}];
// @formatter:on

const accounts = require('./accounts.json');

const app = express();
const bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(express.json());

process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';
const chainUrl = 'https://127.0.0.1:1337/rpc';
const queryManager = contracts.newContractManagerDev(chainUrl, accounts.testchain_participant_001, true);
const txManager = contracts.newContractManagerDev(chainUrl, accounts.testchain_participant_001, false);
const queryContract = queryManager.newContractFactory(abi).at(address);
const txContract = txManager.newContractFactory(abi).at(address);

app.get('/:topic/isFull', function (request, response) {
    queryContract.isFull(request.params.topic, function (error, isFull) {
        response.setHeader('Content-Type', 'application/json');
        if (error) {
            console.log("isFull: error");
            response.statusCode = 500;
            response.write(JSON.stringify({
                "returnCode": "error"
            }));
        } else {
            console.log("isFull: " + isFull);
            response.statusCode = 200;
            response.write(JSON.stringify({
                "returnCode": "ok",
                "isFullResponse": {
                    "isFull": isFull
                }
            }));
        }
        response.end('\n');
    })
});

app.get('/:topic/capacity', function (request, response) {
    queryContract.getCapacity(request.params.topic, function (error, capacity) {
        response.setHeader('Content-Type', 'application/json');
        if (error) {
            console.log("capacity: error");
            response.statusCode = 500;
            response.write(JSON.stringify({
                "returnCode": "error"
            }));
        } else {
            console.log("capacity: " + capacity);
            response.statusCode = 200;
            response.write(JSON.stringify({
                "returnCode": "ok",
                "capacityResponse": {
                    "capacity": capacity
                }
            }));
        }
        response.end('\n');
    })
});

app.get('/:topic/count', function (request, response) {
    queryContract.getCount(request.params.topic, function (error, count) {
        response.setHeader('Content-Type', 'application/json');
        if (error) {
            console.log("count: error");
            response.statusCode = 500;
            response.write(JSON.stringify({
                "returnCode": "error"
            }));
        } else {
            console.log("count: " + count);
            response.statusCode = 200;
            response.write(JSON.stringify({
                "returnCode": "ok",
                "countResponse": {
                    "count": count
                }
            }));
        }
        response.end('\n');
    })
});

app.post('/:topic/enqueue', function (request, response) {
    const data = JSON.stringify(request.body);
    console.log("enqueue: " + data);

    txContract.enqueue(request.params.topic, data, function (error) {
        response.setHeader('Content-Type', 'application/json');
        if (error) {
            console.log("enqueue: error");
            response.statusCode = 500;
            response.write(JSON.stringify({
                "returnCode": "error"
            }));
        } else {
            console.log("enqueue: submitted");
            response.statusCode = 200;
            response.write(JSON.stringify({
                "returnCode": "ok",
                "enqueueResponse": {}
            }));
        }
        response.end('\n');
    })
});

app.get('/:topic/peek/:offset', function (request, response) {
    const offset = parseInt(request.params.offset);
    queryContract.peek(request.params.topic, offset, function (error, item) {
        response.setHeader('Content-Type', 'application/json');
        if (error) {
            console.log("peek(" + request.params.offset + "): error");
            response.statusCode = 500;
            response.write(JSON.stringify({
                "returnCode": "error"
            }));
        } else {
            console.log("peek(" + request.params.offset + "): " + item);
            response.statusCode = 200;
            response.write(JSON.stringify({
                "returnCode": "ok",
                "peekResponse": {
                    "item": {
                        "offset": offset,
                        "sender": item[0],
                        "data": item[1]
                    }
                }
            }));
        }
        response.end('\n');
    })
});

app.post('/:topic/removeFirstItem', function (request, response) {
    txContract.removeFirstItem(request.params.topic, function (error) {
        response.setHeader('Content-Type', 'application/json');
        if (error) {
            console.log("removeFirstItem: error");
            response.statusCode = 500;
            response.write(JSON.stringify({
                "returnCode": "error"
            }));
        } else {
            console.log("removeFirstItem: submitted");
            response.statusCode = 200;
            response.write(JSON.stringify({
                "returnCode": "ok",
                "removeFirstItemResponse": {}
            }));
        }
        response.end('\n');
    })
});

app.post('/:topic/removeFirstItems/:count', function (request, response) {
    txContract.removeFirstItems(request.params.topic, parseInt(request.params.count), function (error) {
        response.setHeader('Content-Type', 'application/json');
        if (error) {
            console.log("removeFirstItems: error");
            response.statusCode = 500;
            response.write(JSON.stringify({
                "returnCode": "error"
            }));
        } else {
            console.log("removeFirstItems: submitted");
            response.statusCode = 200;
            response.write(JSON.stringify({
                "returnCode": "ok",
                "removeFirstItemResponse": {}
            }));
        }
        response.end('\n');
    })
});

app.post('/:topic/removeAllItems', function (request, response) {
    txContract.removeAllItems(request.params.topic, function (error) {
        response.setHeader('Content-Type', 'application/json');
        if (error) {
            console.log("removeAllItems: error");
            response.statusCode = 500;
            response.write(JSON.stringify({
                "returnCode": "error"
            }));
        } else {
            console.log("removeAllItems: submitted");
            response.statusCode = 200;
            response.write(JSON.stringify({
                "returnCode": "ok",
                "removeAllItemsResponse": {}
            }));
        }
        response.end('\n');
    })
});

app.get('/blockNumber', function (request, response) {
    queryContract.getBlockNumber(function (error, blockNumber) {
        response.setHeader('Content-Type', 'application/json');
        if (error) {
            console.log("blockNumber: error");
            response.statusCode = 500;
            response.write(JSON.stringify({
                "returnCode": "error"
            }));
        } else {
            console.log("blockNumber: " + blockNumber);
            response.statusCode = 200;
            response.write(JSON.stringify({
                "returnCode": "ok",
                "blockNumberResponse": {
                    "blockNumber": blockNumber
                }
            }));
        }
        response.end('\n');
    })
});

const httpServer = http.createServer(app);
httpServer.listen(1111, '0.0.0.0', function () {
    console.log('Listening for HTTP requests on port 1111')
});
