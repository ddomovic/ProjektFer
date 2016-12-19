/**
 *
 */
package fer.projekt.neuralnetwork.alcoholconsumption;

public class StudentConverter implements DataConverter {

	private Double[] minInputValues = new Double[33];
	private Double[] maxInputValues = new Double[33];

	@Override
	public double[] getInput(String[] param) {
		double[] input = new double[32];
		input[0] = param[0].equals("GP") ? 1 : 0;
		input[1] = param[1].equals("F") ? 1 : 0;

		input[2] = Double.parseDouble(param[2]);
		isMinOrMax(input[2], 2);

		input[3] = param[3].equals("U") ? 1 : 0;
		input[4] = param[4].equals("LE3") ? 1 : 0;
		input[5] = param[5].equals("T") ? 1 : 0;

		input[6] = Double.parseDouble(param[6]);
		isMinOrMax(input[6], 6);

		input[7] = Double.parseDouble(param[7]);
		isMinOrMax(input[7], 7);

		input[8] = convertJob(param[8]);
		input[9] = convertJob(param[9]);

		input[10] = convertReason(param[10]);
		if(param[11].equals("mother")){
			input[11] = 0;
		} else if (param[11].equals("father")){
			input[11] = 0.5;
		} else {
			input[11] = 1;
		}
		input[12] = Double.parseDouble(param[12]);
		isMinOrMax(input[12], 12);

		input[13] = Double.parseDouble(param[13]);
		isMinOrMax(input[13], 13);

		input[14] = Double.parseDouble(param[14]);
		isMinOrMax(input[14], 14);

		input[15] = param[15].equals("yes") ? 1 : 0;
		input[16] = param[16].equals("yes") ? 1 : 0;
		input[17] = param[17].equals("yes") ? 1 : 0;
		input[18] = param[18].equals("yes") ? 1 : 0;
		input[19] = param[19].equals("yes") ? 1 : 0;
		input[20] = param[20].equals("yes") ? 1 : 0;
		input[21] = param[21].equals("yes") ? 1 : 0;
		input[22] = param[22].equals("yes") ? 1 : 0;

		input[23] = Double.parseDouble(param[23]);
		isMinOrMax(input[23], 23);

		input[24] = Double.parseDouble(param[24]);
		isMinOrMax(input[24], 24);

		input[25] = Double.parseDouble(param[25]);
		isMinOrMax(input[25], 25);

		input[26] = Double.parseDouble(param[26]);
		isMinOrMax(input[26], 26);

		input[27] = Double.parseDouble(param[27]);
		isMinOrMax(input[27], 27);

		input[28] = Double.parseDouble(param[28]);
		isMinOrMax(input[28], 28);

		input[29] = Double.parseDouble(param[29]);
		isMinOrMax(input[29], 29);

		input[30] = Double.parseDouble(param[30]);
		isMinOrMax(input[30], 30);

		input[31] = Double.parseDouble(param[31]);
		isMinOrMax(input[31], 31);

		return input;
	}

	/**
	 * @param input
	 * @param i
	 *
	 */
	private void isMinOrMax(double input, int i) {
		if (minInputValues[i] == null || input < minInputValues[i]) {
			minInputValues[i] = input;
		}
		if (maxInputValues[i] == null || input > maxInputValues[i]) {
			maxInputValues[i] = input;
		}
	}

	/**
	 * @param string
	 * @return
	 */
	private double convertReason(String string) {
		switch(string){
		case "home":
			return 0;
		case "reputation":
			return 1d/3;
		case "course":
			return 2d/3;
		case "other":
			return 1;
		default:
			System.err.println("krivi podatak u datasetu");
			return 999;
		}
	}

	/**
	 * @param string
	 * @return
	 */
	private double convertJob(String string) {
		switch(string){
		case "teacher":
			return 0;
		case "health":
			return 0.25;
		case "services":
			return 0.5;
		case "at_home":
			return 0.75;
		case "other":
			return 1;
		default:
			System.err.println("krivi podatak u datasetu");
			return 999;
		}

	}

	@Override
	public double[] getOutput(String[] param) {
		double[] output = new double[1];
		output[0] = Double.parseDouble(param[32]);
		isMinOrMax(output[0], 32);
		return output;
	}

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

}
