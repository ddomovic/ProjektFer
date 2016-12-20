/**
 *
 */
package fer.projekt.neuralnetwork.datasetExamples;

import java.nio.file.Paths;

import fer.projekt.neuralnetwork.ClassificationRegressionNetwork;
import fer.projekt.neuralnetwork.utils.DataConverter;

public class ServoConverter extends DataConverter {

	/**
	 * @param numberOfInputs
	 * @param numberOfOutputs
	 */
	public ServoConverter() {
		super(4, 1);
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

		for (int i = 0; i < in.length; i++) {
			isMinOrMaxInput(in[i], i);
		}

		return in;
	}

	/* (non-Javadoc)
	 * @see fer.projekt.neuralnetwork.alcoholconsumption.DataConverter#getOutput(java.lang.String[])
	 */
	@Override
	public double[] getOutput(String[] parameters) {
		double[] out = new double[1];

		out[0] = Double.parseDouble(parameters[4]);

		for (int i = 0; i < out.length; i++) {
			isMinOrMaxOutput(out[i], i);
		}

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

	/**
	 * The magic happens here
	 *
	 * @param args
	 *            parametri komandne linije
	 */
	public static void main(String[] args) {
		final boolean radiNovuMrezu = true;
		final String networkName = "ServoNetwork";
		String datasetPath = "servo.data";
		DataConverter converter = new ServoConverter();

		ClassificationRegressionNetwork network = new ClassificationRegressionNetwork(radiNovuMrezu, null, networkName,
				Paths.get(datasetPath), converter);
		network.test(true);
	}
}
