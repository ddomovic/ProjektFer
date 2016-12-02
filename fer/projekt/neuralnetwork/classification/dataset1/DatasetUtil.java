package fer.projekt.neuralnetwork.classification.dataset1;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DatasetUtil {

	public List<Data> loadDataset(Path p) {
		ArrayList<Data> dataset = new ArrayList<>();

		try (BufferedReader br = Files.newBufferedReader(p)) {
			String line = null;
			while (br.ready() && (line = br.readLine()) != null) {
				String[] args = line.split("\t");
				double temperature = Double.parseDouble(args[0]);
				int nausea = Integer.parseInt(args[1]);
				int lumbPain = Integer.parseInt(args[2]);
				int urinePush = Integer.parseInt(args[3]);
				int mictPain = Integer.parseInt(args[4]);
				int burn = Integer.parseInt(args[5]);
				int inflammation = Integer.parseInt(args[6]);
				int nephrihis = Integer.parseInt(args[7]);
				Data data = new Data(temperature, nausea, lumbPain, urinePush, mictPain, burn, inflammation, nephrihis);
				dataset.add(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dataset;
	}

}
