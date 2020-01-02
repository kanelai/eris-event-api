# eris-event-api
An experimental event queue on Eris (or Ethereum) blockchain

This is NOT well-tested, but just my own proof-of-concept to see if we can emulate a pub-sub system using Eris blockchain.
It is developed based on Eris 0.12, but the smart contract is developed using Truffle. So it works fine in Ethereum as well.

---
Build Docker image:

`./docker_build.sh`

Load image (in server):

`./docker_load.sh`

Start container (in server):

`./docker_run.sh`
