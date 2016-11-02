package fer.projekt.neuralnetwork;

import java.util.ArrayList;

import fer.projekt.neuralnetwork.elements.Connection;
import fer.projekt.neuralnetwork.elements.Layer;
import fer.projekt.neuralnetwork.elements.Neuron;

/**
 * Predstavlja osnovnu strukturu neuronske mreže koja može raditi s proizvoljnim brojem
 * layera te transfernom funckijom koja se definira na razini svakog layera.
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
	 * <br> Note: layeri se moraju ručno namjestiti pozivom funkcije {@link #addLayer(Layer)}
	 *
	 */
	public NeuralNetwork() {
		this.layerList = new ArrayList<>();
	}

	/**
	 * Dodaje novi layer u mrežu i spaja ga s prijašnjim.
	 * <br> Tezine {@link Connection}-a koji ga spajaju s prijasnjim su random vrijednosti izmedu 0 i 1.
	 * 
	 * @param layer zadnji(najdesniji) {@link Layer} u mrezi
	 */
	public void addLayer(Layer layer) {
		this.addLayer(layer, 0, 1);
	}
	
	/**
	 * Dodaje novi layer u mrežu i spaja ga s prijašnjim.
	 * <br> Tezine {@link Connection}-a koji ga spajaju s prijasnjim su random vrijednosti izmedu 0 i 1.
	 *
	 * @param layer zadnji(najdesniji) {@link Layer} u mrezi
	 * @param minWeight minimalna tezina konekcije s prijasnjiim layerom
	 * @param maxWeight maximalna tezina konekcije s prijasnim layerom
	 * 
	 */
	public void addLayer(Layer layer, double minWeight, double maxWeight) {
		if (!layerList.isEmpty()) {
			Layer lastLayer = layerList.get(layerList.size() - 1);
			lastLayer.connect(layer, minWeight, maxWeight);
		}
		layerList.add(layer);
	}

	/**
	 * Izračunava izlaz neuronske mreže za dani ulaz.
	 *
	 * @param input ulaz u neuronsku mrežu
	 * @return izlaz koji je mreža izračunala
	 */
	public double[] run(double[] input) {
		if (input.length != layerList.get(0).size()) {
			throw new IllegalArgumentException("Velicina inputa u mrezi treba odgovarati velicini "
				+ "input layera!");
		}
		
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
		this.reset();
		return output;
	}
	/**
	 * Metoda koja služi za resetiranje mreže nakon 
	 */
	public void reset(){
		for(Layer layer : layerList){
			for(Neuron neuron : layer){
				neuron.reset();
			}
		}
	}

}
