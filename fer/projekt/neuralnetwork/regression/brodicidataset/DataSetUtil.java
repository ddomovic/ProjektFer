/**
 *
 */
package fer.projekt.neuralnetwork.regression.brodicidataset;

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
		try (BufferedReader br = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
			String line = null;
			while (br.ready() && (line = br.readLine()) != null) {
				line = line.trim();
				String[] args = line.split(" ");
				double in1 = Double.parseDouble(args[0]);
				double in2 = Double.parseDouble(args[1]);
				double in3 = Double.parseDouble(args[2]);
				double in4 = Double.parseDouble(args[3]);
				double in5 = Double.parseDouble(args[4]);
				double in6 = Double.parseDouble(args[5]);
				double out = Double.parseDouble(args[6]);
				Data data = new Data(in1,in2,in3,in4,in5,in6,out);
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
