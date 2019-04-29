#!/bin/sh
INFILE=$1
OUTFILE=$2
DB=~/Desktop/annovar/humandb
table_annovar.pl $INFILE $DB -buildver hg19 -out $OUTFILE -remove -protocol refGene,cytoBand,genomicSuperDups,esp6500siv2_all,1000g2014oct_all,1000g2014oct_afr,1000g2014oct_eas,1000g2014oct_eur,snp138,ljb26_all -operation g,r,r,f,f,f,f,f,f,f -nastring 'N/A'

