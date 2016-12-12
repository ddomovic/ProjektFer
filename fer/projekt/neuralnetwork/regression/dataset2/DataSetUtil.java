/**
 *
 */
package fer.projekt.neuralnetwork.regression.dataset2;

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
				String[] args = line.split(",");
				double motor = LetterToNumb(args[0]);
				double screw = LetterToNumb(args[1]);
				double pgain = Double.parseDouble(args[2]);
				double vgain = Double.parseDouble(args[3]);
				double out = Double.parseDouble(args[4]);
				Data data = new Data(motor,screw,pgain,vgain,out);
				dataset.add(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dataset;
	}

	/**
	 * @param string
	 * @return
	 */
	private static double LetterToNumb(String letter) {
		switch (letter) {
		case "A":
			return -2;
		case "B":
			return -1;
		case "C":
			return 0;
		case "D":
			return 1;
		case "E":
			return 2;
		default:
			return 999999999;
		}
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
