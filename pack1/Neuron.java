package pack1;

import java.util.ArrayList;

import fer.projekt.neuralnetwork.activationfunction.ITransferFunction;

public class Neuron {
	
	private int id;
	private static int counter = 0;
	private ITransferFunction tfunction;
	private double bias;
	private ArrayList<Connection> connections = new ArrayList<>();
	private double output; 
	
	public Neuron(ITransferFunction tfunction, double bias) {
		
		this.tfunction = tfunction;
		this.bias = bias;
		this.id = counter++;
		
	}
	
	public ITransferFunction getTfunction() {
		
		return this.tfunction;
		
	}
	
	public void setTfunction(ITransferFunction tfunction) {
		
		this.tfunction = tfunction;
		
	}
	
	public double getBias() {
		
		return this.bias;
		
	}
	
	public void setBias(double bias) {
		
		this.bias = bias;
		
	}
	
	public ArrayList<Connection> getConnections() {
		
		return this.connections;
		
	}
	
	public void addNewConnections(ArrayList<Neuron> neu, double minWeight, double maxWeight) {
		
		for (Neuron n : neu) {
			
			Connection con = new Connection(Math.random()*(maxWeight - minWeight) + minWeight, n, this);
			
			connections.add(con);
			
		}
		
	}
	
	public double getOutput() {
	       
		return this.output;
    
	}
	
    public void setOutput(double o){
        
    	this.output = o;
    
    }
	
	public double calculateOutput() {
		
		double s = 0;
		
		for (Connection c : connections) {
			
			s += c.getWeight() * c.getFirstN().getOutput();
			
		}
		
		s += this.bias;
		output = tfunction.applyFunction(s);
		
		return output;
	}

}
