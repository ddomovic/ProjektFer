/**
 *
 */
package fer.projekt.neuralnetwork.alcoholconsumption;

/**
 * @author Nikola
 *
 */
public class StudentConverter implements DataConverter {



	@Override
	public double[] getInput(String[] param) {
		double[] input = new double[32];
		input[0] = param[0].equals("GP") ? 1 : 0;
		input[1] = param[1].equals("F") ? 1 : 0;
		input[2] = Double.parseDouble(param[2]);
		input[3] = param[3].equals("U") ? 1 : 0;
		input[4] = param[4].equals("LE3") ? 1 : 0;
		input[5] = param[5].equals("T") ? 1 : 0;
		input[6] = Double.parseDouble(param[6]);
		input[7] = Double.parseDouble(param[7]);
		input[8] = convertJob(param[8]);
		input[9] = convertJob(param[9]);
		input[10] = convertReason(param[10]);
		//12:
		if(param[11].equals("mother")){
			input[11] = -1;
		}
		else if(param[11].equals("father")){
			input[11] = 1;
		} else {
			input[11] = 0;
		}
		//13:
		input[12] = Double.parseDouble(param[12]);
		input[13] = Double.parseDouble(param[13]);
		input[14] = Double.parseDouble(param[14]);
		input[15] = param[15].equals("yes") ? 1 : 0;
		input[16] = param[16].equals("yes") ? 1 : 0;
		input[17] = param[17].equals("yes") ? 1 : 0;
		input[18] = param[18].equals("yes") ? 1 : 0;
		input[19] = param[19].equals("yes") ? 1 : 0;
		input[20] = param[20].equals("yes") ? 1 : 0;
		input[21] = param[21].equals("yes") ? 1 : 0;
		input[22] = param[22].equals("yes") ? 1 : 0;
		input[23] = Double.parseDouble(param[23]);
		input[24] = Double.parseDouble(param[24]);
		input[25] = Double.parseDouble(param[25]);
		input[26] = Double.parseDouble(param[26]);
		input[27] = Double.parseDouble(param[27]);
		input[28] = Double.parseDouble(param[28]);
		input[29] = Double.parseDouble(param[29]);
		input[30] = Double.parseDouble(param[30]);
		input[31] = Double.parseDouble(param[31]);
		return input;
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
			return 5;
		case "course":
			return 10;
		case "other":
			return 15;
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
			return 5;
		case "services":
			return 10;
		case "at_home":
			return 15;
		case "other":
			return 20;
		default:
			System.err.println("krivi podatak u datasetu");
			return 999;
		}

	}

	@Override
	public double[] getOutput(String[] param) {
		double[] input = new double[1];
		input[0] = Double.parseDouble(param[32]);
		return input;
	}



}
