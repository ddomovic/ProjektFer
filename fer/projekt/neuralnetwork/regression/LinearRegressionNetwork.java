package fer.projekt.neuralnetwork.regression;

import java.util.ArrayList;
import java.util.function.Consumer;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import fer.projekt.neuralnetwork.NeuralNetwork;
import fer.projekt.neuralnetwork.activationfunction.ITransferFunction;
import fer.projekt.neuralnetwork.activationfunction.LinearTransferFunction;
import fer.projekt.neuralnetwork.activationfunction.SigmoidTransferFunction;
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
	public static final int NUMBOF_SAMPLES = 200_000;
	/**
	 * Broj neurona u hidden layeru.
	 */
	public static final int NUMBOF_HID_NEURONS = 200;
	/**
	 * Minimalna težina na konekcijama izmedu prvog i drugog layera.
	 */
	public static final double MIN_WEIGHTS_FIRST_LAYER = -10d;
	/**
	 * Maximalna težina na konekcijama izmedu prvog i drugog layera.
	 */
	public static final double MAX_WEIGHTS_FIRST_LAYER = 10d;
	/**
	 * Funkcija koju mreža uči.
	 */
	public static final IMathFunction LEARNING_FUNC = new IMathFunction() {
		
		@Override
		public String getFuncName() {
			return "cos";
		}
		
		@Override
		public double calculate(double input) {
			return Math.cos(input);
		}
	};
	
	/**
	 * Inicijalizira novu mrezu za učenje neke matematičke funkcije.
	 */
	public LinearRegressionNetwork() {
		super();
		ITransferFunction sigmoid = new SigmoidTransferFunction();
		this.addLayer(new Layer(1, sigmoid, 0));
		this.addLayer(
				new Layer(NUMBOF_HID_NEURONS, sigmoid, 0), 
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
		for (int i = 0; i < NUMBOF_SAMPLES; i++) {
			//0.stupac mora biti = 1
			x[i][0] = 1;
			double rand = Math.random() * 2 * Math.PI;
			y[i] = LEARNING_FUNC.calculate(rand);
			double in[] = {rand};
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
	 * The magic happens here
	 *
	 * @param args parametri komandne linije
	 */
	public static void main(String[] args) {
		LinearRegressionNetwork network = new LinearRegressionNetwork();
		
		int brojTestova = 1000;
		
		double totalPercErr = 0;
		int total = 0;
		System.out.println("\t\t  OCEKIVANO:  DOBIVENO:  POGREŠKA[%]");
		for (int i = 0; i < brojTestova; i++, total++) {
			double in = Math.random() * 2 * Math.PI;
			double correct = LEARNING_FUNC.calculate(in);
			double out = network.run(in);
			double percErr = Math.abs(out - correct) / Math.abs(correct) * 100;
			if (percErr < 100) {
				totalPercErr += percErr;
			}
			System.out.printf("%3d. %s(%.3f)= %7.3f %11.3f \t %6.3f %n", i + 1, LEARNING_FUNC.getFuncName(), in, correct, out, percErr);
		}
		System.out.printf("%nUkupna pogreška je: %.3f%% %n", totalPercErr / total);
	}
	
}
