import java.io.*;
import java.util.*;

public class AddRows {
	
	public static void main(String[] args) {

		try {
			if(args.length!=5) {
				System.out.println("USAGE: java AddRows [file1] [file2] [file1Cols] [file2Cols] [outFile]");
				return;
			}
			else {
				
				//extract input
				String file1 = args[0];
				String file2 = args[1];
				String file1ColsArg = args[2];
				String file2ColsArg = args[3];
				String outFile = args[4];

				//debug print
				System.out.println();
				System.out.println("---------------------------------------------------");
				System.out.println("Adding Rows from " + file1 + " and " +file2+" to " + outFile + ".");
				System.out.println();
				
				//expand cols
				List<Integer> f1Cols = Utils.expandCols(file1ColsArg);
				List<Integer> f2Cols = Utils.expandCols(file2ColsArg);
				if(f1Cols.size()!=f2Cols.size()) {
					System.out.println("SYNTAX ERROR: Concat operation for " + file1 + " has " + f1Cols.size() + 
								" cols, while " + file2 + " has " + f2Cols.size() + " cols.");
					return;
				}

				//write file
				PrintWriter w = new PrintWriter(new FileWriter(outFile),true);
				int written = addFileToStream(file1,f1Cols,w,true) - 1;
				written+= addFileToStream(file2,f2Cols,w,false);
				w.flush();
				w.close();
				
				//report
				System.out.println("Total columns written to " + outFile + ": " + f1Cols.size());
				System.out.println("Total rows written to " + outFile + ": " + written);
				System.out.println("---------------------------------------------------");
				
				

			}


		}
		catch(Exception e) {
			e.printStackTrace();
		}		

	}

	public static int addFileToStream(String inFile, List<Integer> cols, PrintWriter w, boolean includeHeader) throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(inFile));
		String line="";
		if(!includeHeader) {
			r.readLine();
		}
		int c=0;
		while((line=r.readLine())!=null) {
			String[] lineCols = line.split("\t");
			String lineToWrite = "";
			for(int x=0; x < cols.size(); x++) {
				if(x==cols.size()-1)
					lineToWrite += lineCols[cols.get(x)];
				else
					lineToWrite += lineCols[cols.get(x)] + "\t";
			}
			w.println(lineToWrite);
			c++;
		}
		r.close();
		return c;
	} 
}
