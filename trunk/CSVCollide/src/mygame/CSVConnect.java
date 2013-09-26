
package mygame;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *Reads a csv and save it in a multidimensional array
 * @author GIOI Staff
 */
public class CSVConnect {
    String csvFile="";
    String[] singleLine = null;
    ArrayList<String[]> csvArray = null;
    
   
    public ArrayList<String[]> getCSVArray(){
        return this.csvArray;
    }
    
    CSVConnect(String path) {
     this.csvFile = path;     
     csvArray = new ArrayList<String[]>();
    }        
    
    public void run(){    
	BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";        
	try { 
		br = new BufferedReader(new FileReader(this.csvFile));
		while ((line = br.readLine()) != null) {
                        		       
			singleLine = line.split(cvsSplitBy); 
                        csvArray.add(singleLine); 
		}
 
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}    	
  }
}
