package fer.projekt.neuralnetwork.alcoholconsumption;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Utility razred koji se koristi za loadanje dataseta. Dodatno, nudi metodu za splitanje dataseta u odredenom postotku za
 * podjelu na dio za učenje i posebno na dio za testiranje.
 */
public class DatasetUtil {

	/**
	 * Učitava dataset iz danje putanje pomoću danog data convertera.
	 *
	 * @param p putanja do datoteke(dataseta)
	 * @param converter objekt razreda {@link DataConverter} koji sluzi za pretvorbu jednog reda zapisa u datoteci
	 * @return listu {@link Data} objekata
	 */
	public static List<Data> loadDataset(Path p, DataConverter converter) {
		ArrayList<Data> dataset = new ArrayList<>();

		try (BufferedReader br = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
			String line = null;
			while (br.ready() && (line = br.readLine()) != null) {
				String[] args = line.split("\t");
				double[] dataIn = converter.getInput(args);
				double[] dataOut = converter.getOutput(args);
				Data data = new Data(dataIn, dataOut);
				dataset.add(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dataset;
	}

	/**
	 * Splita listu Data zapisa u odredenom postotku i vrati te dvije liste.
	 * <br> <code>learnNum/learnDenum</code> daju postotak u kojem ce se lista dijeliti
	 *
	 * @param dataList lista svih {@link Data} objekata
	 * @param learnNum brojnik podjele
	 * @param learnDeNum nazivnik podjele
	 * @return listu listi, odnosno listu koja sadrzi listu za učenje i listu za testiranje
	 */
	public static List<List<Data>> splitDataset(List<Data> dataList, int learnNum, int learnDeNum) {
		List<Data> learningDataset = new ArrayList<>();
		List<Data> testingDataset = new ArrayList<>();

		dataList.forEach(new Consumer<Data>() {
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
