package rf8;

public class ClassificationUtils 
{
	protected static int[] classifySet(double[][] entrySet, int numberOfClasses, int numberOfFeatures)
	{
		double[][] kernels = new double[numberOfClasses][numberOfFeatures];
		int[] iClass = new int[entrySet.length];

		boolean done = false;
		
		for (int i = 0 ; i< numberOfClasses; i++)
		{
			kernels[i] = initializeKernel(entrySet[i]);
		}
				
		while (!done)
		{
			done = true;
			double[][] centersOfGravity = new double[numberOfClasses][numberOfFeatures];
			int[] patternsCount = new int[numberOfClasses];
			int kMin = 0;
			for (int i = 0 ; i< entrySet.length; i++)
			{
				double minDistance = Double.MAX_VALUE;				
				for (int k = 0; k < numberOfClasses; k++)
				{
					double distanceToKernel = calculateDistance(entrySet[i], kernels[k]);
					if (distanceToKernel < minDistance)
					{
						minDistance = distanceToKernel;
						kMin = k;
					}
				}
				patternsCount[kMin]++;
				for (int j = 0 ; j < numberOfFeatures; j++)
				{
					centersOfGravity[kMin][j] += entrySet[i][j];
				}
				if (iClass[i] != kMin)
				{
					// we wnat to number the classes from 1
					iClass[i] = kMin;
					done = false;
				}
			}
			if (!done)
			{
				for (int k = 0; k < numberOfClasses; k++)
				{
					for (int j = 0 ; j < numberOfFeatures; j++)
					{
						centersOfGravity[k][j] /= patternsCount[k];
					}
					kernels[k] = updateKernel(entrySet, centersOfGravity[k]);
				}
				
			}
		}
		return iClass;
	}
	

	
	private static double[] initializeKernel(double[] initialValues)
	{
		double[] kernel = new double[initialValues.length];
		
		for (int i = 0; i<initialValues.length; i++)
		{
			kernel[i] = initialValues[i];
		}
		return kernel;
	}
	
	private static double calculateDistance(double[] currentPattern, double[] kernel)
	{
		double sum = 0;
		for (int i = 0; i < currentPattern.length; i++ )
		{
			sum += Math.pow((currentPattern[i]- kernel[i]), 2);
		}
		return Math.sqrt(sum);
	}
	
	private static double[] updateKernel(double[][] entrySet, double[] centerOfGravity)
	{
		double minDistance = Double.MAX_VALUE;
		int closestPattern = 0;
		for (int i = 0; i<entrySet.length; i++)
		{
			double distanceToKernel = calculateDistance(entrySet[i], centerOfGravity);
			if (distanceToKernel < minDistance)
			{
				minDistance = distanceToKernel;
				closestPattern = i;
			}
				
		}
		return entrySet[closestPattern];
	}
}