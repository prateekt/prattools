import java.util.*;
import java.io.*;

public class ColChunker {

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

			//count lines in file
			BufferedReader r1 = new BufferedReader(new FileReader(fileName));
			int numLines =0;
			String[] headers = r1.readLine().split("\t");
			String line = "";
			while((line=r1.readLine())!=null) {
				numLines = numLines+1;
			}
			r1.close();
			System.out.println("Counted Lines in File: " + numLines);

			//read entire file into 2D String structure
			String[][] tbl = new String[numLines][headers.length];
			BufferedReader r = new BufferedReader(new FileReader(fileName));
			r.readLine(); //toss headers
			line = "";
			int cnt=0;
			while((line=r.readLine())!=null) {
				String[] lineVals = line.split("\t");
				for(int x=0; x < tbl[cnt].length; x++) {
					tbl[cnt][x] = lineVals[x];
				}
				cnt++;
				if((cnt%1000)==0) {
					System.out.println(cnt);
				}
			}
			r.close();

			//Print files
			int chunksWritten=0;
			File f = new File(outDir);
			f.mkdir();
			for(int x=0; x < headers.length; x=x+((int)colsPerChunk)) {

				//write chunk to file
				String file = outDir + "/CHUNK" + chunksWritten + ".txt";
				PrintWriter w = new PrintWriter(new FileWriter(file, true));

				//write headers
				String headersToWrite = "";
				for(int z=x; z < (x+((int)colsPerChunk)) && z < headers.length; z++) {
					headersToWrite += headers[z] + "\t";
				}				
				headersToWrite = headersToWrite.trim();
				w.println(headersToWrite);

				//write column data
				for(int y=0; y < tbl.length; y++) {
					line="";	
					for(int z=x; z < (x+((int)colsPerChunk)) && z < headers.length; z++) {
						line += tbl[y][z];
						if(z!=(x+colsPerChunk-1) && z!=headers.length-1) {
							line += "\t";
						}
					}
					w.println(line);
				}
				w.flush();
				w.close();
				chunksWritten++;
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
