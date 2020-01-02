const Web3 = require("web3");

// check from output of deploy script
const EVENT_QUEUE_CONTRACT_ADDRESS = "0x613d49033f64b0a2834dc14b669d29f3f44aece8";

// check from Ganache
const FROM_ADDRESS1 = "0xBfd3c6583EA353850a193689fdaEbFff7Ef527e1";
const FROM_ADDRESS2 = "0xbA51945054F8352FaeD88023cf5Fc71Ef6868e53";
const FROM_ADDRESS3 = "0xD9A62E1fe212f6832379c07AecCAc1c1B6396832";

const MY_TOPIC = "topic1";

const web3 = new Web3(new Web3.providers.HttpProvider("http://localhost:7545"));

const json = require("../../build/contracts/EventQueue.json");
const abi = json.abi;

let eventQueue = new web3.eth.Contract(abi, EVENT_QUEUE_CONTRACT_ADDRESS);

async function check() {
    console.log();
    console.log("***check***");

    let capacity = await eventQueue.methods.capacity(MY_TOPIC).call();
    console.log("capacity=" + capacity);

    let count = await eventQueue.methods.count(MY_TOPIC).call();
    console.log("count=" + count);

    let isFull = await eventQueue.methods.isFull(MY_TOPIC).call();
    console.log("isFull=" + isFull);

    let firstIndex = await eventQueue.methods._firstIndex(MY_TOPIC).call();
    console.log("firstIndex=" + firstIndex);

    let lastIndex = await eventQueue.methods._lastIndex(MY_TOPIC).call();
    console.log("lastIndex=" + lastIndex);
}

function enqueue1() {
    console.log();
    console.log("***enqueue1***");

    eventQueue.methods.enqueue(MY_TOPIC, "hello1").send({from: FROM_ADDRESS1});
}

function enqueue2() {
    console.log();
    console.log("***enqueue2***");

    eventQueue.methods.enqueue(MY_TOPIC, "hello2").send({from: FROM_ADDRESS1});
    eventQueue.methods.enqueue(MY_TOPIC, "hello3").send({from: FROM_ADDRESS2});
}

async function peek() {
    console.log();
    console.log("***peek***");

    let result = await eventQueue.methods.peek(MY_TOPIC).call();
    let address = result[0];
    let item = result[1];
    console.log("address=" + address);
    console.log("item=" + item);
}

function removeFirstItem() {
    console.log();
    console.log("***removeFirstItem***");

    eventQueue.methods.removeFirstItem(MY_TOPIC).send({from: FROM_ADDRESS3});
}

delay = 0;

check();

setTimeout(enqueue1, (delay += 2000));
setTimeout(check, (delay += 5000));

setTimeout(peek, (delay += 5000));
setTimeout(removeFirstItem, (delay += 2000));
setTimeout(check, (delay += 5000));

setTimeout(enqueue2, (delay += 2000));
setTimeout(check, (delay += 5000));

setTimeout(peek, (delay += 5000));
setTimeout(removeFirstItem, (delay += 2000));
setTimeout(check, (delay += 5000));

setTimeout(peek, (delay += 5000));
setTimeout(removeFirstItem, (delay += 2000));
setTimeout(check, (delay += 5000));

setTimeout(enqueue1, (delay += 2000));
setTimeout(enqueue2, (delay += 0));
setTimeout(enqueue2, (delay += 0));
setTimeout(check, (delay += 5000));

setTimeout(enqueue1, (delay += 2000));
setTimeout(check, (delay += 5000));

setTimeout(peek, (delay += 5000));
setTimeout(removeFirstItem, (delay += 2000));
setTimeout(check, (delay += 5000));

setTimeout(peek, (delay += 5000));
setTimeout(removeFirstItem, (delay += 2000));
setTimeout(check, (delay += 5000));

setTimeout(peek, (delay += 5000));
setTimeout(removeFirstItem, (delay += 2000));
setTimeout(check, (delay += 5000));

setTimeout(peek, (delay += 5000));
setTimeout(removeFirstItem, (delay += 2000));
setTimeout(check, (delay += 5000));

setTimeout(peek, (delay += 5000));
setTimeout(removeFirstItem, (delay += 2000));
setTimeout(check, (delay += 5000));

setTimeout(peek, (delay += 5000));
setTimeout(removeFirstItem, (delay += 2000));
setTimeout(check, (delay += 5000));
