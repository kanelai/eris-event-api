
1. Deploy
```
[blockchain@bochk-eris-2 deploy]$ ./deploy.sh testchain 13942CFA112D79F335C2B4272902AF6B35147F4A MyContract.sol
The marmots could not find [docker-machine] installed. While it is not required to be used with Eris, we strongly recommend it be installed for maximum blockchain awesomeness
Performing action. This can sometimes take a wee while
The marmots could not read that package.json. Will use defaults.
Running container                             compilers-9afceba2-5ae5-43f4-8a92-74a0212e11a7
Container already running. Skipping           compilers-9afceba2-5ae5-43f4-8a92-74a0212e11a7
No chain flag used. Booting chain from package file
                                              testchain
Starting chain                           name=testchain
Booting chain dependencies             chains=[]
                                     services=[]
Starting a chain                              testchain
Running container                             testchain-8caa818a-f70f-439e-9d2a-5663c967683b
Container already running. Skipping           testchain-8caa818a-f70f-439e-9d2a-5663c967683b
Getting data container situated           dir=inbound
Creating data container                       deploy_tmp_-2398f594-7bab-4d1b-8a74-d7536e7e3cb9
Data container created                        deploy_tmp_-2398f594-7bab-4d1b-8a74-d7536e7e3cb9
Executing data container                      
Executing data container                      deploy_tmp_-2398f594-7bab-4d1b-8a74-d7536e7e3cb9
                                         args=[test -d /home/eris/.eris/apps/deploy]
Data container configured               image=quay.io/eris/data:0.12.0
Executing interactive data container          interactive-2f38c00f-580e-4565-8783-635a9e703710
Waiting for container to exit                 interactive-2f38c00f-580e-4565-8783-635a9e703710
Removing data container                       interactive-2f38c00f-580e-4565-8783-635a9e703710
Data container removed                        interactive-2f38c00f-580e-4565-8783-635a9e703710
Directory missing                 destination=/home/eris/.eris/apps/deploy
                                        error=Container interactive-2f38c00f-580e-4565-8783-635a9e703710 exited with status 1
Running data container                        deploy_tmp_-2398f594-7bab-4d1b-8a74-d7536e7e3cb9
                                         args=[/bin/mkdir -p /home/eris/.eris/apps/deploy]
Data container configured               image=quay.io/eris/data:0.12.0
Starting data container                       interactive-ed6ed8dc-f843-4fcb-817d-6be121e00c70
Waiting for data container to exit            interactive-ed6ed8dc-f843-4fcb-817d-6be121e00c70
Getting logs from container                   interactive-ed6ed8dc-f843-4fcb-817d-6be121e00c70
Removing data container                       interactive-ed6ed8dc-f843-4fcb-817d-6be121e00c70
Container removed                             interactive-ed6ed8dc-f843-4fcb-817d-6be121e00c70
Executing data container                      
Executing data container                      deploy_tmp_-2398f594-7bab-4d1b-8a74-d7536e7e3cb9
                                         args=[test -d /home/eris/.eris/apps/deploy]
Data container configured               image=quay.io/eris/data:0.12.0
Executing interactive data container          interactive-ecdb6a86-806b-41e7-b4ee-d6bcc8225253
Waiting for container to exit                 interactive-ecdb6a86-806b-41e7-b4ee-d6bcc8225253
Removing data container                       interactive-ecdb6a86-806b-41e7-b4ee-d6bcc8225253
Data container removed                        interactive-ecdb6a86-806b-41e7-b4ee-d6bcc8225253
Copying into container                        deploy_tmp_-2398f594-7bab-4d1b-8a74-d7536e7e3cb9
Running data container                        deploy_tmp_-2398f594-7bab-4d1b-8a74-d7536e7e3cb9
                                         args=[chown --recursive eris /home/eris/.eris/apps/deploy]
Data container configured               image=quay.io/eris/data:0.12.0
Starting data container                       interactive-a1a9460e-f872-471f-b101-452c3588e81f
Waiting for data container to exit            interactive-a1a9460e-f872-471f-b101-452c3588e81f
Getting logs from container                   interactive-a1a9460e-f872-471f-b101-452c3588e81f
Removing data container                       interactive-a1a9460e-f872-471f-b101-452c3588e81f
Container removed                             interactive-a1a9460e-f872-471f-b101-452c3588e81f
Package path does not exist on the host or is inside the pkg path
PM files do not exist on the host or are inside the pkg path
Starting pkgs action container
                                        image=quay.io/eris/pm:0.12.0
                                      service=deploy_tmp_
Executing container                           deploy_tmp_-551e9b35-7d3d-4755-aae5-53b129d42adb
Manage data containers?              autodata=true
Data container already exists, am not creating
Executing interactive container               interactive-a7b4b6c9-761e-474f-bd27-40ea74b14513
                                          cmd=[]
                               data container=deploy_tmp_-2398f594-7bab-4d1b-8a74-d7536e7e3cb9
                                   entrypoint=[eris-pm --chain tcp://chain:46657 --sign http://keys:4767 --verbose --debug --overwrite --output json --set ,solFile=MyContract.sol --file epm.yaml --summary --gas 1111111111 --compiler http://compilers:9099 --address 13942CFA112D79F335C2B4272902AF6B35147F4A --fee 1234 --amount 9999]
                                  environment=[]
                                        image=quay.io/eris/pm:0.12.0
                              ports published=false
                                         user=eris
                                         vols=[]
                                      workdir=/home/eris/.eris/apps/deploy
Waiting for container to exit                 interactive-a7b4b6c9-761e-474f-bd27-40ea74b14513
Removing container                            interactive-a7b4b6c9-761e-474f-bd27-40ea74b14513
Container removed                             interactive-a7b4b6c9-761e-474f-bd27-40ea74b14513
Hello! I'm EPM.
Using Compiler at                             http://compilers:9099
Using Chain at                                tcp://chain:46657
Using Signer at                               http://keys:4767
Using ChainID from Node                       testchain
Loading eris-pm Package Definition File.
Executing Job                                 defaultAddr
Type                                          Account
Setting Account                               13942CFA112D79F335C2B4272902AF6B35147F4A
Getting Public Key                       from=http://keys:4767
Executing Job                                 solFile
Type                                          Set
Setting Variable                              MyContract.sol
Executing Job                                 deployMyContract
Type                                          Deploy
Contract path                                 MyContract.sol
Setting compiler path                         http://compilers:9099
Response                                  abi=[{"constant":false,"inputs":[{"name":"x","type":"uint256"}],"name":"set","outputs":[],"type":"function"},{"constant":true,"inputs":[],"name":"get","outputs":[{"name":"retVal","type":"uint256"}],"type":"function"}]
                                          bin=606060405260978060106000396000f360606040526000357c01000000000000000000000000000000000000000000000000000000009004806360fe47b11460415780636d4ce63c14605757603f565b005b605560048080359060200190919050506078565b005b606260048050506086565b6040518082815260200191505060405180910390f35b806000600050819055505b50565b600060006000505490506094565b9056
                                         name=MyContract
Deploying the only contract in file      path=MyContract.sol
Deploying Contract                       name=MyContract
                                         code=606060405260978060106000396000f360606040526000357c01000000000000000000000000000000000000000000000000000000009004806360fe47b11460415780636d4ce63c14605757603f565b005b605560048080359060200190919050506078565b005b606260048050506086565b6040518082815260200191505060405180910390f35b806000600050819055505b50565b600060006000505490506094565b9056
                                       source=13942CFA112D79F335C2B4272902AF6B35147F4A
you have specified both a pubkey and an address. the pubkey takes precedent
                                      address=13942CFA112D79F335C2B4272902AF6B35147F4A
                                   public key=49DD73B526A87D720EAE4FFB19177E310A33E029EEDCAE399DC0C28A6F112DA3
[eris-client] recceived confirmation for event (Acc/13942CFA112D79F335C2B4272902AF6B35147F4A/Input) with subscription id (75945C13BD80D8F369C9CA77070E247758F713A870F79CCD3FED9B3EA550E4B1).
[eris-client] recceived confirmation for event (NewBlock) with subscription id (4539A5E6D7B06F9691F00D001AFC0E359DC3AEF395B4655C66E33B751C1AD19A).
                                         addr=0B6767A54785C94D687FE040AA7C171AD15DA4D2
                                       txHash=C80A54EE3085049259F4ABFEDBF8865B6B3FFF6B
Writing [jobs_output.json] to current directory
Finished performing action
Cleaning up
Building services group                  args=[compilers]
Stopping container                            compilers-9afceba2-5ae5-43f4-8a92-74a0212e11a7
                                      timeout=0
Container stopped                             compilers-9afceba2-5ae5-43f4-8a92-74a0212e11a7
Removing container                            compilers-9afceba2-5ae5-43f4-8a92-74a0212e11a7
Removing dependent data container             compilers-98eda071-d5f7-45fa-baeb-7ea9875aef77
Getting data container situated           dir=outbound
Exporting data container                      deploy_tmp_
Copying out of container                      deploy_tmp_-2398f594-7bab-4d1b-8a74-d7536e7e3cb9
Move all files/dirs out of a dir and `rm -rf` that dir
                                         from=/tmp/deploy_tmp_044068169/.eris
                                           to=/tmp/deploy_tmp_044068169
Removing directory                            /tmp/deploy_tmp_044068169/.eris
Move all files/dirs out of a dir and `rm -rf` that dir
                                         from=/tmp/deploy_tmp_044068169
                                           to=/home/blockchain
Removing directory                            /tmp/deploy_tmp_044068169
Package path does not exist on the host or is inside the pkg path
PM files do not exist on the host or are inside the pkg path
Removing container                            deploy_tmp_-2398f594-7bab-4d1b-8a74-d7536e7e3cb9





[blockchain@bochk-eris-2 deploy]$ cat jobs_output.json 
{
  "defaultAddr": "13942CFA112D79F335C2B4272902AF6B35147F4A",
  "deployMyContract": "0B6767A54785C94D687FE040AA7C171AD15DA4D2",
  "solFile": "MyContract.sol"
}
```

2. Update abi in `test_app/app.js` from `deploy/abi/MyContract`
3. Copy `deploy/jobs_output.json` as `test_app/contractAddress.json` (actually only `deployMyContract` is required)
4. Copy `BlockchainApp.pem` and `BlockchainApp.key` from `~/.eris/chains/xxx_chain` as `test_app/app.pem` and `test_app/app.key`
4. `node app.js`
5. `./test.sh`
