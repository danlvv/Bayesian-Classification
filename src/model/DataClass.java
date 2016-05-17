package model;

import java.util.ArrayList;

import Jama.Matrix;

public class DataClass {

	private double class_value;
	private double[] mean;
	private Matrix covariance;
	private ArrayList<double[]> dataList;
	
	public void set_class(double value){
		class_value = value;
		dataList = new ArrayList<double[]>();
	}
	
	public double get_class(){
		return class_value;
	}
	
	public void addData(double[] x){
		dataList.add(x);
	}
	
	public void setMean(){
		mean = new double[dataList.get(0).length];
		for(int i=0; i<dataList.get(0).length-1; i++){
			double sum = 0;
			double n = 0;
			
			for(double[] x: dataList){
				sum += x[i];
				n++;
			}
			
			mean[i] = sum/n;
		}	
		
//		System.out.print("[");
//		for(int i=0; i<mean.length-1; i++){
//			System.out.print(mean[i] + ", ");
//		}
//		System.out.print(mean[mean.length-1]);
//		System.out.println("]");
	}
	
	public double[] getMean(){
		return mean;
	}
	
	public void setCovariance(Matrix cov){
		covariance = cov;
	}
	
	public Matrix getCovariance(){
		return covariance;
	}
}
