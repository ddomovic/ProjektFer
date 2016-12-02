/**
 *
 */
package fer.projekt.neuralnetwork.regression.dataset1;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;



/**
 * @author Nikola
 *
 */
public class DataSetUtil {
	private static List<Data> loadDataset(Path p) {
		ArrayList<Data> dataset = new ArrayList<>();
		int counter = 0;
		try (BufferedReader br = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
			String line = null;
			while (counter < 147 && br.ready() && (line = br.readLine()) != null) {
				counter++;
				String[] args = line.split(",");
				int OSM_ID = Integer.parseInt(args[0]);
				double longitude = Double.parseDouble(args[1]);
				double latitude = Double.parseDouble(args[2]);
				double altitude = Double.parseDouble(args[3]);
				Data data = new Data(OSM_ID,longitude,latitude,altitude);
				dataset.add(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dataset;
	}

	public static List<List<Data>> splitDataset(Path p, int learnNum, int learnDeNum) {
		List<Data> dataset = DataSetUtil.loadDataset(p);

		List<Data> learningDataset = new ArrayList<>();
		List<Data> testingDataset = new ArrayList<>();

		dataset.forEach(new Consumer<Data>() {
			double counter = 1;

			@Override
			public void accept(Data d) {
				if (counter % learnDeNum >= learnNum) {
					testingDataset.add(d);
				} else {
					learningDataset.add(d);
				}
				counter++;
			}
		});

		List<List<Data>> returnList = new ArrayList<>();
		returnList.add(learningDataset);
		returnList.add(testingDataset);
		return returnList;
	}
}
