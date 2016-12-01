package fer.projekt.neuralnetwork.regression;

public class ClassificationFunction implements IMathFunction {


	@Override
	public String getFuncName() {
		return "klasifikacijska";
	}
	
	@Override
	public double calculate(double input) {
		if(input%3 <= 1){
			return 1;
		}
		else return 0;
	}

	@Override
	public double getDomainMin() {
		return 0;
	}

	@Override
	public double getDomainMax() {
		return 6;
	}

}
