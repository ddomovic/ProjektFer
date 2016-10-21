package pack1;

public class Connection {
	
	private Neuron firstN;
	private Neuron secondN;
	private double weight;
	
	public Connection(double weight, Neuron firstN, Neuron secondN) {
		
		this.weight = weight;
		this.firstN = firstN;
		this.secondN = secondN;
		
	}
	
	public double getWeight() {
		
		return this.weight;
		
	}
	
	public void setWeight(double weight) {
		
		this.weight = weight;
		
	}
	
	public Neuron getFirstN() {
		
		return this.firstN;
	
	}
	
	public void setFirstN(Neuron firstN) {
		
		this.firstN = firstN;
		
	}
	
	public Neuron getSecondN() {
		
		return this.secondN;
	
	}
	
	public void setSecondN(Neuron secondN) {
		
		this.secondN = secondN;
		
	}

}
