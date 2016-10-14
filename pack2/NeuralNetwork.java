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
	 * {@link ITransferFunction} kojom se izračunava izlaz neurona
	 */
	private ITransferFunction tfunction;

	/**
	 * Inicijalizira neuronsku mrežu s danom transfernom funkcijom.
	 * <br> Note: layeri se moraju ručno namjestiti pozivom funkcije {@link #addLayer(int)}
	 *
	 * @param tfunction transferna funkcija
	 */
	public NeuralNetwork() {
		this.layerList = new ArrayList<>();
	}

	/**
	 * Dodaje novi layer zadane veličnine neuronskoj mreži.
	 * <br> Layer se automatski spoji sa posljednjim u mreži ako takav postoji.
	 *
	 * @param numberOfNeurons velicina novog layera
	 */
	public void addLayer(int numberOfNeurons, ITransferFunction tfunction) {
		Layer layer = new Layer(numberOfNeurons, tfunction);
		//ako nije prvi layer onda ga spoji sa skroz desnim(zadnjim) layerom
		if (!layerList.isEmpty()) {
			Layer lastLayer = layerList.get(layerList.size() - 1);
			lastLayer.connect(layer);
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
		network.addLayer(3, tsigmoid);
		network.addLayer(4, tsigmoid);
		network.addLayer(2, tlinear);

		double[] input = {7.0,16.0,3.0};
		double[] output = network.run(input);
		for(double out : output){
			System.out.println(out);
		}
	}

}
