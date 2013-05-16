package com.danvegeto.ants;

public class NeuralNetwork {
	
	public NeuronLayer layer0, layer1;
	
	public NeuralNetwork(int inputs, int hidden, int outputs)
	{
		layer0 = new NeuronLayer(hidden, inputs);		
		layer1 = new NeuronLayer(outputs, hidden);
	}
	
	public NeuralNetwork(NeuronLayer lyr0, NeuronLayer lyr1)
	{
		layer0 = lyr0.copy();
		layer1 = lyr1.copy();
	}
	
	public double[] outputs(double[] inputs)
	{
		return layer1.outputs(layer0.outputs(inputs));
	}
	
	public void mutate()
	{
		layer0.mutate();
		layer1.mutate();
	}

	public NeuralNetwork copy()
	{
		return new NeuralNetwork(layer0, layer1);
	}
	
	public NeuralNetwork crossover(NeuralNetwork other)
	{
		return new NeuralNetwork(
				layer0.crossover(other.layer0),
				layer1.crossover(other.layer1)
				);
	}
}
