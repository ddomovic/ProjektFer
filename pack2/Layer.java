package pack2;

import java.util.List;

public class Layer {

	private List<Neuron> neuronList;

	public Layer(List<Neuron> neurons) {
		super();
		this.neuronList = neurons;
	}

	public Layer(Neuron ... neurons) {
		for (Neuron n : neurons) {
			neuronList.add(n);
		}
	}

}
