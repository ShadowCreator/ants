package com.danvegeto.ants;

public class GA {

	public static final double 
		mutationChance = 0.05,
		mutationRate = 1;
	
	public static int stochasticSelection(double[] weights)
	{
		double sum = 0;
		for(double d : weights)
			sum += d;
		
		if(sum <= 0)
			return (int)(Math.random() * weights.length);
		
		double rnd = sum * Math.random();
		for(int i = 0; i < weights.length; i++)
		{
			rnd -= weights[i];
			if(rnd <= 0)
				return i;
		}
		
		return -1;
	}
	
}
