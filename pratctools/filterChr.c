#include <stdio.h>
#include <string.h>
#include <stdlib.h> 
#include <math.h> 
#include <string.h>

#define BUFFER_SIZE 1000

int accept(char* chrs) {
	if(strcmp(chrs,"chr1")==0)
		return 1;
	if(strcmp(chrs,"chr2")==0)
		return 2;
	if(strcmp(chrs,"chr3")==0)
		return 3;
	if(strcmp(chrs,"chr4")==0)
		return 4;
	if(strcmp(chrs,"chr5")==0)
		return 5;
	if(strcmp(chrs,"chr6")==0)
		return 6;
	if(strcmp(chrs,"chr7")==0)
		return 7;
	if(strcmp(chrs,"chr8")==0)
		return 8;
	if(strcmp(chrs,"chr9")==0)
		return 9;
	if(strcmp(chrs,"chr10")==0)
		return 10;
	if(strcmp(chrs,"chr11")==0)
		return 11;
	if(strcmp(chrs,"chr12")==0)
		return 12;
	if(strcmp(chrs,"chr13")==0)
		return 13;
	if(strcmp(chrs,"chr14")==0)
		return 14;
	if(strcmp(chrs,"chr15")==0)
		return 15;
	if(strcmp(chrs,"chr16")==0)
		return 16;
	if(strcmp(chrs,"chr17")==0)
		return 17;
	if(strcmp(chrs,"chr18")==0)
		return 18;
	if(strcmp(chrs,"chr19")==0)
		return 19;
	if(strcmp(chrs,"chr20")==0)
		return 20;
	if(strcmp(chrs,"chr21")==0)
		return 21;
	if(strcmp(chrs,"chr22")==0)
		return 22;
	/*
	if(strcmp(chrs,"chrX")==0)
		return 23;
	if(strcmp(chrs,"chrY")==0)
		return 24;
	*/
	return -1;
}

int main( int argc, char *argv[]) {
	if(argc!=4) {
		printf("USAGE: ./filterChr [in_file] [chrCol] [out_file] \n");
	}
	else {

		//read params
		int colNum = atol(argv[2]);

		//open out file
		FILE* outFile = fopen(argv[3],"w");
		if(outFile == NULL) {
			perror("Error opening file.");
			return -1;
		}

		//read in file line by line and filter chr
		char line[BUFFER_SIZE];
		char line2[BUFFER_SIZE];
		FILE* file = fopen(argv[1],"r");
		if(file == NULL) {
			perror("Error opening file.");
			return -1;
		}
		int linenum = 0;
		while(fgets(line,sizeof line, file) != NULL) {
			
			//get current value
			strcpy(line2,line);
			char* colVal = strtok(line,"\t");
			int c=0;
			while(c < colNum) {
				colVal = strtok(NULL,"\t");
				c++;
			}

			//if accept, print line
			int chr = accept(colVal);
			if(chr!=-1) {
				fprintf(outFile,"%s",line2);
//				fflush(outFile);
			}

			//ctr
			/*
			if(linenum%1000000==0 && linenum>0) {
				printf("%d\n",linenum);
				fflush(stdout);
			}*/
			linenum++;			

		}

		//close file
		fclose(outFile);

	}
}
