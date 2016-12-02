package fer.projekt.neuralnetwork.classification.dataset1;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class DatasetUtil {

	private static List<Data> loadDataset(Path p) {
		ArrayList<Data> dataset = new ArrayList<>();

		NumberFormat nf = NumberFormat.getInstance(Locale.FRANCE);

		try (BufferedReader br = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
			String line = null;
			while (br.ready() && (line = br.readLine()) != null) {
				String[] args = line.split("\t");
				Number number = nf.parse(args[0]);
				double temperature = number.doubleValue();
				int nausea = args[1].equals("no") ? 0 : 1;
				int lumbPain = args[2].equals("no") ? 0 : 1;
				int urinePush = args[3].equals("no") ? 0 : 1;
				int mictPain = args[4].equals("no") ? 0 : 1;
				int burn = args[5].equals("no") ? 0 : 1;
				int inflammation = args[6].equals("no") ? 0 : 1;
				int nephrihis = args[7].equals("no") ? 0 : 1;
				Data data = new Data(temperature, nausea, lumbPain, urinePush, mictPain, burn, inflammation, nephrihis);
				dataset.add(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return dataset;
	}

	public static List<List<Data>> splitDataset(Path p, int learnNum, int learnDeNum) {
		List<Data> dataset = DatasetUtil.loadDataset(p);

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
