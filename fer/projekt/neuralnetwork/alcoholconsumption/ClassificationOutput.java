/**
 *
 */
package fer.projekt.neuralnetwork.alcoholconsumption;

/**
 * Predstavlja specifikacije jednog output neruona kod {@code ClassificationNetwork} neuronske mreže.
 * <br> Specifikacija sluzi za laksi pronalazak thresholda i binarnu klasifikaciju.
 * <br> Vrijedi da je clas2 > clas1
 *
 * <p> Svaka specifikacija sadrzi gornju i donju granicu binarne klasifikacije i threshold parametar koji je inijcijalno
 * postavljen na clas1.
 */
public class ClassificationOutput {

	/**
	 * Vrijednost prve klasifikacije.
	 */
	private final double clas1;
	/**
	 * Vrijednost druge klasifikacije.
	 */
	private final double clas2;
	/**
	 * Vrijednost thresholda za dani neuron.
	 */
	private double bestThreshold;
	/**
	 * Označava minimalnu vrijednost od kuda se trazi bestThreshold.
	 */
	private final double threshStart;
	/**
	 * Označava maksimalnu vrijednost do kuda se trazi bestThreshold.
	 */
	private final double threshEnd;

	/**
	 * Kreira novu instancu ovog objekta s danim parametrima.
	 *
	 * @param clas1 prva klasifikacija
	 * @param clas2 druga klasifikacija
	 * @param threshStart minimalna vrijednost od kuda se trazi threshold
	 * @param threshEnd maksimalna vrijednost do kuda se trazi threshold
	 */
	public ClassificationOutput(double clas1, double clas2, double threshStart, double threshEnd) {
		super();
		this.clas1 = clas1;
		this.clas2 = clas2;
		this.threshStart = threshStart;
		this.threshEnd = threshEnd;
		this.bestThreshold = threshStart;
	}

	/**
	 * @return the clas1
	 */
	public double getClas1() {
		return clas1;
	}

	/**
	 * @return the clas2
	 */
	public double getClas2() {
		return clas2;
	}

	/**
	 * @return the bestThreshold
	 */
	public double getBestThreshold() {
		return bestThreshold;
	}

	/**
	 * @param bestThreshold the bestThreshold to set
	 */
	public void setBestThreshold(double bestThreshold) {
		this.bestThreshold = bestThreshold;
	}

	/**
	 * @return the threshStart
	 */
	public double getThreshStart() {
		return threshStart;
	}

	/**
	 * @return the threshEnd
	 */
	public double getThreshEnd() {
		return threshEnd;
	}

}