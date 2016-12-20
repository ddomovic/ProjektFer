/**
 *
 */
package fer.projekt.neuralnetwork.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.Consumer;

import fer.projekt.neuralnetwork.ClassificationRegressionNetwork;
import fer.projekt.neuralnetwork.elements.Connection;
import fer.projekt.neuralnetwork.elements.Layer;
import fer.projekt.neuralnetwork.elements.Neuron;

/**
 * Util razred za spremanje {@link ClassificationRegressionNetwork} neuronske mreže.
 * <br> Note: razlika od običnog spremanja neuronske mreže je da se ovdje jos dodatno spremaju i najbolji thresholdi mreže.
 *
 * @author David
 *
 */
public class ClassificationFileUtils {

	/**
	 * Sprema danu mrezu za klasifikaciju u datoteku s danim imenom <code>fileName</code>.
	 *
	 * @param network {@link ClassificationRegressionNetwork} neuronska mreža
	 * @param p path do datoteke u kojoj ce se mreza spremiti
	 */
	public static void saveClassificationNetwork(ClassificationRegressionNetwork network, Path p) {
		try (BufferedWriter bw = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
			//zapisivanje broja neurona po layerima
			ArrayList<Layer> layerList = network.getLayerList();
			layerList.forEach(l -> {
				try {
					bw.write(l.size() + ",");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			bw.newLine();
			// zapisivanje thresholdova
			double[] bestThresholds = network.getBestThresholds();
			bw.write("thresholds:");
			for (double b : bestThresholds) {
				bw.write(b + " ");
			}
			bw.newLine();

			layerList.forEach(new Consumer<Layer>() {
				int layerIndex = 0;

				@Override
				public void accept(Layer l) {
					String layerDescription = RegressionFileUtils.getLayerDescription(layerIndex, l);
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

	/**
	 * Metoda koja ucitava neuronsku mrežu s dane putanje u dani objekt {@link ClassificationRegressionNetwork} neuronske mreže.
	 *
	 * @param network {@link ClassificationRegressionNetwork} neuronska mreža
	 * @param p putanja do datoteke u kojoj je mreža spremljena
	 */
	public static void loadClassificationNetwork(ClassificationRegressionNetwork network, Path p) {
		Layer l = null;
		Neuron n = null;
		try (BufferedReader br = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
			String line = null;
			if (br.ready()) {
				line = br.readLine();
			}
			// provjera je su li layeri iste velicine
			StringBuffer sb = new StringBuffer();
			ArrayList<Layer> layerList = network.getLayerList();
			layerList.forEach(layer -> {
				sb.append(layer.size() + ",");
			});
			if (!line.equals(sb.toString()) && line != null) {
				throw new IllegalArgumentException(
						"Broj layera ili broj neurona u njima ne odgovara definiciji networka u file-u!"
								+ "\nTraženi broj layera je: " + line + " a dobiveni: " + sb.toString());
			}

			// ucitavanje najboljih thresholda za neuronsku mrežu
			String thresholds = null;
			if (br.ready()) {
				thresholds = br.readLine();
			}
			String[] thresholdArgs = thresholds.trim().substring(thresholds.indexOf(':') + 1).split(" ");
			double[] bestThresholds = new double[thresholdArgs.length];
			int i = 0;
			for (String t : thresholdArgs) {
				if (!t.isEmpty()) {
					bestThresholds[i++] = Double.valueOf(t);
				}
			}

			network.setBestThresholds(bestThresholds);

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
