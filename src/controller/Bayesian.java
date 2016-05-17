package controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import Jama.Matrix;
import model.DataClass;

public class Bayesian {

	private double testCount;
	private double accuracy;
	private ArrayList<DataClass> classes;
	
	public void createTrainingSet(double[][] data, HashSet<Double> class_values, String type){
		classes = new ArrayList<DataClass>();
		testCount = 0.0;
		accuracy = 0.0;
		
		Iterator it = class_values.iterator();
		
		for(int i=0; i<class_values.size(); i++){
			if(it.hasNext()){
				DataClass c = new DataClass();
				c.set_class((double) it.next());
				classes.add(c);
			}			
		}
		/*
		 *  Separate data into their respective class lists.
		 */		
			
		
		for(DataClass c: classes){
			for(double[] x: data){
				if(x[x.length-1] == c.get_class()){
					c.addData(x);
				}
			}
			c.setMean();
								
			double[][] mat = new double[data[0].length-1][data[0].length-1];
			
			int length = data[0].length-1;
			
			double[] m = c.getMean();
			for(double[] x: data){
				if(x[x.length-1] == c.get_class()){
			
					for(int i=0; i<length; i++){
						for(int j=0; j<length; j++){
							if(type.equals("Optimal") || type.equals("Linear")){
								mat[i][j] += (x[i] - m[i]) * (x[j] - m[j]);
							}							
						}
						if(type.equals("Naive")){
							mat[i][i] += (x[i] - m[i]) * (x[i] - m[i]);
						}
					}
				}
			}
			
			for(int i=0; i<length; i++){
				for(int j=0; j<length; j++){
					if(type.equals("Optimal") || type.equals("Linear")){
						mat[i][j] /= data.length;
					}
				}
				if(type.equals("Naive")){
					mat[i][i] /= data.length;
				}
			}
			
			Matrix A = new Matrix(mat);
			c.setCovariance(A);
		}		
	}
	
	public void pairwise(double[][] data, String type){
		double result = 0;
		
		for(double[] x: data){			
			Matrix A = classes.get(0).getCovariance();
			Matrix B = classes.get(1).getCovariance();
			
			Matrix D = A.plus(B);
			D.timesEquals(0.5);			
			
			double[][] sub_A = subtract_Mat(x, classes.get(0).getMean());
			double[][] sub_B = subtract_Mat(x, classes.get(1).getMean());
			
			Matrix A_diff = new Matrix(sub_A);
			Matrix B_diff = new Matrix(sub_B);
						
			Matrix test = new Matrix(new double[1][1]);
			if(type.equals("Linear")){
				result = Math.log(D.det()) - Math.log(D.det());
				test = (B_diff.transpose().times(D.inverse().times(B_diff))).minus(A_diff.transpose().times(D.inverse().times(A_diff)));
			}else{			
				result = Math.log(B.det()) - Math.log(A.det());		
				test = (B_diff.transpose().times(B.inverse().times(B_diff))).minus(A_diff.transpose().times(A.inverse().times(A_diff)));
			}
			result += test.get(0, 0);
			
			for(int i=0; i<classes.size()-2; i++){
				if (result < 0.0) {
					A = classes.get(i+2).getCovariance();
					sub_A = subtract_Mat(x, classes.get(i+2).getMean());
					A_diff = new Matrix(sub_A);
				}else{
					B = classes.get(i+2).getCovariance();
					sub_B = subtract_Mat(x, classes.get(i+2).getMean());
					B_diff = new Matrix(sub_B);
				}
				
				D = A.plus(B);
				D.timesEquals(0.5);
				
				test = new Matrix(new double[1][1]);
				if(type.equals("Linear")){
					result = Math.log(D.det()) - Math.log(D.det());
					test = (B_diff.transpose().times(D.inverse().times(B_diff))).minus(A_diff.transpose().times(D.inverse().times(A_diff)));
				}else{
					result = Math.log(B.det()) - Math.log(A.det());
					test = (B_diff.transpose().times(B.inverse().times(B_diff))).minus(A_diff.transpose().times(A.inverse().times(A_diff)));
				}
				result += test.get(0,0);
			}
			
			if(result < 0){
				for(DataClass c: classes){
					if(B == c.getCovariance()){
						if(x[x.length-1] == c.get_class()){
							accuracy += 100;
						}else{
							accuracy += 0;
						}
						testCount++;
					}
				}
			}else{
				for(DataClass c: classes){
					if(A == c.getCovariance()){
						if(x[x.length-1] == c.get_class()){
							accuracy += 100;
						}else{
							accuracy += 0;
						}
						testCount++;
					}
				}
			}
		}
	}
	
	public double getAccuracy(){
		return accuracy/testCount;
	}
	
	public double[][] subtract_Mat(double[] x, double[] m){
		double[][] temp = new double[x.length-1][1];
		
		for(int i=0; i<x.length-1; i++){
			temp[i][0] = x[i] - m[i];
		}
		return temp;
	}
}
