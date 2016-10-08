package pack1;

public class PerceptronInput {
	
	private double inputValue;
	private double inputWeight;
	
	public PerceptronInput(double inputValue, double inputWeight) {
		
		this.inputValue = inputValue;
		this.inputWeight = inputWeight;
		
	}

	public double getInputValue() {
		return inputValue;
	}

	public void setInputValue(double inputValue) {
		this.inputValue = inputValue;
	}

	public double getInputWeight() {
		return inputWeight;
	}

	public void setInputWeight(double inputWeight) {
		this.inputWeight = inputWeight;
	}

}
