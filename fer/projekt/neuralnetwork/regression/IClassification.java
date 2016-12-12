package fer.projekt.neuralnetwork.regression;

public interface IClassification {

	public double calculate(double input, double border);
	
	public String getFuncName();

	public double getDomainMin();
	
	public double getDomainMax();
	
}
