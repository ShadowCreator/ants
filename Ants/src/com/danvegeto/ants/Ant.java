package com.danvegeto.ants;
import java.awt.Color;


public class Ant {
	
	public Colony colony;
	
	public Color color;
	
	public NeuralNetwork brain;
	
	public int direction;
		// 0 = N, 1 = E, 2 = S, 3 = W
	
	private double[] outputs;
		// R G B   R G B  F 
		// scent   movement
	
	public double food;
	
	public int age, starvation;
	
	public Ant(Colony c)
	{
		colony = c;
		color = c.color;
		brain = new NeuralNetwork(5, 8, 8);
		
		direction = (int)(Math.random() * 4);
		outputs = new double[8];
		age = 0;
		
		food = Game.foodPerOffspring -1;
	}
	
	public Ant(Colony c, NeuralNetwork n)
	{
		colony = c;
		color = c.color;
		brain = n.copy();
		
		direction = (int)(Math.random() * 4);
		outputs = new double[8];
		age = 0;
		
		food = Game.foodPerOffspring -1;
	}
	
	public void act(double dist, double rgb[], boolean[] open, double[][] scents)
	{
		if(food > 0)
		{
			food -= Game.foodConsumption;
			starvation = 0;
		}
		else
		{
			food = 0;
			starvation++;
		}
		
		outputs = brain.outputs(inputs(dist, rgb));
		turn(open, scents);
		age++;
	}
	
	private double[] inputs(double dist, double[] rgb)
	{
		double[] inputs = new double[5];
		
		for(int i = 0; i < rgb.length; i++)
			inputs[i] = rgb[i];
		
		inputs[3] = 1.0 * food / Game.foodCapacity;
		inputs[4] = dist;
		
		return inputs;
	}
	
	// R G B
	public double[] scent()
	{
		double[] rgb = new double[3];
		for(int i = 0; i < rgb.length; i++)
			rgb[i] = outputs[i];
		return rgb;
	}
	
	// R G B
	private double[] scentBias()
	{
		double[] move = new double[3];
		for(int i = 0; i < move.length; i++)
			move[i] = outputs[3 + i];
		return move;
	}
	
	private double forwardBias()
	{
		return outputs[6];
	}
	private double movementBias()
	{
		return outputs[7];
	}
	
	private void turn(boolean[] open, double[][] scents)
	{
		double f = forwardBias();
		double m = movementBias();
		
		double[] chances = 
		{
				f * m, 
				(1 - f) * m, 
				0, 
				(1 - f) * m
		};
		
		double[] scentBias = scentBias();
		
		for(int i = 0; i < chances.length; i++)
		{
			for(int c = 0; c < 3; c++)
				chances[i] += 
					(scents[absDir(i)][c] * scentBias[c] +
					((1 - scents[absDir(i)][c]) * (1 - scentBias[c])))
						* (1 - m);
			
			if(!open[absDir(i)])
				chances[i] = 0;
		}
		
		int relDir = GA.stochasticSelection(chances);
		
		direction = absDir(relDir);
	}
	
	private int absDir(int relDir)
	{
		return (direction + relDir) % 4;
	}

	public void mutate()
	{
		brain.mutate();
	}
	
	public Ant copy()
	{
		return new Ant(colony, brain);
	}
	
	public Ant crossover(Ant other)
	{
		return new Ant(colony, brain.crossover(other.brain));
	}
}
