package pack2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Layer implements Iterable<Neuron> {

	private List<Neuron> neuronList;

	public Layer(boolean isFirstLayer, int numberOfNeurons, ITransferFunction tfunction) {
		super();
		this.neuronList = new ArrayList<>();
		for (int i= 0; i < numberOfNeurons; i++) {
			Neuron neuron = new Neuron(
					isFirstLayer ? 0 : Math.random(),
							Math.random(),
							tfunction
					);
			neuronList.add(neuron);
		}
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
