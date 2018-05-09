package rf8;

public class MainClass {
	public static void main(String[] args)
	{
		double[][] learningSet;
		try
		{
			learningSet = FileUtils.readLearningSetFromFile("in.txt");
			int numberOfPatterns = learningSet.length;
			int numberOfFeatures = learningSet[0].length;
			System.out.println(String.format("The learning set has %s patterns and %s features", numberOfPatterns, numberOfFeatures));
			int[] iClass = ClassificationUtils.classifySet(learningSet, 3, numberOfFeatures);
			for(int i=0;i<iClass.length;i++)
			{
				System.out.println("class : " + (iClass[i+0]));
			}
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		finally
		{
			System.out.println("Finished learning set operations");
		}
	}

}
