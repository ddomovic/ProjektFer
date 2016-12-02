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
import java.util.function.Consumer;

import fer.projekt.neuralnetwork.NeuralNetwork;
import fer.projekt.neuralnetwork.elements.Connection;
import fer.projekt.neuralnetwork.elements.Layer;
import fer.projekt.neuralnetwork.elements.Neuron;
import fer.projekt.neuralnetwork.regression.LinearRegressionNetwork;

public class FileUtils {

	private static String getLayerDescription(int layerIndex, Layer layer) {
		StringBuilder sb = new StringBuilder();
		sb.append("layerIndex=" + layerIndex + System.lineSeparator());
		
		layer.forEach(new Consumer<Neuron>() {
			int nIndex = 0;
			@Override
			public void accept(Neuron n) {
				sb.append("nIndex=" + nIndex++ + " bias=" + n.getBias() + System.lineSeparator());
				
				List<Connection> connections = n.getAllInConnections();
				connections.forEach(new Consumer<Connection>() {
					int cIndex = 0;
					@Override
					public void accept(Connection c) {
						sb.append("cIndex=" + cIndex++ + " weight=" + c.getWeight() + System.lineSeparator());
					}
				});
			}
		});
		return sb.toString();
	}
	
	public static void saveNetwork(NeuralNetwork network, String fileName) {
		Path p = Paths.get(fileName);
		try (BufferedWriter bw = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
			ArrayList<Layer> layerList = network.getLayerList();
			layerList.forEach(l -> {
				try {
					bw.write(l.size() + ",");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			bw.newLine();
			
			layerList.forEach(new Consumer<Layer>() {
				int layerIndex = 0;
				@Override
				public void accept(Layer l) {
					String layerDescription = FileUtils.getLayerDescription(layerIndex, l);
					layerIndex++;
					try {
						bw.write(layerDescription);
					} catch (IOException e) {
						e.printStackTrace();
					}					
				}
			});
		} catch (IOException e) {
			System.out.println("Error while saving neural network to file!");
			e.printStackTrace();
		}
	}
	
	public static void loadNeuralNetwork(NeuralNetwork network, String fileName) {
		Path p = Paths.get(fileName);
		Layer l = null;
		Neuron n = null;
		try (BufferedReader br = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
			String line = null;
			if (br.ready()) {
				line = br.readLine();
			}
			//provjera je su li layeri iste velicine
			StringBuffer sb = new StringBuffer();
			ArrayList<Layer> layerList = network.getLayerList();
			layerList.forEach(layer -> {
				sb.append(layer.size() + ",");
			});
			if (!line.equals(sb.toString()) && line != null) {
				throw new IllegalArgumentException("Broj layera ili broj neurona u njima ne odgovara definiciji networka u file-u!" +
						"\nTraženi broj layera je: " + line + " a dobiveni: " + sb.toString());						
			}
			
			while (br.ready() && (line = br.readLine()) != null) {
				if (line.startsWith("layerIndex")) {
					String layerIndex = line.substring(line.indexOf("=") + 1);
					l = network.getLayerList().get(Integer.valueOf(layerIndex));
				} else if (line.startsWith("nIndex")) {
					String nIndex = line.substring(line.indexOf("=") + 1, line.indexOf(" "));
					n = l.getNeuron(Integer.valueOf(nIndex));
					
					String bias = line.substring(line.indexOf("bias=") + 5);
					n.setBias(Double.valueOf(bias));
				} else if (line.startsWith("cIndex")) {
					ArrayList<Connection> inConnections = n.getAllInConnections();
					
					int cIndex = Integer.valueOf(line.substring(line.indexOf("=") + 1, line.indexOf(" ")));
					String weight = line.substring(line.indexOf("weight=") + 7);
					Connection c = inConnections.get(cIndex);
					c.setWeight(Double.valueOf(weight));
				}
			}
		} catch (IOException e) {
			System.out.println("Error while loading neural network from file!");
			e.printStackTrace();
		}
		
	}
	
}
