#!/bin/sh
filename=$1
delimiter=$2
if test $delimiter = 1; then
	awk -F',' ' { print NF;exit }' $filename
else
	awk -F'\t' ' { print NF;exit }' $filename
fi

