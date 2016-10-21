package pack1;

import java.util.ArrayList;

import fer.projekt.neuralnetwork.activationfunction.ITransferFunction;

public class Layer {
	
	private int numberOfNeurons;
	private ITransferFunction tfunction;
	private double bias;
	private ArrayList<Neuron> neurons = new ArrayList<>();
	private Layer nextLayer;
	
	public Layer(int numberOfNeurons, ITransferFunction tfunction, double bias) {
		
		this.numberOfNeurons = numberOfNeurons;
		this.tfunction = tfunction;
		this.bias = bias;
		for (int i = 0; i < numberOfNeurons; i++) {
			
			neurons.add(new Neuron(tfunction, bias));
			
		}
		
	}
	
	public Layer(int numberOfNeurons, ITransferFunction tfunction, double minBias, double maxBias) {
		
		this.numberOfNeurons = numberOfNeurons;
		this.tfunction = tfunction;
		for (int i = 0; i < numberOfNeurons; i++) {
			
			neurons.add(new Neuron(tfunction, Math.random() * (maxBias - minBias) + minBias));
			
		}
	}
	
	public int getNumberOfNeurons() {
		
		return this.numberOfNeurons;
		
	}
	
	public ITransferFunction getTfuction() {
		
		return this.tfunction;
		
	}
	
	public void setNextLayer(Layer nextLayer) {
		
		this.nextLayer = nextLayer;
		for (Neuron n : nextLayer.getNeurons()) {
			
				n.addNewConnections(this.neurons, 0, 1);
			
		}
		
	}
	
	public void setNextLayer(Layer nextLayer, double minWeight, double maxWeight) {
		
		this.nextLayer = nextLayer;
		for (Neuron n : nextLayer.getNeurons()) {
			
			n.addNewConnections(this.neurons, minWeight, maxWeight);
			
		}
		
	}
	
	public Layer getNextLayer() {
		
		return this.nextLayer;
		
	}
	
	public ArrayList<Neuron> getNeurons() {
		
		return this.neurons;
		
	}

}
