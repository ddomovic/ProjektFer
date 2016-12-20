/**
 *
 */
package fer.projekt.neuralnetwork.datasetExamples;

import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import fer.projekt.neuralnetwork.ClassificationRegressionNetwork;
import fer.projekt.neuralnetwork.utils.ClassificationOutput;
import fer.projekt.neuralnetwork.utils.DataConverter;

/**
 * @author David
 *
 */
public class InflamationConverter extends DataConverter {

	NumberFormat nf = NumberFormat.getInstance(Locale.FRANCE);

	public InflamationConverter() {
		super(6, 2);
	}

	/* (non-Javadoc)
	 * @see fer.projekt.neuralnetwork.utils.DataConverter#getInput(java.lang.String[])
	 */
	@Override
	public double[] getInput(String[] param) {
		double[] in = new double[6];

		Number number = null;
		try {
			number = nf.parse(param[0]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		in[0] = number.doubleValue();
		in[1] = param[1].equals("no") ? 0 : 1;
		in[2] = param[2].equals("no") ? 0 : 1;
		in[3] = param[3].equals("no") ? 0 : 1;
		in[4] = param[4].equals("no") ? 0 : 1;
		in[5] = param[5].equals("no") ? 0 : 1;

		for (int i = 0; i < in.length; i++) {
			isMinOrMaxInput(in[i], i);
		}

		return in;
	}

	/* (non-Javadoc)
	 * @see fer.projekt.neuralnetwork.utils.DataConverter#getOutput(java.lang.String[])
	 */
	@Override
	public double[] getOutput(String[] param) {
		double[] out = new double[2];

		out[0] = param[6].equals("no") ? 0 : 1;
		out[1] = param[7].equals("no") ? 0 : 1;

		for (int i = 0; i < out.length; i++) {
			isMinOrMaxOutput(out[i], i);
		}

		return out;
	}

	/**
	 * The magic happens here
	 *
	 * @param args
	 *            parametri komandne linije
	 */
	public static void main(String[] args) {
		final boolean radiNovuMrezu = true;
		ClassificationOutput[] cOutputs = { new ClassificationOutput(0, 1, 0, 1), new ClassificationOutput(0, 1, 0, 1) };
		final String networkName = "InflamationNetwork";
		String datasetPath = "inflamation.txt";
		DataConverter converter = new InflamationConverter();

		ClassificationRegressionNetwork network = new ClassificationRegressionNetwork(radiNovuMrezu, cOutputs, networkName,
				Paths.get(datasetPath), converter);

		System.out.print("Best thresholds: ");
		for (double d : network.getBestThresholds()) {
			System.out.print(d + ", ");
		}
		System.out.println();

		network.test(true);
	}

}
