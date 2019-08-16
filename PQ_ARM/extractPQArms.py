#!/anaconda3/bin/python
import gzip
import sys
import os

#params
if(len(sys.argv)!=2):
	print("USAGE: python extractPQArms.py [REF_GENOME]")
	sys.exit()
refGenome = sys.argv[1]
if(refGenome=='hg19'):
	CYTOBAND_FILE = '/Users/ptandon/Desktop/tools/prattools/Hg1k_v37_fasta/cytoBand.txt.bgz'
elif(refGenome=='hg38'):
	CYTOBAND_FILE = '/Users/ptandon/Desktop/tools/prattools/Hg1k_v38_fasta/cytoBand.txt.gz'

#dict
QChrStarts = dict()
QChrEnds = dict()
PChrStarts = dict()
PChrEnds = dict()

#chr accept
chrList = list()
for i in range(1,23):
	chrList.append('chr'+str(i))
chrList.append('chrX')
chrList.append('chrY')

#loop
fin = gzip.open(CYTOBAND_FILE,'r')
for line in fin:

	#line
	line = line.decode('UTF-8')
	line = line.strip()
	lineParts = line.split('\t')
	chrs = lineParts[0]
	start = int(lineParts[1])
	end = int(lineParts[2])
	band = lineParts[3]

	#p,q
	if('q' in band):
		if(chrs not in QChrStarts):
			QChrStarts[chrs] = start
			QChrEnds[chrs] = end
		else:
			QChrStarts[chrs] = min(QChrStarts[chrs],start)
			QChrEnds[chrs] = max(QChrEnds[chrs],end)
	if('p' in band):
		if(chrs not in PChrStarts):
			PChrStarts[chrs] = start
			PChrEnds[chrs] = end
		else:
			PChrStarts[chrs] = min(PChrStarts[chrs],start)
			PChrEnds[chrs] = max(PChrEnds[chrs],end)
fin.close()

#output loop
p = open('P_ARM_'+refGenome+'.txt','w')
q = open('Q_ARM_'+refGenome+'.txt','w')
p.write('Chr\tStart\tEnd\n')
q.write('Chr\tStart\tEnd\n')
for chrs in chrList:
	newLine1 = chrs + '\t' + str(PChrStarts[chrs]) + '\t' + str(PChrEnds[chrs])
	newLine2 = chrs + '\t' + str(QChrStarts[chrs]) + '\t' + str(QChrEnds[chrs])
	p.write(newLine1+'\n')
	q.write(newLine2+'\n')
p.close()
q.close()

#generate ideograms
os.system('bed2Ideogram P_ARM_'+refGenome+'.txt P_ARM_'+refGenome+'.txt.html overlay "P-ARM ('+refGenome+')" '+refGenome)
os.system('bed2Ideogram Q_ARM_'+refGenome+'.txt Q_ARM_'+refGenome+'.txt.html overlay "Q-ARM ('+refGenome+')" '+refGenome)