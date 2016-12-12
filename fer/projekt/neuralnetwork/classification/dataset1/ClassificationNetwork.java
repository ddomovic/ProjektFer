package fer.projekt.neuralnetwork.classification.dataset1;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import fer.projekt.neuralnetwork.NeuralNetwork;
import fer.projekt.neuralnetwork.activationfunction.ITransferFunction;
import fer.projekt.neuralnetwork.activationfunction.LinearTransferFunction;
import fer.projekt.neuralnetwork.activationfunction.WaveTransferFunction;
import fer.projekt.neuralnetwork.elements.Connection;
import fer.projekt.neuralnetwork.elements.Layer;
import fer.projekt.neuralnetwork.elements.Neuron;
import fer.projekt.neuralnetwork.utils.FileUtils;

public class ClassificationNetwork extends NeuralNetwork {
	/**
	 * Broj neurona u hidden layeru.
	 */
	public static int NUMBOF_HID_NEURONS = 30;
	public static int NUMBOF_OUTPUT = 2;
	/**
	 * Minimalna težina na konekcijama izmedu prvog i drugog layera.
	 */
	public static double MIN_WEIGHTS_FIRST_LAYER = -10d;
	/**
	 * Maximalna težina na konekcijama izmedu prvog i drugog layera.
	 */
	public static double MAX_WEIGHTS_FIRST_LAYER = 10d;

	public static final int LEARN_NUMERATOR = 4;
	public static final int LEARN_DENUMERATOR = 5;
	private int learningDatasetSize;
	private List<Data> learningDataset;
	private int testingDatasetSize;
	private List<Data> testingDataset;

	public ClassificationNetwork(Path networkSetup, Path datasetPath) {
		super();
		//		ITransferFunction wavefunction = new WaveTransferFunction("/home/ProjektFer/PodaciZaAktivacijskuFunkciju.txt");
		//ITransferFunction wavefunction = new WaveTransferFunction("D:/ProjektFer/PodaciZaAktivacijskuFunkciju.txt");
		ITransferFunction wavefunction = new WaveTransferFunction(
				"C:/Users/David/Desktop/git/ProjektFer/PodaciZaAktivacijskuFunkciju.txt");
		this.addLayer(new Layer(6, wavefunction, 0));
		this.addLayer(new Layer(10_000, wavefunction, 1), -1, 1);
		this.addLayer(new Layer(NUMBOF_HID_NEURONS, wavefunction, 0), MIN_WEIGHTS_FIRST_LAYER, MAX_WEIGHTS_FIRST_LAYER);

		List<List<Data>> returnList = DatasetUtil.splitDataset(datasetPath, LEARN_NUMERATOR, LEARN_DENUMERATOR);
		learningDataset = returnList.get(0);
		learningDatasetSize = learningDataset.size();
		testingDataset = returnList.get(1);
		testingDatasetSize = testingDataset.size();

		if (networkSetup == null) {
			this.setupNetwork();
			FileUtils.saveNetwork(this, "ClassificationNetwork");
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
		double y[][] = new double[learningDatasetSize][NUMBOF_OUTPUT];

		Data testInput = learningDataset.get(0);
		for (int i = 0; i < learningDatasetSize - 1; i++, testInput = learningDataset.get(i)) {
			// 0.stupac mora biti = 1
			x[i][0] = 1;
			y[i][0] = testInput.getInflammation();
			y[i][1] = testInput.getNephritis();
			double in[] = { testInput.getTemperature(), testInput.getNausea(), testInput.getLumbPain(),
					testInput.getUrinePush(), testInput.getMictPain(), testInput.getBurn() };
			// ostali stupci x matrice su outputi hidden neurona
			double[] result = this.run(in);
			int j = 1;
			for (double re : result) {
				x[i][j++] = re;
			}
		}

		OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
		regression.setNoIntercept(true);
		double[] aux = new double[learningDatasetSize];
		for (int j = 0; j < learningDatasetSize; j++) {

			aux[j] = y[j][0];

		}
		regression.newSampleData(aux, x);
		double[] weights = regression.estimateRegressionParameters();
		regression.setNoIntercept(true);
		for (int j = 0; j < learningDatasetSize; j++) {

			aux[j] = y[j][1];

		}
		regression.newSampleData(aux, x);
		double[] weights2 = regression.estimateRegressionParameters();

		// dodat jos zadnji layer(neuron)
		ITransferFunction tlinear = new LinearTransferFunction();
		Layer outputLayer = new Layer(2, tlinear, 0);
		this.addLayer(outputLayer, 0, 1); // ovdje je tezina konekcija nebitna
		// jer ju ionako namjestamo

		Neuron n = outputLayer.getNeuron(0);
		ArrayList<Connection> inConnections = n.getAllInConnections();
		inConnections.forEach(new Consumer<Connection>() {
			int i = 1;

			@Override
			public void accept(Connection c) {
				c.setWeight(weights[i++]);
			}
		});
		n.setBias(weights[0]);

		n = outputLayer.getNeuron(1);
		inConnections = n.getAllInConnections();
		inConnections.forEach(new Consumer<Connection>() {
			int i = 1;

			@Override
			public void accept(Connection c) {
				c.setWeight(weights2[i++]);
			}
		});
		n.setBias(weights2[0]);

	}

	/**
	 * Metoda koju koristimo kada želimo binarnu klasifikaciju
	 *
	 * @param clas1
	 *            double vrijednost 1. klase
	 * @param clas2
	 *            double vrijednost 2. klase
	 * @param treshhold
	 *            threshold ispod kojeg se zaokružuje na 1. klasu
	 * @param input
	 *            Ulaz funkcije čiji izlaz računamo
	 * @return output kao vrijednost clas1 ili clas2
	 */
	public double[] runAndClassify(ClassificationOutput[] cOutputs, double[] input) {
		double[] output = run(input);
		for (int i = 0; i < cOutputs.length; i++) {
			ClassificationOutput cOut = cOutputs[i];
			if (output[i] <= cOut.getThreshold()) {
				output[i] = cOut.getClas1();
			} else {
				output[i] = cOut.getClas2();
			}
		}
		return output;
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
	public double test(ClassificationOutput[] cOutputs, boolean doPrint) {
		double totalErrCounter = 0;

		if (doPrint) {
			System.out.println("\tOCEKIVANO:\tDOBIVENO:        točna klasifikacija?:");
		}
		for (int i = 0; i < testingDatasetSize; i++) {
			Data testInput = testingDataset.get(i);
			double testIn[] = { testInput.getTemperature(), testInput.getNausea(), testInput.getLumbPain(),
					testInput.getUrinePush(), testInput.getMictPain(), testInput.getBurn() };

			double[] correct = {testInput.getInflammation(), testInput.getNephritis()};
			double[] out = this.runAndClassify(cOutputs, testIn);
			boolean isti = Arrays.equals(correct, out);
			if (!isti) {
				totalErrCounter++;
			}
			if (doPrint) {

				System.out.printf("%4d. \t%.1f, %.1f \t%.1f, %.1f \t %.5s %n", i + 1, correct[0], correct[1], out[0], out[1], isti);
			}
		}

		double avgPerc = totalErrCounter / testingDatasetSize * 100;
		if (doPrint) {
			System.out.printf("%nUkupna pogreška je: %.5f%% %n", avgPerc);
		}
		return avgPerc;
	}

	private double[] getBestThreshold(ClassificationNetwork network, ClassificationOutput[] cOutputs) {
		int brojStepova = 100;
		double[] steps = new double[NUMBOF_OUTPUT];
		for (int i = 0; i < cOutputs.length; i++) {
			steps[i] = (cOutputs[i].getClas2() - cOutputs[i].getClas1()) / brojStepova;
		}

		double[] bestThresholds = new double[NUMBOF_OUTPUT];
		Arrays.fill(bestThresholds, 0);

		double perc;
		double bestPerc = 100;
		for (int i = 0; i < brojStepova; i++) {
			perc = network.test(cOutputs, false);
			if (perc < bestPerc) {
				bestPerc = perc;
				int j = 0;
				for (ClassificationOutput cO : cOutputs) {
					bestThresholds[j++] = cO.getThreshold();
				}
			}
			//povecaj threshold za step
			int z = 0;
			for (ClassificationOutput cO : cOutputs) {
				cO.setThreshold(cO.getThreshold() + steps[z++]);
			}
		}
		int i = 0;
		for (ClassificationOutput cO : cOutputs) {
			cO.setThreshold(bestThresholds[i++]);
		}

		return bestThresholds;
	}

	/**
	 * The magic happens here
	 *
	 * @param args
	 *            parametri komandne linije
	 */
	public static void main(String[] args) {
		ClassificationNetwork network = new ClassificationNetwork(null, Paths.get("diagnosis.txt"));
		// ClassificationNetwork network = new
		// ClassificationNetwork(Paths.get("ClassificationNetwork"),
		// Paths.get("diagnosis.txt"));

		ClassificationOutput[] cOutputs = new ClassificationOutput[NUMBOF_OUTPUT];
		for (int i = 0; i < NUMBOF_OUTPUT; i++) {
			cOutputs[i] = new ClassificationOutput(0, 1);
		}
		double[] bestThresholds = network.getBestThreshold(network, cOutputs);
		network.test(cOutputs, true);
	}

}
