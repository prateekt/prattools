#!/bin/sh
INFILE=$1
WIN_SIZE=201

#clean bed file
cleanBed $INFILE cleanedBed.txt dropDuringClean.txt

#generate windowed
genWindowedBed cleanedBed.txt $WIN_SIZE > windowedBed.txt

#generate .fa file (around window)
bedtools getfasta -fi ~/Desktop/GVCode/lib/Hg1k_v37_fasta/human_g1k_v37.fasta -bed windowedBed.txt -fo fafile.fa

#align to fa file
alignToFasta windowedBed.txt fafile.fa $WIN_SIZE alignedBed.txt alignmentErr.txt

