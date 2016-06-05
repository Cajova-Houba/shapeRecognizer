#!/bin/bash

paths=("src/*" "data/*" "pom.xml" "gitAddAll.sh")

for item in ${paths[*]}
do
	printf "git add --all %s\n" $item
	git add --all $item
done