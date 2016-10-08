package pack2;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {
	List<Layer> layerList = new ArrayList<>();
	
	public NeuralNetwork(Layer ...layers){
		
		for(Layer layer : layers){
			layerList.add(layer);
		}
		connectLayers();		
	}
	
	public NeuralNetwork(List<Layer> layers){
		layerList.addAll(layers);
		connectLayers();
	}
	
	private void connectLayers(){
		for(int i = 0; i< layerList.size()-1; i++){
			layerList.get(i).connect(layerList.get(i+1));
		}
	}
	
	public double[] run(double[] inputs){
		return inputs;
		
	}
	//The magic happens here
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
