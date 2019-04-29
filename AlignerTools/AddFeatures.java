import java.io.*;
import java.util.*;

public class AddFeatures {
	
	public static void main(String[] args) {
		try {
			if(args.length!=8) {
				System.out.println("USAGE: java AddFeatures [file_master] [file_align] [hash_master_ints] [hash_align_ints] [align_cols_add] [prefixToAdd] [out_file] [dropped_file]");
			}
			else {
				
				//extract input
				String file_master = args[0];
				String file_align = args[1];
				String hash_master_ints = args[2];
				String hash_align_ints = args[3];
				String align_cols_add = args[4];
				String prefixToAdd = args[5];
				String out_file = args[6];
				String dropped_file = args[7];
				
				//debug print
				System.out.println();
				System.out.println("---------------------------------------------------");
				System.out.println("Adding Features from " + file_align + " to " + file_master + ".");
				System.out.println();
			
				//parse input
				List<Integer> masterHashCols = Utils.expandCols(hash_master_ints);
				List<Integer> alignHashCols = Utils.expandCols(hash_align_ints);
				List<Integer> alignAddCols = Utils.expandCols(align_cols_add);
				if(masterHashCols.size()!=alignHashCols.size()) {
					System.out.println("ALIGNMENT SYNTAX ERROR: " + file_master + " has " + masterHashCols.size()
 							+ " cols, while " + file_align + " has " + alignHashCols.size() + " cols.");
					return;
				}

				//create hash map for align file
				//deal with headers, then rest of file.
				HashMap<String,String> alignHash = new HashMap<String,String>();
				BufferedReader r = new BufferedReader(new FileReader(file_align));
				String line=r.readLine();
				String[] headersAlign = line.split("\t");
				String HEADERS_ALIGN = "";
				for(int x=0; x < alignAddCols.size(); x++) {
					HEADERS_ALIGN += "\t" + prefixToAdd + headersAlign[alignAddCols.get(x)];
				}
				while((line=r.readLine())!=null) {					
					String[] toks = line.split("\t",-1);
					//System.out.println(line);
					//System.out.println("NUM TOKS: " + toks.length);
					//for(int y=0; y < toks.length; y++) {
					//	System.out.println(toks[y]);
					//}

					String hash = "";
					for(int x=0; x < alignHashCols.size(); x++) {
						hash = hash + toks[alignHashCols.get(x)];
					}
					String colsToAdd = "";
					for(int x=0; x < alignAddCols.size(); x++) {
						colsToAdd = colsToAdd + "\t" + toks[alignAddCols.get(x)];
					}
					alignHash.put(hash,colsToAdd);

				}
				r.close();

				//create new file
				//print headers then rest of file
				BufferedReader r1 = new BufferedReader(new FileReader(file_master));
				line=r1.readLine();
				PrintWriter w = new PrintWriter(new FileWriter(out_file),true);
				w.println(line + HEADERS_ALIGN); //write headers to file
				PrintWriter wd = new PrintWriter(new FileWriter(dropped_file),true);
				wd.println(line); //write master headers to dropped line file
				int c=0;
				int droppedLines=0;
				int droppedLinesPF=0;
				String toWrite = "";
				
				//the only point here is to get the pass filters index		
				String[] headersMaster = line.split("\t");
				int passFiltersIndex=-1;
				for(int x=0; x < headersMaster.length; x++) {
					if(headersMaster[x].equalsIgnoreCase("pass_filters")) {
						passFiltersIndex = x;
						break;
					}
				}

				//loop
				while((line=r1.readLine())!=null) {
					String[] toks = line.split("\t");
					String hash="";
					for(int x=0; x < masterHashCols.size(); x++) {
						hash = hash + toks[masterHashCols.get(x)];
					}
					String colsToAdd = alignHash.get(hash);
					if(colsToAdd!=null) { //if no line in both files, don't write the line.
						toWrite = line + colsToAdd; 
						w.println(toWrite);
						c++;
					}
					else {
						//count total dropped lines
						droppedLines++;
						wd.println(line);
					
						//count total dropped lines that pass filters
						if(passFiltersIndex>-1) {
							int pf = Integer.parseInt(toks[passFiltersIndex]);
							if(pf==1)
								droppedLinesPF++;
						}
					}
			
				}
				r1.close();
				w.flush();
				w.close();
				wd.flush();
				wd.close();
				
				//validation print out
				String[] toks = toWrite.split("\t");
				System.out.println("Total columns written to " + out_file + ": " + toks.length);
				System.out.println("Total rows written to " + out_file + ": " + c);
				System.out.println("Total columns added: " + alignAddCols.size());
				System.out.println("Total rows dropped from "+file_master+ ": " + droppedLines);
				if(passFiltersIndex>-1)
					System.out.println("Total rows dropped that pass filters from " + file_master + ": " + droppedLinesPF);
				System.out.println("---------------------------------------------------");

			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}
}
