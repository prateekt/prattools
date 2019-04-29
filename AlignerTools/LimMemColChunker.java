import java.util.*;
import java.io.*;

public class LimMemColChunker {
	
	public static void main(String[] args) {
		try {
			//check args
			if(args.length!=3) {
				System.out.println("USAGE: java ColChunker [fileName] [colsPerChunk] [outDir]");
				return;
			}

			//parse input
			String fileName = args[0];
			double colsPerChunk = Double.parseDouble(args[1]);
			String outDir = args[2];
			Process p = Runtime.getRuntime().exec("rm -r " + outDir);
			p.waitFor();

			//debug print
			System.out.println();
			System.out.println("---------------------------------------------------");
			System.out.println("Chunking " + fileName + " into chunks of " + colsPerChunk + " in " + outDir);
			System.out.println();

			//count cols in file
			BufferedReader r1 = new BufferedReader(new FileReader(fileName));
			int numCols = r1.readLine().split("\t").length;
			r1.close();
			System.out.println("Counted Columns in file in File: " + numCols);

			//set up writers for each col chunk
			int chunksWritten=0;
			File f = new File(outDir);
			f.mkdir();
			FileWriter[] writers = new FileWriter[(int)Math.ceil(numCols/colsPerChunk)];
			File[] wfiles = new File[writers.length];
			for(int x=0; x < writers.length; x++) {

				//set up writer for col chunk
				String file = outDir + "/CHUNK" + chunksWritten + ".txt";
				wfiles[x] = new File(file);
				writers[x] = new FileWriter(wfiles[x],true);
				//writers[x] = new PrintWriter(new FileWriter(file),true);
				chunksWritten++;
			}

			//iterate through master file, writing each line with appropriate writers
			BufferedReader r = new BufferedReader(new FileReader(fileName));
			//r.readLine(); //toss headers
			String line = "";
			int cnt =0;
			while((line=r.readLine())!=null) {
				String[] lineVals = line.split("\t");
				int colIndex=0;
				for(int x=0; x < writers.length; x++) {
					
					String lineToWrite = "";
					for(int y=0; y  < ((int)colsPerChunk) && colIndex < numCols; y++) {
						lineToWrite = lineToWrite + lineVals[colIndex] + "\t";
						colIndex++;
					}
					writers[x].append(lineToWrite.substring(0,lineToWrite.length()-1) + "\n");
					writers[x].flush();

				}
				cnt++;
				if((cnt%1000)==0) {
					System.out.println(cnt);
					for(int x=0; x < writers.length; x++) {
						writers[x].flush();
						writers[x].close();
						writers[x] = new FileWriter(wfiles[x],true);
					}
				}
			}
			r.close();

			//flush and close all writers
			for(FileWriter w : writers) {
				w.flush();
				w.close();
			}

			//validation print out
			System.out.println("Wrote " + chunksWritten + " chunks to " + outDir);
			System.out.println("---------------------------------------------------");			


		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}
	
}
