package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class ReadFile {
	
	public double[][] read(String filename){
		String csvFile = "/home/daniel/Desktop/Artificial Intel/Classification/src/controller/" + filename;
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		ArrayList<String[]> values = new ArrayList<String[]>();
		double[] line_nums;
		double[][] result;
		
		try {
			int i = 0;
			br = new BufferedReader(new FileReader(csvFile));
			
			while ((line = br.readLine()) != null) {
				i++;
			        // use comma as separator
				String[] heartDisease = line.split(cvsSplitBy);
				values.add(heartDisease);
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
		ArrayList<String> iris_type = new ArrayList<String>();
		
		if(filename.equals("Datasets/heartDisease.csv")){
			result = new double[values.size()][values.get(0).length];
			for(int i=0; i<values.size(); i++){
				for(int j=0; j<values.get(0).length; j++){
					result[i][j] = Double.parseDouble(values.get(i)[j]);
				}
			}			
		}else if(filename.equals("Datasets/iris.csv")){
			result = new double[values.size()][values.get(0).length];
			for(String[] x: values){
				if(!iris_type.contains(x[x.length-1])){
					iris_type.add(x[x.length-1]);
				}
			}
			
			for(int i=0; i<values.size(); i++){
				for(int j=0; j<values.get(0).length; j++){
					for(int k=0; k<3; k++){
						if(iris_type.get(k).equals(values.get(i)[values.get(i).length-1])){
							if((j + 1) == values.get(0).length){
								result[i][j] = (double) k+1;
							}else{
								result[i][j] = Double.parseDouble(values.get(i)[j]);
							}
						}						
					}
				}
			}
		}else{
			result = new double[values.size()][values.get(0).length];
			for(int i=0; i<values.size(); i++){
				for(int j=1; j<values.get(0).length; j++){
					result[i][j-1] = Double.parseDouble(values.get(i)[j]);
					result[i][j] = Double.parseDouble(values.get(i)[0]);
				}
			}
		}
		
		return result;
	  }	
}
