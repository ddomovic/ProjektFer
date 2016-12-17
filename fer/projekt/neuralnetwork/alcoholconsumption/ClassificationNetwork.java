package fer.projekt.neuralnetwork.alcoholconsumption;

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
 *
 * <p> Dataset koji ima 6 inputa i koristi se za izračun mogućih bolesti(upala).
 * <br> {@linkplain https://archive.ics.uci.edu/ml/datasets/Acute+Inflammations}
 *
 */
public class ClassificationNetwork extends NeuralNetwork {
	/**
	 * Broj neurona u hidden layeru.
	 */
	public static final int NUMBOF_HID_NEURONS = 50;
	/**
	 * Minimalna težina na konekcijama izmedu prvog i drugog layera.
	 */
	public static final double MIN_WEIGHTS_FIRST_LAYER = -0.1;
	/**
	 * Maximalna težina na konekcijama izmedu prvog i drugog layera.
	 */
	public static final double MAX_WEIGHTS_FIRST_LAYER = 0.1;
	/**
	 * Brojnik koji zajedno s denumeratorom označava postotak podjele testova za učenje i testiranje.
	 */
	public static final int LEARN_NUMERATOR = 4;
	/**
	 * Nazivnik koji zajedno s denumeratorom označava postotak podjele testova za učenje i testiranje.
	 */
	public static final int LEARN_DENUMERATOR = 5;

	private List<Data> learningDataset;
	private List<Data> testingDataset;
	private ClassificationOutput[] cOutputs;
	private int outputNeuronNumber;

	/**
	 * Stvara novu objekt sa danom putanjom do spremljene mreže i putanjom do dataseta.
	 * <br> Ako je {@code networkSetup} <code>null</code> onda se mreža ponovno generira i automatski sprema u direktorij
	 * projekta pod nazivom @link
	 * @param radiNovuMrezu
	 *
	 */
	public ClassificationNetwork(boolean radiNovuMrezu, String networkName, Path datasetPath, DataConverter converter) {
		super();
		List<Data> dataList = DatasetUtil.loadDataset(datasetPath, converter);
		List<List<Data>> returnList = DatasetUtil.splitDataset(dataList, LEARN_NUMERATOR, LEARN_DENUMERATOR);
		learningDataset = returnList.get(0);
		testingDataset = returnList.get(1);
		Data nekiData = learningDataset.get(0);
		this.outputNeuronNumber = nekiData.getOutput().length;

		ITransferFunction wavefunction = new WaveTransferFunction("PodaciZaAktivacijskuFunkciju.txt");
		cOutputs = new ClassificationOutput[outputNeuronNumber];
		for (int i = 0; i < outputNeuronNumber; i++) {
			cOutputs[i] = new ClassificationOutput(0, 1);
		}

		this.addLayer(new Layer(nekiData.getInput().length, wavefunction, 0));
		this.addLayer(new Layer(10_000, wavefunction, 1), -1, 1);
		this.addLayer(new Layer(NUMBOF_HID_NEURONS, wavefunction, 0), MIN_WEIGHTS_FIRST_LAYER, MAX_WEIGHTS_FIRST_LAYER);

		if (radiNovuMrezu) {
			this.setupNetwork();
			for (ClassificationOutput cO : cOutputs) {
				cO.setThreshold(10);
			}
			//			this.findBestThresholds();
			ClassificationFileUtils.saveClassificationNetwork(this, Paths.get(networkName));
		} else {
			ITransferFunction tlinear = new LinearTransferFunction();
			Layer outputLayer = new Layer(outputNeuronNumber, tlinear, 0);
			this.addLayer(outputLayer, 0, 1);
			ClassificationFileUtils.loadClassificationNetwork(this, Paths.get(networkName));
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
		Data testInput = learningDataset.get(0);
		int outputNeuronNumber = testInput.getOutput().length;

		double x[][] = new double[learningDataset.size()][NUMBOF_HID_NEURONS + 1];
		double y[][] = new double[learningDataset.size()][outputNeuronNumber];

		for (int i = 0; i < learningDataset.size() - 1; i++) {
			testInput = learningDataset.get(i);
			// 0.stupac mora biti = 1
			x[i][0] = 1;
			for (int z = 0; z < outputNeuronNumber; z++) {
				y[i][z] = testInput.getOutput()[z];
			}
			// ostali stupci x matrice su outputi hidden neurona
			double[] result = this.run(testInput.getInput());
			int j = 1;
			for (double re : result) {
				x[i][j++] = re;
			}
		}

		OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
		regression.setNoIntercept(true);

		// dodat jos zadnji layer(neuron)
		ITransferFunction tlinear = new LinearTransferFunction();
		Layer outputLayer = new Layer(outputNeuronNumber, tlinear, 0);
		this.addLayer(outputLayer, 0, 1); // ovdje je tezina konekcija nebitna jer ju ionako namjestamo

		double[] yi = new double[learningDataset.size()];
		for (int i = 0; i < outputNeuronNumber; i++) {
			for (int j = 0; j < learningDataset.size(); j++) {
				yi[j] = y[j][i];
			}
			regression.newSampleData(yi, x);
			double[] weights = regression.estimateRegressionParameters();
			regression.setNoIntercept(true);

			Neuron n = outputLayer.getNeuron(i);
			ArrayList<Connection> inConnections = n.getAllInConnections();
			inConnections.forEach(new Consumer<Connection>() {
				int i = 1;

				@Override
				public void accept(Connection c) {
					c.setWeight(weights[i++]);
				}
			});
			n.setBias(weights[0]);
		}
	}

	/**
	 * Pronalazi najbolje thresholdove i automatski ih postavlja u <code>ClassificationOutput</code> objekte.
	 */
	private void findBestThresholds() {
		int i = 0;
		for (ClassificationOutput cO : cOutputs) {
			cO.setThreshold(findThresholdForOutput(i++));
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
		for (int i = 0; i < learningDataset.size(); i++) {
			Data learnInput = learningDataset.get(i);
			double testIn[] = learnInput.getInput();
			double[] correctArray =  learnInput.getOutput();
			double correct = correctArray[outNeuronNumber];
			double out = this.runAndClassify(cOutputs, testIn)[outNeuronNumber];
			if (correct != out) {
				totalErrCounter++;
			}
		}
		return totalErrCounter / learningDataset.size() * 100;
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
		for (int i = 0; i < testingDataset.size(); i++) {
			Data testInput = testingDataset.get(i);
			double testIn[] = testInput.getInput();
			double[] correct = testInput.getOutput();
			for (int j = 0; j < correct.length; j++) {
				correct[j] = correct[j] < 10 ? 0 : 1;
			}
			double[] out = this.runAndClassify(cOutputs, testIn);
			boolean isti = Arrays.equals(correct, out);
			if (!isti) {
				totalErrCounter++;
			}
			if (doPrint) {
				StringBuffer sb = new StringBuffer();
				sb.append(i+1 + ".\t");
				int j = 0;
				for (double d : out) {
					sb.append(String.format("%.2f %.2f\t", correct[j++], d));
				}
				System.out.println(sb.toString());
			}
		}

		double avgPerc = totalErrCounter / testingDataset.size() * 100;
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
		final boolean radiNovuMrezu = true;
		DataConverter studentConverter = new StudentConverter();
		String datasetPath = "student_mat_dataset.txt";

		ClassificationNetwork network = new ClassificationNetwork(radiNovuMrezu, "AlcoholConsumptionNetwork", Paths.get(datasetPath), studentConverter);
		for (double d : network.getBestThresholds()) {
			System.out.println(d);
		}
		network.test(true);
	}

}
