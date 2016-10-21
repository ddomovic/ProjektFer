package fer.projekt.neuralnetwork.regression;

import java.util.ArrayList;
import java.util.function.Consumer;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import fer.projekt.neuralnetwork.NeuralNetwork;
import fer.projekt.neuralnetwork.activationfunction.ITransferFunction;
import fer.projekt.neuralnetwork.activationfunction.LinearTransferFunction;
import fer.projekt.neuralnetwork.activationfunction.SigmoidTransferFunction;
import fer.projekt.neuralnetwork.activationfunction.WaveTransferFunction;
import fer.projekt.neuralnetwork.elements.Connection;
import fer.projekt.neuralnetwork.elements.Layer;
import fer.projekt.neuralnetwork.elements.Neuron;

/**
 * {@link NeuralNetwork} koji koristi linearnu regresiju za izračunavanje neke matematičke funkcije.
 */
public class LinearRegressionNetwork extends NeuralNetwork {
	/**
	 * Broj testnih primjera za linearnu regresiju.
	 */
	public static int NUMBOF_SAMPLES = 100_000;
	/**
	 * Broj neurona u hidden layeru.
	 */
	public static int NUMBOF_HID_NEURONS = 200;
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
	public static final IMathFunction LEARNING_FUNC = new IMathFunction() {
		
		@Override
		public String getFuncName() {
			return "Sin";
		}
		
		@Override
		public double calculate(double input) {
			
			return Math.sin(input);
		}

		@Override
		public double getDomainMin() {
			return 0;
		}

		@Override
		public double getDomainMax() {
			return 2*Math.PI;
		}
	};
	
	/**
	 * Inicijalizira novu mrezu za učenje neke matematičke funkcije.
	 */
	public LinearRegressionNetwork() {
		super();
		ITransferFunction wavefunction = new WaveTransferFunction("d://ProjektFer//PodaciZaAktivacijskuFunkciju.txt");
		this.addLayer(new Layer(1, wavefunction, 0));
		this.addLayer(
				new Layer(NUMBOF_HID_NEURONS, wavefunction, 0), 
				MIN_WEIGHTS_FIRST_LAYER,
				MAX_WEIGHTS_FIRST_LAYER);
		this.setupNetwork();
	}

	
	/**
	 * Izračunava tezine konekcija neuronske mreže i postavlja ih na konekcije izmedu hidden 
	 * i output layera. Koristi se linearna regresija.
	 */
	public void setupNetwork() {
		double x[][] = new double[NUMBOF_SAMPLES][NUMBOF_HID_NEURONS + 1];
		double y[] = new double[NUMBOF_SAMPLES];
		
		double testInput = LEARNING_FUNC.getDomainMin();
		double step = (LEARNING_FUNC.getDomainMax() - LEARNING_FUNC.getDomainMin()) / NUMBOF_SAMPLES;
		for (int i = 0; i < NUMBOF_SAMPLES; i++, testInput += step) {
			//0.stupac mora biti = 1
			x[i][0] = 1;
			y[i] = LEARNING_FUNC.calculate(testInput);
			double in[] = {testInput};
			//ostali stupci x matrice su outputi hidden neurona
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

		//dodat jos zadnji layer(neuron)
		ITransferFunction tlinear = new LinearTransferFunction();
		Layer outputLayer = new Layer(1, tlinear, 0);
		this.addLayer(outputLayer, 0, 1);	//ovdje je tezina konekcija nebitna jer ju ionako namjestamo
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
	 * @param input ulaz
	 * @return izlaz
	 */
	public double run(double input) {
		double[] in = {input};
		double[] out = this.run(in);
		return out[0];
	}
	
	/**
	 * Testira izgeneriranu mrežu na nekoliko random brojeva i vraća postotak pogreške.
	 * 
	 * @param minInterval minimalni interval na kojemu se testira
	 * @param maxInterval maximalni interval na kojemu se testira
	 * @return postatk pogreške od očekivanog rezultata
	 */
	public double runTests(double minInterval, double maxInterval) {
		int brojTestova = 100;
		
		double totalPercErr = 0;
		int total = 0;
		double testIn = minInterval;
		double step = (maxInterval - minInterval) / brojTestova;
		
		System.out.println("\t\t\tOCEKIVANO: DOBIVENO:    POGREŠKA[%]");
		for (int i = 0; i < brojTestova; i++, total++, testIn += step) {
			double correct = LEARNING_FUNC.calculate(testIn);
			double out = this.run(testIn);
			double percErr = Math.abs(out - correct) / Math.abs(correct) * 100;
			if (correct > 0.00001 ) {
				totalPercErr += percErr;
			}
			System.out.printf("%4d. %s(%.5f)= %10.5f %11.5f \t %6.2f%% %n", i + 1, 
					LEARNING_FUNC.getFuncName(), testIn, correct, out, percErr);
		}
		double avgPerc = totalPercErr / total;
		System.out.printf("%nUkupna pogreška je: %.3f%% %n", avgPerc);
		return avgPerc;
	}
	
	/**
	 * The magic happens here
	 *
	 * @param args parametri komandne linije
	 */
	public static void main(String[] args) {
		LinearRegressionNetwork network = new LinearRegressionNetwork();
		
		//network.runTests(-Math.PI, Math.PI);
		network.runTests(0, 2*Math.PI);
	}
	
}
