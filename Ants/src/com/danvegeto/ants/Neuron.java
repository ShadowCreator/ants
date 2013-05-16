package com.danvegeto.ants;
public class Neuron {
	
	public final double thresholdFactor = 5;

	public double[] weights;
	public double threshold;
	
	public Neuron(int inputs)
	{
		weights = new double[inputs];
		
		for(int i = 0; i < inputs; i++)
			weights[i] = Math.random() - 0.5;
		
		threshold = thresholdFactor * (Math.random() - 0.5);
	}
	
	public Neuron(double[] w, double t)
	{
		weights = w.clone();
		threshold = t;
	}
	
	public double output(double[] inputs)
	{
		double diff = weightedSum(inputs) - threshold;
		return 1.0 / (1 + Math.exp(-diff));
	}
	
	private double weightedSum(double[] inputs)
	{
		double sum = 0;
		for(int i = 0; i < inputs.length; i++)
			sum += inputs[i] * weights[i];
		return sum;
	}
	
	public void mutate()
	{
		for(int i = 0; i < weights.length; i++)
			if(Math.random() < GA.mutationChance)
				weights[i] += GA.mutationRate * (2.0 * Math.random() - 1);
		
		if(Math.random() < GA.mutationChance)
			threshold += GA.mutationRate * (2.0 * Math.random() - 1) * thresholdFactor;
	}
	
	public Neuron copy()
	{
		return new Neuron(weights, threshold);
	}
	
	public Neuron crossover(Neuron other)
	{
		double[] child = new double[weights.length];
		
		for(int i = 0; i < weights.length; i++)
		{
			if(Math.random() < 0.5)
				child[i] = weights[i];
			else
				child[i] = other.weights[i];
		}
		
		double childThreshold;
		
		if(Math.random() < 0.5)
			childThreshold = threshold;
		else
			childThreshold = other.threshold;
		
		return new Neuron(child, childThreshold);
	}
}
