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
		double[] input = new double[30];
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
		input[10] = converReason(param[10]);

		return input;
	}

	@Override
	public double[] getOutput(String[] parameters) {
		// TODO Auto-generated method stub
		return null;
	}



}
