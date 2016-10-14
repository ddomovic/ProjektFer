package pack2;

import java.util.ArrayList;

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
		ITransferFunction tlinear = new LinearTransferFunction();
		NeuralNetwork network = new NeuralNetwork();
		network.addLayer(new Layer(1, tsigmoid, 0));
		network.addLayer(new Layer(1000, tsigmoid, 0), -3d, 3d);
		network.addLayer(new Layer(1, tlinear, 0));

		double[] input = {7.0,16.0,3.0};
		double[] output = network.run(input);
		for(double out : output){
			System.out.println(out);
		}
	}

}
