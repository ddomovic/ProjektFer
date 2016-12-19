/**
 *
 */
package fer.projekt.neuralnetwork.alcoholconsumption;

/**
 * @author Nikola
 *
 */
public class ServoConverter implements DataConverter{

	private Double[] minInputValues = new Double[5];
	private Double[] maxInputValues = new Double[5];
	/* (non-Javadoc)
	 * @see fer.projekt.neuralnetwork.alcoholconsumption.DataConverter#getMaxInputValues()
	 */
	@Override
	public Double[] getMaxInputValues() {
		return maxInputValues;
	}

	/* (non-Javadoc)
	 * @see fer.projekt.neuralnetwork.alcoholconsumption.DataConverter#getMinInputValues()
	 */
	@Override
	public Double[] getMinInputValues() {
		return minInputValues;
	}

	/* (non-Javadoc)
	 * @see fer.projekt.neuralnetwork.alcoholconsumption.DataConverter#getInput(java.lang.String[])
	 */
	@Override
	public double[] getInput(String[] parameters) {
		double[] in = new double[4];
		in[0] = LetterToNumb(parameters[0]);
		in[1] = LetterToNumb(parameters[1]);
		in[2] = Double.parseDouble(parameters[2]);
		in[3] = Double.parseDouble(parameters[3]);
		return in;
	}

	/* (non-Javadoc)
	 * @see fer.projekt.neuralnetwork.alcoholconsumption.DataConverter#getOutput(java.lang.String[])
	 */
	@Override
	public double[] getOutput(String[] parameters) {
		double[] out = new double[1];
		out[0] = Double.parseDouble(parameters[4]);
		isMinOrMax(out[0], 4);
		return out;
	}

	private static double LetterToNumb(String letter) {
		switch (letter) {
		case "A":
			return -2;
		case "B":
			return -1;
		case "C":
			return 0;
		case "D":
			return 1;
		case "E":
			return 2;
		default:
			System.err.println("Greska");
			return 999999999;
		}
	}

	private void isMinOrMax(double input, int i) {
		if (minInputValues[i] == null || input < minInputValues[i]) {
			minInputValues[i] = input;
		}
		if (maxInputValues[i] == null || input > maxInputValues[i]) {
			maxInputValues[i] = input;
		}
	}

}
