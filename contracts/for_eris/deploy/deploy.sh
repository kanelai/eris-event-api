#!/bin/bash

if [ $# -ne 3 ]; then
	echo "Usage: deploy.sh <chain_name> <account_address> <sol_file>"
	echo "    chain_name:"
	echo "    account_address: The account keys with permission to deploy contract must exist in the local eris-keys container. i.e. The local node must be running with that account, or the account keys must be imported to eris-keys."
	echo "    sol_file: Name of the Solidity file to deploy (in the current dir)."
	exit 1
fi

chain_name=$1
account_address=$2
sol_file=$3

eris services start compilers
eris pkgs do --chain $chain_name --address $account_address --set "solFile="$sol_file -z -d -v
