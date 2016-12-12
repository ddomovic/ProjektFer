package fer.projekt.neuralnetwork.regression;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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

/**
 * {@link NeuralNetwork} koji koristi linearnu regresiju za izračunavanje neke
 * matematičke funkcije.
 */
public class LinearRegressionNetwork extends NeuralNetwork {
	/**
	 * Broj testnih primjera za linearnu regresiju.
	 */
	public static int NUMBOF_SAMPLES = 1_000;
	/**
	 * Broj neurona u hidden layeru.
	 */
	public static int NUMBOF_HID_NEURONS = 25;
	/**
	 * Minimalna težina na konekcijama izmedu prvog i drugog layera.
	 */
	public static double MIN_WEIGHTS_FIRST_LAYER = -10d;
	/**
	 * Maximalna težina na konekcijama izmedu prvog i drugog layera.
	 */
	public static double MAX_WEIGHTS_FIRST_LAYER = 10d;
	/**
	 * Funkcija koju mreža uči.
	 */
	public static final IMathFunction LEARNING_FUNC = new ClassificationFunction();

	/**
	 * Inicijalizira novu mrezu za učenje neke matematičke funkcije.
	 */
	public LinearRegressionNetwork(Path fileName) {
		super();
		// ITransferFunction wavefunction = new
		// WaveTransferFunction("D:/ProjektFer/PodaciZaAktivacijskuFunkciju.txt");
		//ITransferFunction wavefunction = new WaveTransferFunction(
				//"C:/Users/David/Desktop/git/ProjektFer/PodaciZaAktivacijskuFunkciju.txt");
		ITransferFunction wavefunction = new WaveTransferFunction("/home/ProjektFer/PodaciZaAktivacijskuFunkciju.txt");
		this.addLayer(new Layer(1, wavefunction, 0));
		this.addLayer(new Layer(10_000, wavefunction, 1), -1, 1);
		this.addLayer(new Layer(NUMBOF_HID_NEURONS, wavefunction, 0), MIN_WEIGHTS_FIRST_LAYER, MAX_WEIGHTS_FIRST_LAYER);

		if (fileName == null) {
			this.setupNetwork();
			FileUtils.saveNetwork(this, "LinearRegressionNetwork");
		} else {
			ITransferFunction tlinear = new LinearTransferFunction();
			Layer outputLayer = new Layer(1, tlinear, 0);
			this.addLayer(outputLayer, 0, 1);
			FileUtils.loadNeuralNetwork(this, fileName);
		}
	}

	/**
	 * Izračunava tezine konekcija neuronske mreže i postavlja ih na konekcije
	 * izmedu hidden i output layera. Koristi se linearna regresija.
	 */
	public void setupNetwork() {
		double x[][] = new double[NUMBOF_SAMPLES][NUMBOF_HID_NEURONS + 1];
		double y[] = new double[NUMBOF_SAMPLES];

		double testInput = LEARNING_FUNC.getDomainMin();
		double step = (LEARNING_FUNC.getDomainMax() - LEARNING_FUNC.getDomainMin()) / NUMBOF_SAMPLES;
		for (int i = 0; i < NUMBOF_SAMPLES; i++, testInput += step) {
			// 0.stupac mora biti = 1
			x[i][0] = 1;
			y[i] = LEARNING_FUNC.calculate(testInput);
			double in[] = { testInput };
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
	 * Izračunava izlaz neuronske mreže za tocno jedan ulaz.
	 *
	 * @param input
	 *            ulaz
	 * @return izlaz
	 */
	public double run(double input) {
		double[] in = { input };
		double[] out = this.run(in);
		return out[0];
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
	public double run(double clas1, double clas2, double treshhold, double input) {
		double output = run(input);
		if (output <= treshhold) {
			output = clas1;
		} else {
			output = clas2;
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
	public double runTests(boolean takeRandomTests) {
		int brojTestova = 100;

		double totalPercErr = 0;
		int total = 0;
		double testIn = LEARNING_FUNC.getDomainMin();
		double step = (LEARNING_FUNC.getDomainMax() - LEARNING_FUNC.getDomainMin()) / brojTestova;

		System.out.println("\t\t\tOCEKIVANO: DOBIVENO:    POGREŠKA[%]");
		for (int i = 0; i < brojTestova; i++, total++, testIn += step) {
			if (takeRandomTests) {
				testIn = Math.random() * (LEARNING_FUNC.getDomainMax() - LEARNING_FUNC.getDomainMin());
			}
			double correct = LEARNING_FUNC.calculate(testIn);
			double out = this.run(testIn);
			double percErr = Math.abs(out - correct) / Math.abs(correct) * 100;
			if (correct > 0.00001) {
				totalPercErr += percErr;
			}
			System.out.printf("%4d. %s(%.5f)= \t%.10f \t%.10f \t %.3f%% %n", i + 1, LEARNING_FUNC.getFuncName(), testIn,
					correct, out, percErr);
		}
		double avgPerc = totalPercErr / total;
		System.out.printf("%nUkupna pogreška je: %.5f%% %n", avgPerc);
		return avgPerc;
	}

	/**
	 * Testira izgeneriranu mrežu na nekoliko random brojeva i vraća postotak
	 * pogreške.
	 *
	 * @param clas1
	 * @param clas2
	 * @param treshhold
	 * @param takeRandomTests
	 *            označava da li su input testovi generirani random ili linearno
	 *            na intervalu
	 * @param print
	 *            - želimo li isprintati rezultate
	 * @return postotak pogreške od očekivanog rezultata
	 */
	public double runTests(double clas1, double clas2, double treshhold, boolean takeRandomTests, boolean print) {
		int brojTestova = 100;

		double totalErrNumb = 0;
		int total = 0;
		double testIn = LEARNING_FUNC.getDomainMin();
		double step = (LEARNING_FUNC.getDomainMax() - LEARNING_FUNC.getDomainMin()) / brojTestova;

		if (print) {
			System.out.println("\t\t\t\t\tOCEKIVANO:\tDOBIVENO:        točna klasifikacija?:");
		}
		for (int i = 0; i < brojTestova; i++, total++, testIn += step) {
			if (takeRandomTests) {
				testIn = Math.random() * (LEARNING_FUNC.getDomainMax() - LEARNING_FUNC.getDomainMin());
			}
			double correct = LEARNING_FUNC.calculate(testIn);
			double out = this.run(clas1, clas2, treshhold, testIn);
			if (correct != out) {
				totalErrNumb++;
			}
			if (print) {
				System.out.printf("%4d. %s(%.5f)= \t%.10f \t%.10f \t %.5s %n", i + 1, LEARNING_FUNC.getFuncName(),
						testIn, correct, out, !(correct != out));
			}
		}
		double avgPerc = totalErrNumb / total * 100;
		if (print) {
			System.out.printf("%nUkupna pogreška je: %.5f%% %n", avgPerc);
		}
		return avgPerc;
	}

	/**
	 * The magic happens here
	 *
	 * @param args
	 *            parametri komandne linije
	 */
	public static void main(String[] args) {
		// LinearRegressionNetwork network = new LinearRegressionNetwork(null);
		LinearRegressionNetwork network = new LinearRegressionNetwork(Paths.get("LinearRegressionNetwork"));
		// network.runTests(false);

		double bestThreshold = network.getBestThreshold(network, 0, 1);
		System.out.println("najbolji threshold " + bestThreshold);
		network.runTests(0, 1, bestThreshold, false, true);
	}

	private double getBestThreshold(LinearRegressionNetwork network, double clas1, double clas2) {
		double treshholdpom;
		double pom;
		double best = network.runTests(clas1, clas2, clas1, false, false);
		double treshhold = clas1;
		for (treshholdpom = clas1; treshholdpom <= clas2; treshholdpom += 0.01) {
			pom = network.runTests(clas1, clas2, treshholdpom, false, false);
			if (pom < best) {
				treshhold = treshholdpom;
				best = pom;
			}

		}
		return treshhold;
	}

}
