package pack2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Predstavlja jedan layer u neronskoj mreži.
 *
 * @version 1.0
 */
public class Layer implements Iterable<Neuron> {

	/**
	 * Lista {@link Neuron}-a koje layer sadrži
	 */
	private List<Neuron> neuronList;

	/**
	 * Inicijalizira layer s danom transfernom funkcijom i brojem neurona.
	 *
	 * @param numberOfNeurons broj neurona u layeru
	 * @param tfunction transferna funkcija neurona
	 */
	public Layer(int numberOfNeurons, ITransferFunction tfunction, double minBias, double maxBias){
		this.neuronList = new ArrayList<>();
		for (int i= 0; i < numberOfNeurons; i++) {
			neuronList.add(new Neuron(tfunction, minBias, maxBias));
		}
	}
	
	public Layer(int numberOfNeurons, ITransferFunction tfunction, double bias){
		this.neuronList = new ArrayList<>();
		for (int i= 0; i < numberOfNeurons; i++) {
			neuronList.add(new Neuron(tfunction, bias));
		}
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
	 */
	public void connect(Layer rightLayer, double min, double max) {
		neuronList.forEach(left -> {
			rightLayer.forEach(right -> {
				Connection con = new Connection(left, right, min, max);
			});
		});
	}

	@Override
	public Iterator<Neuron> iterator() {
		return neuronList.iterator();
	}

}
