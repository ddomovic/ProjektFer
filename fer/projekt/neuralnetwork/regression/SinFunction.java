package fer.projekt.neuralnetwork.regression;

public class SinFunction implements IMathFunction {

	@Override
	public String getFuncName() {
		return "sin";
	}
	
	@Override
	public double calculate(double input) {
		return Math.sin(input);
	}

	@Override
	public double getDomainMin() {
		return 0;
	}

	@Override
	public double getDomainMax() {
		return 2 * Math.PI;
	}

}
