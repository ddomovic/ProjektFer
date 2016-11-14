package fer.projekt.neuralnetwork.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import fer.projekt.neuralnetwork.NeuralNetwork;
import fer.projekt.neuralnetwork.elements.Connection;
import fer.projekt.neuralnetwork.elements.Layer;
import fer.projekt.neuralnetwork.elements.Neuron;
import fer.projekt.neuralnetwork.regression.LinearRegressionNetwork;

public class FileUtils {

	private static String getLayerDescription(Layer layer) {
		StringBuilder sb = new StringBuilder();
		sb.append("layerIndex=" + layer.getIndex() + System.lineSeparator());
		layer.forEach(n -> {
				sb.append("nid=" + n.getId() + " bias=" + n.getBias() + System.lineSeparator());
				List<Connection> connections = n.getAllInConnections();
				connections.forEach(c -> {
					sb.append("cid=" + c.getId() + " weight=" + c.getWeight() + System.lineSeparator());
				});
			});
		return sb.toString();
	}
	
	public static void saveNetwork(NeuralNetwork network, String fileName) {
		Path p = Paths.get(fileName);
		try (BufferedWriter bw = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
			ArrayList<Layer> layerList = network.getLayerList();
			layerList.forEach(l -> {
				String layerDescription = FileUtils.getLayerDescription(l);
				try {
					bw.write(layerDescription);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			System.out.println("Error while saving neural network to file!");
			e.printStackTrace();
		}
	}
	
	public static void loadNeuralNetwork(LinearRegressionNetwork network, String fileName) {
		Path p = Paths.get(fileName);
		Layer l = null;
		Neuron n = null;
		try (BufferedReader br = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
			String line = null;
			while (br.ready() && (line = br.readLine()) != null) {
				if (line.startsWith("layerIndex")) {
					String index = line.substring(line.indexOf("=") + 1, line.indexOf(" "));
					l = network.getLayerList().get(Integer.valueOf(index));
				} else if (line.startsWith("nid")) {
					String nid = line.substring(line.indexOf("=") + 1, line.indexOf(" "));
					n = l.getNeuron(Integer.valueOf(nid));
					
					String bias = line.substring(line.indexOf("bias=") + 5);
					n.setBias(Integer.valueOf(bias));
				} else if (line.startsWith("cid")) {
					ArrayList<Connection> inConnections = n.getAllInConnections();
					
					int cid = Integer.valueOf(line.substring(line.indexOf("=") + 1, line.indexOf(" ")));
					String weight = line.substring(line.indexOf("weight=" + 7));
					inConnections.forEach(c -> {
						if (c.getId() == cid) {
							c.setWeight(Integer.valueOf(weight));
						}
					});
				}
			}
		} catch (IOException e) {
			System.out.println("Error while loading neural network from file!");
			e.printStackTrace();
		}
		
	}
	
}
