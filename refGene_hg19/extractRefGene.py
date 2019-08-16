import sys
import os
import gzip

#column names
colNames = ['bin','name','chrom','strand','txStart','txEnd','cdsStart','cdsEnd','exonCount','exonStarts','exonEnds','score','name2','cdsStartStat','cdsEndStat','exonFrames']

#out files
genes = open('genes.bed','w')
exons = open('exons.bed','w')

#chr accept
chrAccept = set()
for i in range(0,23):
	chrAccept.add('chr'+str(i))
chrAccept.add('chrX')
chrAccept.add('chrY')

#ref gene
refGene = gzip.open('refGene.txt.gz','r')
nameCol = colNames.index('name2')
chrCol = colNames.index('chrom')
geneStartCol = colNames.index('txStart')
geneEndCol = colNames.index('txEnd')
exonStartsCol = colNames.index('exonStarts')
exonEndsCol = colNames.index('exonEnds')
for line in refGene:
	line = line.decode('UTF-8')
	line = line.strip()
	lineParts = line.split('\t')
	geneName = lineParts[nameCol]
	chrs = lineParts[chrCol]
	if(chrs not in chrAccept):
		continue
	geneStart = int(lineParts[geneStartCol])
	geneEnd = int(lineParts[geneEndCol])
	exonStarts = lineParts[exonStartsCol].split(',')
	exonStarts = exonStarts[:-1]
	exonEnds = lineParts[exonEndsCol].split(',')
	exonEnds = exonEnds[:-1]
	newGeneLine = '\t'.join([chrs,str(geneStart),str(geneEnd),geneName])
	genes.write(newGeneLine+'\n')
	for e in range(0,len(exonStarts)):
		exonStart = exonStarts[e]
		exonEnd = exonEnds[e]
		newExonLine = '\t'.join([chrs,str(exonStart),str(exonEnd),geneName])
		exons.write(newExonLine+'\n')

#sortbed
os.system('sortBed2 genes.bed > genes_sorted.bed')
os.system('sortBed2 exons.bed > exons_sorted.bed')

#close
refGene.close()
genes.close()
exons.close()