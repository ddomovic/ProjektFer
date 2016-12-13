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

/**
 * Razred koji predstavlja neuronsku mrežu koja služi za klasifikaciju.
 */
public class ClassificationNetwork extends NeuralNetwork {
	/**
	 * Broj neurona u hidden layeru.
	 */
	public static final int NUMBOF_HID_NEURONS = 30;
	/**
	 * Broj neurona u output layeru.
	 */
	public static final int NUMBOF_OUTPUT = 2;
	/**
	 * Minimalna težina na konekcijama izmedu prvog i drugog layera.
	 */
	public static final double MIN_WEIGHTS_FIRST_LAYER = -10d;
	/**
	 * Maximalna težina na konekcijama izmedu prvog i drugog layera.
	 */
	public static final double MAX_WEIGHTS_FIRST_LAYER = 10d;
	/**
	 * Brojnik koji zajedno s denumeratorom označava postotak podjele testova za učenje i testiranje.
	 */
	public static final int LEARN_NUMERATOR = 4;
	/**
	 * Nazivnik koji zajedno s denumeratorom označava postotak podjele testova za učenje i testiranje.
	 */
	public static final int LEARN_DENUMERATOR = 5;
	/**
	 * Ime pod kojim se network sprema u direktorij projekta.
	 */
	public static final String NETWORK_NAME = "ClassificationNetwork";

	private int learningDatasetSize;
	private List<Data> learningDataset;
	private int testingDatasetSize;
	private List<Data> testingDataset;
	private ClassificationOutput[] cOutputs;

	/**
	 * Stvara novu objekt sa danom putanjom do spremljene mreže i putanjom do dataseta.
	 * <br> Ako je {@code networkSetup} <code>null</code> onda se mreža ponovno generira i automatski sprema u direktorij
	 * projekta pod nazivom @link
	 *
	 * @param networkSetup
	 * @param datasetPath
	 */
	public ClassificationNetwork(Path networkSetup, Path datasetPath) {
		super();
		//ITransferFunction wavefunction = new WaveTransferFunction("/home/ProjektFer/PodaciZaAktivacijskuFunkciju.txt");
		//ITransferFunction wavefunction = new WaveTransferFunction("D:/ProjektFer/PodaciZaAktivacijskuFunkciju.txt");
		ITransferFunction wavefunction = new WaveTransferFunction("C:/Users/David/Desktop/git/ProjektFer/PodaciZaAktivacijskuFunkciju.txt");
		cOutputs = new ClassificationOutput[NUMBOF_OUTPUT];
		for (int i = 0; i < NUMBOF_OUTPUT; i++) {
			cOutputs[i] = new ClassificationOutput(0, 1);
		}

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
			this.findBestThresholds();
			ClassificationFileUtils.saveClassificationNetwork(this, Paths.get(NETWORK_NAME));
		} else {
			ITransferFunction tlinear = new LinearTransferFunction();
			Layer outputLayer = new Layer(NUMBOF_OUTPUT, tlinear, 0);
			this.addLayer(outputLayer, 0, 1);
			ClassificationFileUtils.loadClassificationNetwork(this, networkSetup);
		}
	}

	/**
	 * Getter za polje najbojih thresholdova.
	 *
	 * @return polje najboljih thresholdova izlaznog layera
	 */
	public double[] getBestThresholds() {
		double[] bestThresholds = new double[cOutputs.length];
		int i = 0;
		for (ClassificationOutput cO : cOutputs) {
			bestThresholds[i++] = cO.getThreshold();
		}
		return bestThresholds;
	}

	/**
	 * Setter za postavljanje najboljih thresholdova izlaznog layera
	 *
	 * @param bestThresholds polje najboljih thresholdova
	 */
	public void setBestThresholds(double[] bestThresholds) {
		int i = 0;
		for (ClassificationOutput cO : cOutputs) {
			cO.setThreshold(bestThresholds[i++]);
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
	 * Pronalazi najbolje thresholdove i automatski ih postavlja u <code>ClassificationOutput</code> objekte.
	 */
	private void findBestThresholds() {
		double[] bestThresholds = this.getBestThresholds();
		for (int i = 0; i < bestThresholds.length; i++) {
			bestThresholds[i] = findThresholdForOutput(i);
		}

		int i = 0;
		for (ClassificationOutput cO : cOutputs) {
			cO.setThreshold(bestThresholds[i++]);
		}
	}

	/**
	 * Vraca najbolji threshold za neuron indexa j.
	 *
	 * @param outNeuronNumber index neurona u output layeru
	 * @param classificationOutput lista {@link ClassificationOutput} razreda za izlazni sloj
	 * @return double vrijednost koja predstavlja najbolji threshold za dani j-ti neuron
	 */
	private double findThresholdForOutput(int outNeuronNumber) {
		ClassificationOutput cOut = cOutputs[outNeuronNumber];
		final int brojStepova = 100;
		final double step = (cOut.getClas2() - cOut.getClas1()) / brojStepova;

		double perc;
		double bestPerc = Double.MAX_VALUE;
		double bestThreshold = cOut.getClas1();

		for (int i = 0; i < brojStepova; i++) {
			perc = this.testOutNeuron(outNeuronNumber);
			if (perc < bestPerc) {
				bestPerc = perc;
				bestThreshold = cOut.getThreshold();
			}
			cOut.setThreshold(cOut.getThreshold() + step);
		}
		return bestThreshold;
	}

	/**
	 * Vraća postotak pogreške na datasetu na učenje za dani neuron u output layeru s indexom <code>outNeuronNumber</code>.
	 *
	 * @param outNeuronNumber index neurona u output layeru
	 * @return postatak pogreske
	 */
	private double testOutNeuron(int outNeuronNumber) {
		double totalErrCounter = 0;
		for (int i = 0; i < learningDatasetSize; i++) {
			Data learnInput = learningDataset.get(i);
			double testIn[] = { learnInput.getTemperature(), learnInput.getNausea(), learnInput.getLumbPain(),
					learnInput.getUrinePush(), learnInput.getMictPain(), learnInput.getBurn() };

			double[] correctArray = {learnInput.getInflammation(), learnInput.getNephritis()};
			double correct = correctArray[outNeuronNumber];
			double out = this.runAndClassify(cOutputs, testIn)[outNeuronNumber];
			if (correct != out) {
				totalErrCounter++;
			}
		}
		return totalErrCounter / learningDatasetSize * 100;
	}

	/**
	 * Metoda koju koristimo kada želimo binarnu klasifikaciju
	 *
	 * @param cOutputs polje {@link ClassificationOutput} objekata koji sluze za klasifikaciju
	 * @param input Ulaz funkcije čiji izlaz računamo
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

	/** Testira izgeneriranu mrežu na nekoliko random brojeva i vraća postotak
	 * pogreške.
	 *
	 * @param cOutputs polje {@link ClassificationOutput} objekata koji sluze za klasifikaciju
	 * @param doPrint ako je true onda printa testove, inače ne
	 * @return postotak pogreske u %
	 */
	public double test(boolean doPrint) {
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

	/**
	 * The magic happens here
	 *
	 * @param args parametri komandne linije
	 */
	public static void main(String[] args) {
		final boolean radiNovuMrezu = false;

		ClassificationNetwork network = null;
		if (radiNovuMrezu) {
			network = new ClassificationNetwork(null, Paths.get("diagnosis.txt"));
		} else {
			network = new ClassificationNetwork(Paths.get(NETWORK_NAME), Paths.get("diagnosis.txt"));
		}
		for (double d : network.getBestThresholds()) {
			System.out.println(d);
		}
		network.test(true);
	}

}
