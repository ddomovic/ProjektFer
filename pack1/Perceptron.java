package pack1;

import java.util.ArrayList;
import java.util.List;

public class Perceptron {
	
	private List<PerceptronInput> input = new ArrayList<PerceptronInput>();
	private double threshold;
	private double output;
	private boolean isSigmoid;
	
	public Perceptron(List<PerceptronInput> input, double threshold, boolean isSigmoid) {
		
		this.input = input;
		this.threshold = threshold;
		this.isSigmoid = isSigmoid;
		
	}

	public List<PerceptronInput> getInput() {
		return input;
	}

	public void setInput(List<PerceptronInput> input) {
		this.input = input;
	}

	public boolean isSigmoid() {
		return isSigmoid;
	}

	public void setSigmoid(boolean isSigmoid) {
		this.isSigmoid = isSigmoid;
	}
	
	public double calculateOutput() {
		
		double temp = 0;
		
		for (PerceptronInput i : this.input) {
				
			temp += i.getInputValue()*i.getInputWeight();
				
		}
		
		if (!isSigmoid) {
			
			if (temp-this.threshold <= 0) {
				
				this.output = 0;
				
			}
			else {
				
				this.output = 1;
				
			}
			
		}
		else {
			
			this.output = 1.0 / (1 + Math.exp(-temp));
			
		}
		
		return this.output;
		
	}

}
