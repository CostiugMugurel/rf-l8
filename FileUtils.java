package rf8;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils 
{
	private static final String inputFileValuesSeparator = " ";
	private static final String outputFileValuesSeparator = ",";
	
	protected static ArrayList< double [] > centruDeGreutate (double[][] learningSet)
	{
		ArrayList< double [] > vct = new ArrayList<double[]>();
		double[] firstOne = new double[4];
		firstOne[0] = learningSet[0][0];
		firstOne[1] = learningSet[0][1];
		firstOne[2] = learningSet[0][2];
		firstOne[3] = 1;
		vct.add(firstOne);

		for(int i=1;i<learningSet.length;i++)
		{
			double[] temp = new double[4];
			temp[0] = learningSet[i][0];
			temp[1] = learningSet[i][1];
			temp[2] = learningSet[i][2];
			temp[3] = 1;
			
			int gasit = 0;
			
			for(int j=0;j<vct.size();j++)
			{
				if(temp[2] == vct.get(j)[2])
				{
					double[] temp2 = new double[4]; 
					temp2 = vct.get(j);
					temp2[0] += temp[0];
					temp2[1] += temp[1];
					temp2[3] +=1;
					vct.set(j, temp2);
					gasit = 1;
				}
			}
			
			if(gasit == 0)
			{
				vct.add(temp);
			}
		}
		
		for(int i=0;i<vct.size();i++)
		{
			double[] newTemp = new double[3];
			double[] oldTemp = new double[4];
			oldTemp = vct.get(i);
			newTemp[0] = oldTemp[0] / oldTemp[3];
			newTemp[1] = oldTemp[1] / oldTemp[3];
			newTemp[2] = oldTemp[2];
			vct.set(i, newTemp);
		}
		
		return vct;
	}
	
	
	protected static double[][] readLearningSetFromFile(String fileName) throws Exception
	{
		//Start with an ArrayList<ArrayList<Double>>
		List<ArrayList<Double>> learningSet = new ArrayList<ArrayList<Double>>();
		// read file into stream, try-with-resources
		try  {
			Stream<String> stream = Files.lines(Paths.get(fileName));
			learningSet = stream.map(FileUtils::convertLineToLearningSetRow).collect(Collectors.toList());
		} 
		catch (FileNotFoundException fnfe)
		{
			throw new Exception(" We cannot find the scepicified file on USV computer");
		}	
		catch (IOException ioe) {
			throw new Exception(" We encountered some errors while trying to read the specified file: " + ioe.getMessage());
		}
		catch (Exception e) {
			throw new Exception(" Other errors: " + e.getMessage());
		}	
		//  convert ArrayList<ArrayList<Double>> to double[][] for performance
		return convertToBiDimensionalArray(learningSet);
	}
	
	private static double[][] convertToBiDimensionalArray(List<ArrayList<Double>> learningSet) {
		
		double[][] learningSetArray = new double[learningSet.size()][];
		
		for (int n = 0; n < learningSet.size(); n++) {
			ArrayList<Double> rowListEntry = learningSet.get(n);
			
			// get each row double values
			double[] rowArray = new double[learningSet.get(n).size()];
			
			for (int p = 0; p < learningSet.get(n).size(); p++) 
			{				
				rowArray[p] = rowListEntry.get(p);
			}
			learningSetArray[n] = rowArray;
			
		}
		return learningSetArray;
	}
	
	private static ArrayList<Double> convertLineToLearningSetRow(String line)
	{
		ArrayList<Double> learningSetRow = new ArrayList<Double>();
		String[] stringValues = line.split(inputFileValuesSeparator);
		//we need to convert from string to double
		for (int p = 0; p < stringValues.length; p++)
		{
			learningSetRow.add(Double.valueOf(stringValues[p]));
		}
		return learningSetRow;
	}
	
	protected static void writeLearningSetToFile(String fileName, double[][] normalizedSet)
	{
		// first create the byte array to be written
		StringBuilder stringBuilder = new StringBuilder();
		for(int n = 0; n < normalizedSet.length; n++) //for each row
		{
			//for each column
			 for(int p = 0; p < normalizedSet[n].length; p++) 
			 {
				//append to the output string
				 stringBuilder.append(normalizedSet[n][p]+"");
				 //if this is not the last row element
			      if(p < normalizedSet[n].length - 1)
			      {
			    	  //then add separator
			    	  stringBuilder.append(outputFileValuesSeparator);
			      }
			 }
			//append new line at the end of the row
			 stringBuilder.append("\n"); 
		}
		try {
			Files.write(Paths.get(fileName), stringBuilder.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
