package pack2;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {
	List<Layer> layerList;
	
	public NeuralNetwork(){
		this.layerList = new ArrayList<>();
	}
	
	public void addLayer(int numberOfNeurons, ITransferFunction tfunction) throws Exception{
		Layer layer = new Layer(numberOfNeurons,tfunction);
		layerList.add(layer);
	}
	
	public void connectLayers(){
		for(int i = 0; i< layerList.size()-1; i++){
			layerList.get(i).connect(layerList.get(i+1));
		}
	}
	
	public double[] run(double[] input){
		
		int i = 0;
		Layer InputLayer = layerList.get(0);
		for(Neuron neuron : InputLayer){
			neuron.setInput(input[i]);
			i++;
		}
		
		Layer lastLayer = layerList.get(layerList.size()-1);
		double[] output = new double[lastLayer.size()];
		i = 0;
		for(Neuron neuron : lastLayer){
			output[i] = neuron.calculateOutput();
			i++;
		}
		return output;
	}
	
	//The magic happens here
	public static void main(String[] args) throws Exception {
		NeuralNetwork network = new NeuralNetwork();
		double[] input = {7.0,16.0,3.0};
		network.addLayer(3 , new SigmoidTransferFunction());
		network.addLayer(4, new SigmoidTransferFunction());
		network.addLayer(2, new SigmoidTransferFunction());
		network.connectLayers();
		double[] output = network.run(input);
		for(double out : output){
			System.out.println(out);
		}
	}

}
