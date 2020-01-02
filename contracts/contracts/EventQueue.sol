//pragma solidity ^0.4.0;   // the Solidity compiler that comes with Eris 0.12 does not support this

//
// Known issues:
// * enqueue order is not guaranteed, but depends on order of inclusion in blocks
//
// In general, "view" instead of "constant" can be used in newer Solidity compilers, but the one that comes with Eris 0.12 does not support this
//
contract EventQueue {

    uint constant CAPACITY = 1000;

    struct QueueItemStruct {
        address sender;
        string data;
        uint blockNumber;
    }

    struct QueueStruct {
        QueueItemStruct[1000 + 1] items;   // was [CAPACITY + 1], but the Solidity compiler that comes with Eris does not support this
        uint firstIndex;
        uint lastIndex;
    }

    mapping(string => QueueStruct) private queue;

    function isFull(string topic) public constant returns (bool isFull) {
        return ((queue[topic].lastIndex + 1) % queue[topic].items.length == queue[topic].firstIndex);
    }

    function getCount(string topic) public constant returns (uint count) {
        if (queue[topic].lastIndex >= queue[topic].firstIndex) {
            return queue[topic].lastIndex - queue[topic].firstIndex;
        } else {
            // data is wrapped around
            return queue[topic].items.length - queue[topic].firstIndex + queue[topic].lastIndex;
        }
    }

    function getCapacity(string topic) public constant returns (uint capacity) {
        return queue[topic].items.length - 1;
    }

    function enqueue(string topic, string data) public {
        if (isFull(topic))
            return;

        uint index = queue[topic].lastIndex;
        queue[topic].items[index].sender = msg.sender;
        queue[topic].items[index].data = data;
        queue[topic].items[index].blockNumber = block.number;
        queue[topic].lastIndex = (queue[topic].lastIndex + 1) % queue[topic].items.length;
    }

    function peek(string topic, uint offset) public constant returns (address sender, string data, uint blockNumber) {
        if (queue[topic].firstIndex + offset == queue[topic].lastIndex)
            return;

        uint index = (queue[topic].firstIndex + offset) % queue[topic].items.length;
        sender = queue[topic].items[index].sender;
        data = queue[topic].items[index].data;
        blockNumber = queue[topic].items[index].blockNumber;
    }

    function removeFirstItem(string topic) public {
        if (queue[topic].firstIndex == queue[topic].lastIndex)
            return;

        delete queue[topic].items[queue[topic].firstIndex];
        queue[topic].firstIndex = (queue[topic].firstIndex + 1) % queue[topic].items.length;
    }

    function removeFirstItems(string topic, uint count) public {
        for (uint i = 0; i < count; i++) {
            removeFirstItem(topic);
        }
    }

    function removeAllItems(string topic) public {
        removeFirstItems(topic, getCount(topic));
    }

    function getBlockNumber() public constant returns (uint blockNumber) {
        return block.number;
    }

    // internal functions

    function _firstIndex(string topic) public constant returns (uint firstIndex) {
        return queue[topic].firstIndex;
    }

    function _lastIndex(string topic) public constant returns (uint lastIndex) {
        return queue[topic].lastIndex;
    }

}
