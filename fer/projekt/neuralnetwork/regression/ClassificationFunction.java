package fer.projekt.neuralnetwork.regression;

public class ClassificationFunction implements IMathFunction {

	@Override
	public double calculate(double input) {
		Double output = Math.sin(input);
		if (output < 0) {
			return -1;
		} else {
			return 1;
		}
	}

	@Override
	public String getFuncName() {
		return "sin";
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
