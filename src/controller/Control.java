package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

public class Control {
	
	public static void main(String[] args){
		double[][] data = null;
		ReadFile reader = new ReadFile();
		HashSet<Double> classes = new HashSet<Double>();
		
		for(int i=0; i<3; i++){
			classes = new HashSet<Double>();
			if(i==0){
				data = reader.read("Datasets/heartDisease.csv");
				for(double[] x: data){
					classes.add(x[x.length-1]);
				}
			}else if(i==1){
				data = reader.read("Datasets/iris.csv");
				for(double[] x: data){
					classes.add(x[x.length-1]);
				}
			}else{
				data = reader.read("Datasets/wine.csv");
				for(double[] x: data){
					classes.add(x[x.length-1]);
				}
			}
			
			/*
			 * Scramble data list.
			 */
			int length = data.length;
			ArrayList<double[]> shuffler = new ArrayList<double[]>();
			for(double[] x: data){
				shuffler.add(x);
			}
			Collections.shuffle(shuffler);
			for(int j=0; j<shuffler.size(); j++){
				data[j] = shuffler.get(j);
			}
			
			assert(length == data.length);
			
			/*
			 * Separate data into their respective classes;
			 */
			
			ArrayList<double[][]> class_lists = new ArrayList<double[][]>();
			
			for(double c: classes){
				int count = 0;
				for(double[] x: data){
					if(x[x.length-1] == c){
						count++;
					}
				}
				
				double[][] temp = new double[count][data[0].length];
				count = 0;
				
				for(double[] x: data){
					if(x[x.length-1] == c){
						temp[count++] = x;
					}
				}
				class_lists.add(temp);
			}
			
			/*
			 *  Split the five data classes into a testing set and a training set.
			 */
			double OB_Acc = 0;
			double NB_Acc = 0;
			double LB_Acc = 0;			
			
			int fold = 10;

			System.out.println("10 fold cross validation ---->");
			System.out.println("");
			System.out.println("");
			
			for(int k=0; k<fold; k++){
				double[][] testingSet = new double[data.length/10][data[0].length];
				double[][] trainingSet = new double[((9*data.length)/10 + 2)][data[0].length];
							
				int testCount = 0;
				int trainCount = 0;
							
				for(double[][] list: class_lists){
					for(int j=0; j<list.length; j++){
						if(j >= (k*(list.length/10)) && j < (k+1)*(list.length/10)){//j >= (list.length/10)){
							testingSet[testCount++] = list[j];
						}else{
							trainingSet[trainCount++] = list[j];
						}
					}
				}
				
				
				/*
				 * Run the tests. 
				 */
			
				//	Optimal Bayesian
				
				Bayesian bayes = new Bayesian();
				bayes.createTrainingSet(trainingSet, classes, "Optimal");
				bayes.pairwise(testingSet, "Optimal");
				
				OB_Acc += bayes.getAccuracy();
				
				//	Naive Bayesian
				
				bayes.createTrainingSet(trainingSet, classes, "Naive");
				bayes.pairwise(testingSet, "Naive");
				
				NB_Acc += bayes.getAccuracy();
			
				//	Linear Bayesian
				
				bayes.createTrainingSet(trainingSet, classes, "Linear");
				bayes.pairwise(testingSet, "Linear");
				
				LB_Acc += bayes.getAccuracy();				
			}
			
			System.out.println("Accuracy of the OPTIMAL BAYESIAN CLASSIFIER: ");
			System.out.println("" + OB_Acc/fold);
			System.out.println("");

			System.out.println("Accuracy of the NAIVE BAYESIAN CLASSIFIER: ");
			System.out.println("" + NB_Acc/fold);
			System.out.println("");
			
			System.out.println("Accuracy of the LINEAR BAYESIAN CLASSIFIER: ");
			System.out.println("" + LB_Acc/fold);
			System.out.println("");
			
			System.out.println("--------------------------------------------------");
			System.out.println("");
			System.out.println("");
			
			System.out.println("Leave one out --->");
			System.out.println("");
			System.out.println("");
			
			OB_Acc = 0;
			NB_Acc = 0;
			LB_Acc = 0;
			
			for(int k=0; k<data.length; k++){
				double[][] testingSet = new double[1][data[0].length];
				double[][] trainingSet = new double[data.length][data[0].length];
							
				int testCount = 0;
				int trainCount = 0;
							
				for(double[][] list: class_lists){
					for(int j=0; j<list.length; j++){
						if(j == k){
							testingSet[0] = list[j];
						}else{
							trainingSet[trainCount++] = list[j];
						}
					}
				}
				
				
				/*
				 * Run the tests. 
				 */
			
				//	Optimal Bayesian
				
				Bayesian bayes = new Bayesian();
				bayes.createTrainingSet(trainingSet, classes, "Optimal");
				bayes.pairwise(testingSet, "Optimal");
				
				OB_Acc += bayes.getAccuracy();
				
				//	Naive Bayesian
				
				bayes.createTrainingSet(trainingSet, classes, "Naive");
				bayes.pairwise(testingSet, "Naive");
				
				NB_Acc += bayes.getAccuracy();
			
				//	Linear Bayesian
				
				bayes.createTrainingSet(trainingSet, classes, "Linear");
				bayes.pairwise(testingSet, "Linear");
				
				LB_Acc += bayes.getAccuracy();
			}
			
			System.out.println("Accuracy of the OPTIMAL BAYESIAN CLASSIFIER: ");
			System.out.println("" + OB_Acc/data.length);
			System.out.println("");

			System.out.println("Accuracy of the NAIVE BAYESIAN CLASSIFIER: ");
			System.out.println("" + NB_Acc/data.length);
			System.out.println("");
			
			System.out.println("Accuracy of the LINEAR BAYESIAN CLASSIFIER: ");
			System.out.println("" + LB_Acc/data.length);
			System.out.println("");
			
			System.out.println("--------------------------------------------------");
			System.out.println("");
			System.out.println("");

		}
	}
}
