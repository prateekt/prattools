#!/bin/sh
noHeaderFile=$1
withHeaderFile=$2

head -n 1 $withHeaderFile
cat $noHeaderFile
