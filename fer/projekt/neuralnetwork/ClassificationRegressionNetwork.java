package fer.projekt.neuralnetwork;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import fer.projekt.neuralnetwork.activationfunction.ITransferFunction;
import fer.projekt.neuralnetwork.activationfunction.LinearTransferFunction;
import fer.projekt.neuralnetwork.activationfunction.WaveTransferFunction;
import fer.projekt.neuralnetwork.elements.Connection;
import fer.projekt.neuralnetwork.elements.Layer;
import fer.projekt.neuralnetwork.elements.Neuron;
import fer.projekt.neuralnetwork.utils.ClassificationFileUtils;
import fer.projekt.neuralnetwork.utils.ClassificationOutput;
import fer.projekt.neuralnetwork.utils.Data;
import fer.projekt.neuralnetwork.utils.DataConverter;
import fer.projekt.neuralnetwork.utils.DatasetUtil;
import fer.projekt.neuralnetwork.utils.RegressionFileUtils;

/**
 * Razred koji predstavlja neuronsku mrežu koja služi za klasifikaciju.
 *
 * <p>
 * Dataset koji ima 6 inputa i koristi se za izračun mogućih bolesti(upala).
 * <br>
 */
public class ClassificationRegressionNetwork extends NeuralNetwork {
	/**
	 * Broj neurona u hidden layeru.
	 */
	public static final int NUMBOF_HID_NEURONS = 40;
	/**
	 * Minimalna težina na konekcijama izmedu prvog i drugog layera.
	 */
	public static final double MIN_WEIGHTS_FIRST_LAYER = 0;
	/**
	 * Maximalna težina na konekcijama izmedu prvog i drugog layera.
	 */
	public static final double MAX_WEIGHTS_FIRST_LAYER = 0.1;
	/**
	 * Brojnik koji zajedno s denumeratorom označava postotak podjele testova za
	 * učenje i testiranje.
	 */
	public static final int LEARN_NUMERATOR = 9;
	/**
	 * Nazivnik koji zajedno s denumeratorom označava postotak podjele testova
	 * za učenje i testiranje.
	 */
	public static final int LEARN_DENUMERATOR = 10;

	private List<Data> learningDataset;
	private List<Data> testingDataset;

	private ClassificationOutput[] cOutputs;
	private int outputNeuronNumber;
	private DataConverter converter;
	private boolean radiRegresiju;

	/**
	 * Stvara novu objekt sa danom putanjom do spremljene mreže i putanjom do
	 * dataseta. <br>
	 * Ako je {@code networkSetup} <code>null</code> onda se mreža ponovno
	 * generira i automatski sprema u direktorij projekta pod nazivom @link
	 *
	 * @param radiNovuMrezu
	 * @param radiKlasifikaciju
	 *
	 */
	public ClassificationRegressionNetwork(boolean radiNovuMrezu, ClassificationOutput[] cOutputs, String networkName,
			Path datasetPath, DataConverter converter) {
		super();
		List<Data> dataList = DatasetUtil.loadDataset(datasetPath, converter);
		List<List<Data>> returnList = DatasetUtil.splitDataset(dataList, LEARN_NUMERATOR, LEARN_DENUMERATOR);
		this.learningDataset = returnList.get(0);
		this.testingDataset = returnList.get(1);
		this.outputNeuronNumber = dataList.get(0).getOutput().length;
		this.converter = converter;
		this.cOutputs = cOutputs;
		this.radiRegresiju = cOutputs == null;

		normalize(learningDataset, true);
		normalize(testingDataset, false);

		ITransferFunction wavefunction = new WaveTransferFunction("PodaciZaAktivacijskuFunkciju.txt");
		this.addLayer(new Layer(dataList.get(0).getInput().length, wavefunction, 0));
		this.addLayer(new Layer(NUMBOF_HID_NEURONS, wavefunction, 0), MIN_WEIGHTS_FIRST_LAYER, MAX_WEIGHTS_FIRST_LAYER);

		if (radiNovuMrezu) {
			this.setupNetwork();
			if (!radiRegresiju) {
				learningDataset.forEach(d -> denormalizeOutput(d.getOutput()));
				this.findBestThresholds();
				ClassificationFileUtils.saveClassificationNetwork(this, Paths.get(networkName));
			} else {
				RegressionFileUtils.saveNetwork(this, Paths.get(networkName));
			}
		} else {
			ITransferFunction tlinear = new LinearTransferFunction();
			Layer outputLayer = new Layer(outputNeuronNumber, tlinear, 0);
			this.addLayer(outputLayer, 0, 1);
			if (!radiRegresiju) {
				ClassificationFileUtils.loadClassificationNetwork(this, Paths.get(networkName));
			} else {
				RegressionFileUtils.loadNeuralNetwork(this, Paths.get(networkName));
			}
		}
	}

	/**
	 * @param output
	 * @return
	 */
	private double[] denormalizeOutput(double[] output) {
		Double[] outMinValues = converter.getOutMinValues();
		Double[] outMaxValues = converter.getOutMaxValues();
		for(int i = 0; i< output.length; i++) {
			output[i] = output[i]*(outMaxValues[i] - outMinValues[i]) + outMinValues[i];
		}
		return output;
	}

	/**
	 * @param dataList
	 */
	private void normalize(List<Data> dataList, boolean normalizeOutputs) {
		Double[] inMinValues = converter.getInMinValues();
		Double[] inMaxValues = converter.getInMaxValues();
		Double[] outMinValues = converter.getOutMinValues();
		Double[] outMaxValues = converter.getOutMaxValues();

		dataList.forEach(d -> {
			double[] input = d.getInput();
			for (int i = 0; i < input.length; i++) {
				input[i] = (input[i] - inMinValues[i]) / (inMaxValues[i] - inMinValues[i]);
			}
			if (normalizeOutputs) {
				double[] output = d.getOutput();
				for (int i = 0; i < output.length; i++) {
					output[i] = (output[i] - outMinValues[i]) / (outMaxValues[i] - outMinValues[i]);
				}
			}
		});
	}

	/**
	 * Getter za polje najbojih thresholdova.
	 *
	 * @return polje najboljih thresholdova izlaznog layera
	 */
	public double[] getBestThresholds() {
		if (radiRegresiju) {
			throw new IllegalArgumentException("Mreža se koristi kao regresija!");
		}
		double[] bestThresholds = new double[cOutputs.length];
		int i = 0;
		for (ClassificationOutput cO : cOutputs) {
			bestThresholds[i++] = cO.getBestThreshold();
		}
		return bestThresholds;
	}

	/**
	 * Setter za postavljanje najboljih thresholdova izlaznog layera
	 *
	 * @param bestThresholds
	 *            polje najboljih thresholdova
	 */
	public void setBestThresholds(double[] bestThresholds) {
		int i = 0;
		for (ClassificationOutput cO : cOutputs) {
			cO.setBestThreshold(bestThresholds[i++]);
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
		this.addLayer(outputLayer, 0, 1); // ovdje je tezina konekcija nebitna
		// jer ju ionako namjestamo

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
	 * Pronalazi najbolje thresholdove i automatski ih postavlja u
	 * <code>ClassificationOutput</code> objekte.
	 */
	private void findBestThresholds() {
		int i = 0;
		for (ClassificationOutput cO : cOutputs) {
			cO.setBestThreshold(findBestThresholdForOutput(i++));
		}
	}

	/**
	 * Vraca najbolji threshold za neuron indexa j.
	 *
	 * @param outNeuronNumber
	 *            index neurona u output layeru
	 * @param classificationOutput
	 *            lista {@link ClassificationOutput} razreda za izlazni sloj
	 * @return double vrijednost koja predstavlja najbolji threshold za dani
	 *         j-ti neuron
	 */
	private double findBestThresholdForOutput(int outNeuronNumber) {
		ClassificationOutput cOut = cOutputs[outNeuronNumber];
		final int brojStepova = 100;
		final double step = (cOut.getThreshEnd() - cOut.getThreshStart()) / brojStepova;

		double perc;
		double bestPerc = Double.MAX_VALUE;
		double bestThreshold = 0;

		for (int i = 0; i < brojStepova; i++) {
			perc = this.testOutNeruonForThreshold(outNeuronNumber);
			if (perc < bestPerc) {
				bestPerc = perc;
				bestThreshold = cOut.getBestThreshold();
			}
			cOut.setBestThreshold(cOut.getBestThreshold() + step);
		}
		return bestThreshold;
	}

	/**
	 * Vraća postotak pogreške koji dobivamo za dani threshold na neuronu s
	 * indexom <code>outNeuronNumber</code>.
	 *
	 * @param outNeuronNumber
	 *            index neurona u output layeru
	 * @return postatak pogreske
	 */
	private double testOutNeruonForThreshold(int outNeuronNumber) {
		double totalErrCounter = 0;
		for (int i = 0; i < learningDataset.size(); i++) {
			Data learnInput = learningDataset.get(i);
			double testIn[] = learnInput.getInput();
			double[] correctArray = learnInput.getOutput();
			ClassificationOutput cOut = cOutputs[outNeuronNumber];
			double zeljeniThreshold = (cOut.getThreshEnd() - cOut.getThreshStart()) / 2;
			double correct = correctArray[outNeuronNumber] < zeljeniThreshold ? 0 : 1;
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
	 * @param cOutputs
	 *            polje {@link ClassificationOutput} objekata koji sluze za
	 *            klasifikaciju
	 * @param input
	 *            Ulaz funkcije čiji izlaz računamo
	 * @return output kao vrijednost clas1 ili clas2
	 */
	public double[] runAndClassify(ClassificationOutput[] cOutputs, double[] input) {
		double[] output = run(input);
		output = denormalizeOutput(output);
		for (int i = 0; i < cOutputs.length; i++) {
			ClassificationOutput cOut = cOutputs[i];
			if (output[i] <= cOut.getBestThreshold()) {
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
	 * @param cOutputs
	 *            polje {@link ClassificationOutput} objekata koji sluze za
	 *            klasifikaciju
	 * @param doPrint
	 *            ako je true onda printa testove, inače ne
	 * @return postotak pogreske u %
	 */
	public double test(boolean doPrint) {
		if (doPrint) {
			System.out.println("Rezultati:");
		}
		if (radiRegresiju) {
			return testirajRegresiju(doPrint);
		} else {
			return testirajKlasifikaciju(doPrint);
		}
	}

	/**
	 * @param doPrint
	 *
	 */
	private double testirajKlasifikaciju(boolean doPrint) {
		double totalErrCounter = 0;
		for (int i = 0; i < testingDataset.size(); i++) {
			Data testInput = testingDataset.get(i);
			double testIn[] = testInput.getInput();
			double[] correct = testInput.getOutput();
			for (int j = 0; j < correct.length; j++) {
				ClassificationOutput cOut = cOutputs[j];
				double zeljeniThreshold = (cOut.getThreshEnd() - cOut.getThreshStart()) / 2;
				correct[j] = correct[j] < zeljeniThreshold ? 0 : 1;
			}
			double[] out = this.runAndClassify(cOutputs, testIn);
			boolean isti = Arrays.equals(correct, out);
			if (!isti) {
				totalErrCounter++;
			}

			if (doPrint) {
				StringBuffer sb = new StringBuffer();
				sb.append(i + 1 + ".\t");
				int j = 0;
				for (double d : out) {
					sb.append(String.format("%.2f %.2f\t", correct[j++], d));
				}
				if (!isti) {
					sb.append("<--krivo");
				}
				System.out.println(sb.toString());
			}
		}
		double avgPerc = totalErrCounter / testingDataset.size() * 100;
		if (doPrint) {
			System.out.printf("%nUkupna pogreška klasifikacije je: %.5f%% %n", avgPerc);
		}
		return avgPerc;
	}

	/**
	 * @param doPrint
	 *
	 */
	private double testirajRegresiju(boolean doPrint) {
		double[] RMSE = new double[outputNeuronNumber];
		for (int i = 0; i < testingDataset.size(); i++) {
			Data testInput = testingDataset.get(i);
			double in[] = testInput.getInput();
			double[] correct = testInput.getOutput();
			double[] out = run(in);
			out = denormalizeOutput(out);

			for (int j = 0; j < correct.length; j++) {
				RMSE[j] += Math.pow(correct[j] - out[j], 2);
				if (doPrint) {
					System.out.printf("%.2f %.2f\t", correct[j], out[j]);
				}
			}
			if (doPrint) {
				System.out.println();
			}
		}
		double totalRMSE = 0;
		for (int i = 0; i < RMSE.length; i++) {
			RMSE[i] = Math.sqrt(RMSE[i] / testingDataset.size());
			totalRMSE = Math.pow(RMSE[i], 2);
		}
		totalRMSE = Math.sqrt(totalRMSE);

		System.out.printf("%nRMSE[i]: ");
		for (double rm : RMSE) {
			System.out.printf("%.2f ", rm);
		}
		System.out.printf("%ntotalRMSE = %.2f", totalRMSE);
		return totalRMSE;
	}

}
