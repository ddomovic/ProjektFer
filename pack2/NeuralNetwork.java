package pack2;

import java.util.ArrayList;
import java.util.function.Consumer;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

/**
 * Predstavlja osnovnu strukturu neuronske mreže koja može raditi s proizvoljnim brojem
 * layera te transfernom funckijom.
 *
 * @version 1.0
 */
public class NeuralNetwork {
	/**
	 * Lista {@link Layer}-a u mreži
	 */
	private ArrayList<Layer> layerList;

	/**
	 * Inicijalizira neuronsku mrežu s danom tranisfernom funkcijom.
	 * <br> Note: layeri se moraju ručno namjestiti pozivom funkcije {@link #addLayer(int)}
	 *
	 * @param tfunction transferna funkcija
	 */
	public NeuralNetwork() {
		this.layerList = new ArrayList<>();
	}

	public void addLayer(Layer layer) {
		this.addLayer(layer, 0, 1);
	}
	
	public void addLayer(Layer layer, double min, double max) {
		//ako nije prvi layer onda ga spoji sa skroz desnim(zadnjim) layerom
		if (!layerList.isEmpty()) {
			Layer lastLayer = layerList.get(layerList.size() - 1);
			lastLayer.connect(layer, min, max);
		}
		layerList.add(layer);
	}

	/**
	 * Izračunava izlaz neuronske mreže za dani ulaz.
	 *
	 * @param input ulaz u neuronsku mrežu
	 * @return izlaz koji je mreža izračunala
	 */
	public double[] run(double[] input){
		int i = 0;
		Layer InputLayer = layerList.get(0);
		for(Neuron neuron : InputLayer){
			neuron.setInput(input[i]);
			i++;
		}

		Layer lastLayer = layerList.get(layerList.size() - 1);
		double[] output = new double[lastLayer.size()];
		i = 0;
		for(Neuron neuron : lastLayer){
			output[i] = neuron.calculateOutput();
			i++;
		}
		return output;
	}

	/**
	 * The magic happens here
	 *
	 * @param args parametri komandne linije
	 */
	public static void main(String[] args) {
		ITransferFunction tsigmoid = new SigmoidTransferFunction();
		NeuralNetwork network = new NeuralNetwork();
		
		int numberOfSamples = 100000;
		int hiddenNeuronNum = 100;
		
		network.addLayer(new Layer(1, tsigmoid, 0));
		Layer hiddenLayer = new Layer(hiddenNeuronNum, tsigmoid, 0);
		network.addLayer(hiddenLayer, -20d, 20d);
		
		double output[][] = new double[numberOfSamples][hiddenNeuronNum + 1];
		double desiredOutputs[] = new double[numberOfSamples];
		for (int i = 0; i < numberOfSamples; i++) {
			output[i][0] = 1;
			double num = Math.random() * 2 * Math.PI;
			desiredOutputs[i] = Math.sin(num);
			double in[] = {num};
			
			double[] result = network.run(in);
			int j = 1;
			for (double re : result) {
				output[i][j++] = re;
			}
//			System.out.print(num + ", rez: ");
//			for (double d : output[i]) {
//				System.out.print(d + " ");
//			}
//			System.out.println();
		}
		
		OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
		regression.setNoIntercept(true);
		regression.newSampleData(desiredOutputs, output);
		double[] results = regression.estimateRegressionParameters();
		for (double r : results) {
			System.out.println(r);
		}
		
		ITransferFunction tlinear = new LinearTransferFunction();
		Layer outputLayer = new Layer(1, tlinear, 0);
		network.addLayer(outputLayer, 0, 1);
		
		outputLayer.forEach(new Consumer<Neuron>() {
			
			int i = 1;
			
			@Override
			public void accept(Neuron n) {
				ArrayList<Connection> inConnections = n.getAllInConnections();
				inConnections.forEach(c -> {
					c.setWeight(results[i++]);
				});
				n.setBias(results[0]);
			}
		});
		
		for (int i = 0; i < 10; i++) {
			double test[] = {Math.random() * 2 * Math.PI};
			double outTest[] = network.run(test);
			System.out.printf("Ocekivano: sin(%.3f) = %.3f, dobiveno: %.3f%n", test[0], Math.sin(test[0]), outTest[0]);
		}
	}

}
