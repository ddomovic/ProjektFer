package pack2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Layer implements Iterable<Neuron> {

	private List<Neuron> neuronList;

	public Layer(int numberOfNeurons, ITransferFunction tfunction){
		this.neuronList = new ArrayList<>();
		for (int i= 0; i < numberOfNeurons; i++) {
			Neuron neuron = new Neuron(
							Math.random(),
							tfunction
					);
			neuronList.add(neuron);
		}
	}

	public int size(){
		return neuronList.size();
	}
	
	public Layer(Neuron ... neurons) {
		for (Neuron n : neurons) {
			neuronList.add(n);
		}
	}

	public void addNeuron(Neuron n) {
		neuronList.add(n);
	}

	public void connect(Layer rightLayer) {
		neuronList.forEach(left -> {
			rightLayer.forEach(right -> {
				Connection con = new Connection(left, right);
			});
		});
	}

	@Override
	public Iterator<Neuron> iterator() {
		return neuronList.iterator();
	}

}
