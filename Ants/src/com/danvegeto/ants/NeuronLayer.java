package com.danvegeto.ants;

public class NeuronLayer {
	
	public Neuron[] neurons;
	
	public NeuronLayer(int width, int inputs)
	{
		neurons = new Neuron[width];
		
		for(int i = 0; i < width; i++)
			neurons[i] = new Neuron(inputs);
	}
	
	public NeuronLayer(Neuron[] n)
	{
		neurons = new Neuron[n.length];
		
		for(int i = 0; i < n.length; i++)
			neurons[i] = n[i].copy();
	}
	
	public double[] outputs(double[] inputs)
	{
		double[] outputs = new double[neurons.length];
		
		for(int i = 0; i < outputs.length; i++)
			outputs[i] = neurons[i].output(inputs);
		
		return outputs;
	}
	
	public void mutate()
	{
		for(Neuron n : neurons)
			n.mutate();
	}

	public NeuronLayer copy()
	{
		return new NeuronLayer(neurons);
	}
	
	public NeuronLayer crossover(NeuronLayer other)
	{
		Neuron[] child = new Neuron[neurons.length];
		for(int i = 0; i < neurons.length; i++)
			child[i] = neurons[i].crossover(other.neurons[i]);
		return new NeuronLayer(child);
	}
}
