const EventQueue = artifacts.require("EventQueue");

module.exports = function (deployer) {
    deployer.deploy(EventQueue);
};
