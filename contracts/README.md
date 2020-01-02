# Prerequisites
* Node v6
* Node v8-v12 (if use async/await)
* Truffle 5.1.6
* Ganache 1.3.1
* solc 0.4.25

# Setup
`npm install truffle -g`

# Compile
`truffle compile`

Make sure Ganache is listening to 127.0.0.1.

# Install to Ganache
`truffle migrate --reset --network ganache` (network defined in `truffle.js`)

# Test
`truffle test --network ganache`

# Start development environment
`truffle develop`

## Deploy in Truffle Develop conosle
`migrate`

## Re-deploy
`migrate --reset`

## Run
```
SimpleStorage.deployed().then(function(instance){return instance.get.call();}).then(function(value){return value.toNumber()});
SimpleStorage.deployed().then(function(instance){return instance.set(4);});
Doug.deployed().then(Bank.deployed().then(function(instance){return instance.}));
```

# Open separate log window
`truffle develop --log`

# Debug
In log windows,
```
develop:testrpc eth_sendTransaction +0ms
develop:testrpc  +1s
develop:testrpc   Transaction: 0xe493340792ab92b95ac40e43dca6bc88fba7fd67191989d59ca30f79320e883f +2ms
develop:testrpc   Gas usage: 4712388 +11ms
develop:testrpc   Block Number: 6 +15ms
develop:testrpc   Runtime Error: out of gas +0ms
develop:testrpc  +16ms
```
In Truffle Develop console,
`debug 0xe493340792ab92b95ac40e43dca6bc88fba7fd67191989d59ca30f79320e883f`






Example output:
```
$ truffle compile 

Compiling your contracts...
===========================
> Compiling ./contracts/EventQueue.sol
> Compilation warnings encountered:
,
> Artifacts written to /Users/kane/Dropbox (Personal)/IdeaProjects/eris-event-api/contracts/build/contracts
> Compiled successfully using:
   - solc: 0.4.25+commit.59dbf8f1.Linux.g++




$ truffle migrate --reset --network ganache 

Compiling your contracts...
===========================
> Everything is up to date, there is nothing to compile.



Starting migrations...
======================
> Network name:    'ganache'
> Network id:      5777
> Block gas limit: 0x6691b7


1_initial_migration.js
======================

   Replacing 'Migrations'
   ----------------------
   > transaction hash:    0xbf483d90bf0afeac9c854907a1ca95f42f345af3bddc0cfca386cba7d4ea44b8
   > Blocks: 0            Seconds: 0
   > contract address:    0x43dfCc143387Ef3b5a81fEA4e4137d91e1D9c5a5
   > block number:        65
   > block timestamp:     1577539562
   > account:             0xC63dFA60C92D384CC3DeFEF84b65D0D600eAAA0C
   > balance:             99.6517647
   > gas used:            277462
   > gas price:           20 gwei
   > value sent:          0 ETH
   > total cost:          0.00554924 ETH


   > Saving migration to chain.
   > Saving artifacts
   -------------------------------------
   > Total cost:          0.00554924 ETH


2_deploy_contracts.js
=====================

   Replacing 'EventQueue'
   ----------------------
   > transaction hash:    0x4259dda4d603d5686dc1b67840573c29fbbbdef0342fdf3df702c4aadf5f517b
   > Blocks: 0            Seconds: 0
   > contract address:    0xA767E3C175Cb173b1DdD28Fb0c03129911367863
   > block number:        67
   > block timestamp:     1577539562
   > account:             0xC63dFA60C92D384CC3DeFEF84b65D0D600eAAA0C
   > balance:             99.61550306
   > gas used:            1771074
   > gas price:           20 gwei
   > value sent:          0 ETH
   > total cost:          0.03542148 ETH


   > Saving migration to chain.
   > Saving artifacts
   -------------------------------------
   > Total cost:          0.03542148 ETH


Summary
=======
> Total deployments:   2
> Final cost:          0.04097072 ETH





$ truffle test --network ganache 
Using network 'ganache'.


Compiling your contracts...
===========================
> Everything is up to date, there is nothing to compile.



  Contract: EventQueue
    ✓ should enqueue 3 out of 4 items for topic 1 (378ms)
    ✓ should enqueue 1 items for topic 2 (163ms)
    ✓ should get 1st queued item for topic 1 (106ms)
    ✓ should remove 1st queued item for topic 1 (131ms)
    ✓ should get 2nd queued item for topic 1 (100ms)
    ✓ should remove 2nd queued item for topic 1 (129ms)
    ✓ should get 1st queued item for topic 2 (99ms)
    ✓ should remove 1st queued item for topic 2 (125ms)
    ✓ should get nothing for topic 2 (104ms)
    ✓ should remove nothing for topic 2 (118ms)
    ✓ should get nothing for topic 3 (91ms)
    ✓ should remove nothing for topic 3 (129ms)
    ✓ should get capacity


  13 passing (2s)
```