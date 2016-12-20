/**
 *
 */
package fer.projekt.neuralnetwork.utils;

/**
 * @author David
 *
 */
public abstract class DataConverter {

	private final Double[] inMinValues;
	private final Double[] inMaxValues;
	private final Double[] outMinValues;
	private final Double[] outMaxValues;

	/**
	 *
	 */
	public DataConverter(int numberOfInputs, int numberOfOutputs) {
		inMinValues = new Double[numberOfInputs];
		inMaxValues = new Double[numberOfInputs];
		outMinValues = new Double[numberOfOutputs];
		outMaxValues = new Double[numberOfOutputs];
	}

	/**
	 * @return the inMinValues
	 */
	public Double[] getInMinValues() {
		return inMinValues;
	}

	/**
	 * @return the inMaxValues
	 */
	public Double[] getInMaxValues() {
		return inMaxValues;
	}

	/**
	 * @return the outMinValues
	 */
	public Double[] getOutMinValues() {
		return outMinValues;
	}

	/**
	 * @return the outMaxValues
	 */
	public Double[] getOutMaxValues() {
		return outMaxValues;
	}

	/**
	 * @param input
	 * @param i
	 *
	 */
	protected void isMinOrMaxInput(double input, int i) {
		if (inMinValues[i] == null || input < inMinValues[i]) {
			inMinValues[i] = input;
		}
		if (inMaxValues[i] == null || input > inMaxValues[i]) {
			inMaxValues[i] = input;
		}
	}

	/**
	 * @param input
	 * @param i
	 *
	 */
	protected void isMinOrMaxOutput(double output, int i) {
		if (outMinValues[i] == null || output < outMinValues[i]) {
			outMinValues[i] = output;
		}
		if (outMaxValues[i] == null || output > outMaxValues[i]) {
			outMaxValues[i] = output;
		}
	}

	public abstract double[] getInput(String[] param);

	public abstract double[] getOutput(String[] param);

}
