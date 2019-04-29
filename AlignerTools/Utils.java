import java.util.*;

public class Utils {

	public static List<Integer> expandCols(String colsStr) {

		//create list of cols mentioned in string
		List<Integer> set1 = new ArrayList<Integer>();
		String[] colElem = colsStr.split(",");
		for(int x=0; x < colElem.length; x++) {
			if(colElem[x].indexOf(":") > -1) {
				String[] rng = colElem[x].split(":");
				int rngStart = Integer.parseInt(rng[0]);
				int rngFin = Integer.parseInt(rng[1]);
				for(int y=rngStart; y <= rngFin; y++) {
					set1.add(y);
				}
			}
			else {
				int index = Integer.parseInt(colElem[x]);
				set1.add(index);
			}
		}

		//convert to sorted arraylist -- DO NOT SORT
		List<Integer> rtn = new ArrayList<Integer>(set1);
//		Collections.sort(rtn);
		return rtn;
	}	

}
