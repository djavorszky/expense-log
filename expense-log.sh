#!/bin/bash

if [[ $# -eq 0 ]]; then
	echo "Example usage:"
	echo "./expense-log.sh -c add-expense -a [amount] -ct category [-d date]"
	echo "./exepense-log.sh -c list"
else 
	java -cp ./bin/production/expense-log com.expenselog.Main "$@"
fi
