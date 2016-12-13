/**
 *
 */
package fer.projekt.neuralnetwork.regression.brodicidataset;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import fer.projekt.neuralnetwork.FileUtils;
import fer.projekt.neuralnetwork.NeuralNetwork;
import fer.projekt.neuralnetwork.activationfunction.ITransferFunction;
import fer.projekt.neuralnetwork.activationfunction.LinearTransferFunction;
import fer.projekt.neuralnetwork.activationfunction.WaveTransferFunction;
import fer.projekt.neuralnetwork.elements.Connection;
import fer.projekt.neuralnetwork.elements.Layer;
import fer.projekt.neuralnetwork.elements.Neuron;

/**
 * @author Nikola
 *
 */
public class RegressionNetwork extends NeuralNetwork {
	/**
	 * Broj neurona u hidden layeru.
	 */
	public static int NUMBOF_HID_NEURONS = 15;
	/**
	 * Minimalna težina na konekcijama izmedu prvog i drugog layera.
	 */
	public static double MIN_WEIGHTS_FIRST_LAYER = -10d;
	/**
	 * Maximalna težina na konekcijama izmedu prvog i drugog layera.
	 */
	public static double MAX_WEIGHTS_FIRST_LAYER = 10d;

	public static final int LEARN_NUMERATOR = 9;
	public static final int LEARN_DENUMERATOR = 10;
	private int learningDatasetSize;
	private List<Data> learningDataset;
	private int testingDatasetSize;
	private List<Data> testingDataset;

	public RegressionNetwork(Path networkSetup, Path datasetPath) {
		super();
		ITransferFunction wavefunction = new WaveTransferFunction("PodaciZaAktivacijskuFunkciju.txt");
		this.addLayer(new Layer(6, wavefunction, 0));
		//	this.addLayer(new Layer(3, wavefunction, 1), -1, 1);
		this.addLayer(new Layer(NUMBOF_HID_NEURONS, wavefunction, 0), MIN_WEIGHTS_FIRST_LAYER, MAX_WEIGHTS_FIRST_LAYER);

		List<List<Data>> returnList = DataSetUtil.splitDataset(datasetPath, LEARN_NUMERATOR, LEARN_DENUMERATOR);
		learningDataset = returnList.get(0);
		learningDatasetSize = learningDataset.size();
		testingDataset = returnList.get(1);
		testingDatasetSize = testingDataset.size();

		if (networkSetup == null) {
			this.setupNetwork();
			FileUtils.saveNetwork(this, "RegressionNetwork");
		} else {
			ITransferFunction tlinear = new LinearTransferFunction();
			Layer outputLayer = new Layer(1, tlinear, 0);
			this.addLayer(outputLayer, 0, 1);
			FileUtils.loadNeuralNetwork(this, networkSetup);
		}
	}

	/**
	 * Izračunava tezine konekcija neuronske mreže i postavlja ih na konekcije
	 * izmedu hidden i output layera. Koristi se linearna regresija.
	 */
	public void setupNetwork() {
		double x[][] = new double[learningDatasetSize][NUMBOF_HID_NEURONS + 1];
		double y[] = new double[learningDatasetSize];

		Data testInput = learningDataset.get(0);
		for (int i = 0; i < learningDatasetSize - 1; i++, testInput = learningDataset.get(i)) {
			// 0.stupac mora biti = 1
			x[i][0] = 1;
			y[i] = testInput.getOut();
			double in[] = {testInput.getIn1(),testInput.getIn2(),testInput.getIn3(),testInput.getIn4(),testInput.getIn5(),testInput.getIn6()};
			// ostali stupci x matrice su outputi hidden neurona
			double[] result = this.run(in);
			int j = 1;
			for (double re : result) {
				x[i][j++] = re;
			}
		}

		OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
		regression.setNoIntercept(true);
		regression.newSampleData(y, x);
		double[] weights = regression.estimateRegressionParameters();

		// dodat jos zadnji layer(neuron)
		ITransferFunction tlinear = new LinearTransferFunction();
		Layer outputLayer = new Layer(1, tlinear, 0);
		this.addLayer(outputLayer, 0, 1); // ovdje je tezina konekcija nebitna
		// jer ju ionako namjestamo
		outputLayer.forEach(new Consumer<Neuron>() {

			int i = 1;

			@Override
			public void accept(Neuron n) {
				ArrayList<Connection> inConnections = n.getAllInConnections();
				inConnections.forEach(c -> {
					c.setWeight(weights[i++]);
				});
				n.setBias(weights[0]);
			}
		});
	}


	/**
	 * Testira izgeneriranu mrežu na nekoliko random brojeva i vraća postotak
	 * pogreške.
	 *
	 * @param takeRandomTests
	 *            označava da li su input testovi generirani random ili linearno
	 *            na intervalu
	 *
	 * @return postotak pogreške od očekivanog rezultata
	 */
	public double runTests(boolean print) {
		double totalPercErr = 0;

		System.out.println("   OCEKIVANO:\t\t DOBIVENO:\tgreska:");
		for (int i = 0; i < testingDatasetSize; i++) {
			Data testInput = testingDataset.get(i);
			double in[] = {testInput.getIn1(),testInput.getIn2(),testInput.getIn3(),testInput.getIn4(),testInput.getIn5(),testInput.getIn6()};


			double correct = testInput.getOut();
			double out = this.run(in)[0];
			double percErr = Math.abs(out - correct) / Math.abs(correct) * 100;
			if (correct > 0.00001) {
				totalPercErr += percErr;
			}
			System.out.printf("%4d. %.5f \t	%.5f = \t%.10f%%%n", i + 1,
					correct, out, percErr);
		}
		double avgPerc = totalPercErr / testingDatasetSize;
		System.out.printf("%nUkupna pogreška je: %.5f%% %n", avgPerc);
		return avgPerc;
	}


	/**
	 * The magic happens here
	 *
	 * @param args
	 *            parametri komandne linije
	 */
	public static void main(String[] args) {
		RegressionNetwork network = new RegressionNetwork(null, Paths.get("brodici.data"));
		//RegressionNetwork network = new RegressionNetwork(Paths.get("RegressionNetwork"),Paths.get("brodici.data"));

		network.runTests(true);
	}
}
