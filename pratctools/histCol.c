#include <stdio.h>
#include <string.h>
#include <stdlib.h> 
#include <math.h> 
#include <string.h>

#define BUFFER_SIZE 1000

int main( int argc, char *argv[]) {
	if(argc!=7) {
		printf("USAGE: ./histCol [in_file] [col_num] [min_val] [max_val] [step_size] [out_file] \n");
	}
	else {

		//read params
		int colNum = atol(argv[2]);
		float minVal = atof(argv[3]);
		float maxVal = atof(argv[4]);
		float stepSize = atof(argv[5]);

		//set up histogram array
		int numBins = (int)(ceil((maxVal-minVal) / stepSize));
		int hist[numBins];
		for(int x=0; x < numBins; x++) {
			hist[x] = 0;
		}

		//read in file line by line and histogram
		char line[BUFFER_SIZE];
		FILE* file = fopen(argv[1],"r");
		if(file == NULL) {
			perror("Error opening file.");
			return -1;
		}
		int linenum = 0;
		while(fgets(line,sizeof line, file) != NULL) {
			
			//get current value
			char* colVal = strtok(line,"\t");
			int c=0;
			while(c < colNum) {
				colVal = strtok(NULL,"\t");
				c++;
			}
			float val = atof(colVal);

			//histogram index
			int histIndex = (int) (floor((val-minVal)/stepSize));
			if(histIndex < 0) {
				histIndex = 0;
			}
			if(histIndex >= numBins) {
				histIndex = numBins -1;
			}
			hist[histIndex] = hist[histIndex] + 1;

			//ctr
			/*
			if(linenum%1000000==0 && linenum>0) {
				printf("%d\n",linenum);
				fflush(stdout);
			}
			linenum++;
			*/
		}

		//open out file
		FILE* outFile = fopen(argv[6],"w");
		if(outFile == NULL) {
			perror("Error opening file.");
			return -1;
		}

		//output histogram
		for(int x=0; x < numBins; x++) {
			fprintf(outFile,"%f\t%d\n",(minVal+x*stepSize),hist[x]);
		}
		fflush(outFile);

		//close file
		fclose(outFile);

	}
}
