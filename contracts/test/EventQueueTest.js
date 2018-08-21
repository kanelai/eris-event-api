const myContract = artifacts.require("./EventQueue.sol");

contract('EventQueue', async (accounts) => {

    it("should enqueue 3 out of 4 items for topic 1", async () => {
        let queue = await myContract.deployed();

        let countBefore = await queue.count("topic1");
        assert.equal(countBefore.valueOf(), 0);

        await queue.enqueue("topic1", "111");
        await queue.enqueue("topic1", "222");
        await queue.enqueue("topic1", "333");
        await queue.enqueue("topic1", "444");

        let countAfter = await queue.count("topic1");
        assert.equal(countAfter.valueOf(), 3);
    });

    it("should enqueue 1 items for topic 2", async () => {
        let queue = await myContract.deployed();

        let countBefore = await queue.count("topic2");
        assert.equal(countBefore.valueOf(), 0);

        await queue.enqueue("topic2", "aaa");

        let countAfter = await queue.count("topic2");
        assert.equal(countAfter.valueOf(), 1);
    });

    it("should get 1st queued item for topic 1", async () => {
        let queue = await myContract.deployed();

        let countBefore = await queue.count("topic1");
        assert.equal(countBefore.valueOf(), 3);

        let result = await queue.peek("topic1");
        let address = result[0];
        let item = result[1];
        assert.equal(address, accounts[0]);
        assert.equal(item, "111");

        let countAfter = await queue.count("topic1");
        assert.equal(countAfter.valueOf(), 3);
    });

    it("should remove 1st queued item for topic 1", async () => {
        let queue = await myContract.deployed();

        let countBefore = await queue.count("topic1");
        assert.equal(countBefore.valueOf(), 3);

        await queue.removeFirstItem("topic1");

        let countAfter = await queue.count("topic1");
        assert.equal(countAfter.valueOf(), 2);
    });

    it("should get 2nd queued item for topic 1", async () => {
        let queue = await myContract.deployed();

        let countBefore = await queue.count("topic1");
        assert.equal(countBefore.valueOf(), 2);

        let result = await queue.peek("topic1");
        let address = result[0];
        let item = result[1];
        assert.equal(address, accounts[0]);
        assert.equal(item, "222");

        let countAfter = await queue.count("topic1");
        assert.equal(countAfter.valueOf(), 2);
    });

    it("should remove 2nd queued item for topic 1", async () => {
        let queue = await myContract.deployed();

        let countBefore = await queue.count("topic1");
        assert.equal(countBefore.valueOf(), 2);

        await queue.removeFirstItem("topic1");

        let countAfter = await queue.count("topic1");
        assert.equal(countAfter.valueOf(), 1);
    });

    it("should get 1st queued item for topic 2", async () => {
        let queue = await myContract.deployed();

        let countBefore = await queue.count("topic2");
        assert.equal(countBefore.valueOf(), 1);

        let result = await queue.peek("topic2");
        let address = result[0];
        let item = result[1];
        assert.equal(address, accounts[0]);
        assert.equal(item, "aaa");

        let countAfter = await queue.count("topic2");
        assert.equal(countAfter.valueOf(), 1);
    });

    it("should remove 1st queued item for topic 2", async () => {
        let queue = await myContract.deployed();

        let countBefore = await queue.count("topic2");
        assert.equal(countBefore.valueOf(), 1);

        await queue.removeFirstItem("topic2");

        let countAfter = await queue.count("topic2");
        assert.equal(countAfter.valueOf(), 0);
    });

    it("should get nothing for topic 2", async () => {
        let queue = await myContract.deployed();

        let countBefore = await queue.count("topic2");
        assert.equal(countBefore.valueOf(), 0);

        let result = await queue.peek("topic2");
        let address = result[0];
        let item = result[1];
        assert.equal(address, 0x0);
        assert.equal(item, "");

        let countAfter = await queue.count("topic3");
        assert.equal(countAfter.valueOf(), 0);
    });

    it("should remove nothing for topic 2", async () => {
        let queue = await myContract.deployed();

        let countBefore = await queue.count("topic2");
        assert.equal(countBefore.valueOf(), 0);

        await queue.removeFirstItem("topic2");

        let countAfter = await queue.count("topic2");
        assert.equal(countAfter.valueOf(), 0);
    });

    it("should get nothing for topic 3", async () => {
        let queue = await myContract.deployed();

        let countBefore = await queue.count("topic3");
        assert.equal(countBefore.valueOf(), 0);

        let result = await queue.peek("topic3");
        let address = result[0];
        let item = result[1];
        assert.equal(address, 0x0);
        assert.equal(item, "");

        let countAfter = await queue.count("topic3");
        assert.equal(countAfter.valueOf(), 0);
    });

    it("should remove nothing for topic 3", async () => {
        let queue = await myContract.deployed();

        let countBefore = await queue.count("topic3");
        assert.equal(countBefore.valueOf(), 0);

        await queue.removeFirstItem("topic1");

        let countAfter = await queue.count("topic3");
        assert.equal(countAfter.valueOf(), 0);
    });

    it("should get capacity", async () => {
        let queue = await myContract.deployed();
        let capacity = await queue.capacity("topic1");
        assert.equal(capacity.valueOf(), 3);
    });

});
