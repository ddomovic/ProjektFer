package fer.projekt.neuralnetwork.elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fer.projekt.neuralnetwork.activationfunction.ITransferFunction;

/**
 * Predstavlja jedan layer u neronskoj mreži.
 *
 * @version 1.0
 */
public class Layer implements Iterable<Neuron> {

	public static int COUNTER = 1;
	
	private int index = COUNTER++;
	/**
	 * Lista {@link Neuron}-a koje layer sadrži
	 */
	private List<Neuron> neuronList;
	
	/**
	 * Inicijalizira layer s zadanim brojem neurona. Svakom od neurona daje istu aktivacijsku 
	 * funkciju i random bias izmedu vrijednosti {@code minBias} - {@code maxBias}.
	 *
	 * @param numberOfNeurons broj neurona u layeru
	 * @param tfunction transferna funkcija neurona
	 * @param minBias minimalna vrijednost biasa neurona
	 * @param maxBias maksimalna vrijednost biasa neurona
	 */
	public Layer(int numberOfNeurons, ITransferFunction tfunction, double minBias, double maxBias){
		this.neuronList = new ArrayList<>();
		for (int i= 0; i < numberOfNeurons; i++) {
			Neuron n = new Neuron(tfunction, minBias, maxBias);
			neuronList.add(n);
		}
	}
	
	public Neuron getNeuron(int index) {
		return neuronList.get(index);
	}
	
	
	/**
	 * Inicijalizira layer s zadanim brojem neurona. Svakom od neurona daje istu aktivacijsku 
	 * funkciju i tocno definiran bias.
	 * 
	 * @param numberOfNeurons broj neurona u layeru
	 * @param tfunction transferna funkcija neurona
	 * @param bias vrijednost biasa za svaki neuron u layeru
	 */
	public Layer(int numberOfNeurons, ITransferFunction tfunction, double bias){
		this(numberOfNeurons, tfunction, bias, bias);
	}
	
	public Layer() {
		this.neuronList = new ArrayList<>();
	}
	
	/**
	 * Stvara layer od danih neurona.
	 *
	 * @param neurons neuroni
	 */
	public Layer(Neuron ... neurons) {
		this.neuronList = new ArrayList<>();
		for (Neuron n : neurons) {
			neuronList.add(n);
		}
	}
	
	public int getIndex() {
		return index;
	}
	
	/**
	 * Vraća broj neurona u layeru.
	 *
	 * @return velicinu
	 */
	public int size(){
		return neuronList.size();
	}

	/**
	 * Spaja trenutni layer s danim. Neuroni ovog layera su lijevi u stvorenim {@link Connection}-ima.
	 *
	 * @param rightLayer desni layer s kojim se spaja
	 * @param minWeight minimalna tezina konekcije
	 * @param maxWeight maksimalna tezina konekcije
	 */
	public void connect(Layer rightLayer, double minWeight, double maxWeight) {
		neuronList.forEach(left -> {
			rightLayer.forEach(right -> {
				Connection con = new Connection(left, right, minWeight, maxWeight);
				right.addInConnection(con);
			});
		});
	}

	@Override
	public Iterator<Neuron> iterator() {
		return neuronList.iterator();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Layer other = (Layer) obj;
		if (index != other.index) {
			return false;
		}
		return true;
	}

}
