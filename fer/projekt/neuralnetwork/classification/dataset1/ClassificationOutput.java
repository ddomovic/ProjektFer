/**
 *
 */
package fer.projekt.neuralnetwork.classification.dataset1;

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
	private double clas1;
	/**
	 * Vrijednost druge klasifikacije.
	 */
	private double clas2;
	/**
	 * Vrijednost thresholda za dani neuron.
	 */
	private double threshold;

	/**
	 * Kreira novu instancu ovog objekta s danim parametrima.
	 *
	 * @param clas1 prva klasifikacija
	 * @param clas2 druga klasifikacija
	 */
	public ClassificationOutput(double clas1, double clas2) {
		super();
		this.clas1 = clas1;
		this.clas2 = clas2;
		this.threshold = clas1;
	}

	/**
	 * @return the clas1
	 */
	public double getClas1() {
		return clas1;
	}

	/**
	 * @param clas1 the clas1 to set
	 */
	public void setClas1(double clas1) {
		this.clas1 = clas1;
	}

	/**
	 * @return the clas2
	 */
	public double getClas2() {
		return clas2;
	}

	/**
	 * @param clas2 the clas2 to set
	 */
	public void setClas2(double clas2) {
		this.clas2 = clas2;
	}

	/**
	 * @return the threshold
	 */
	public double getThreshold() {
		return threshold;
	}

	/**
	 * @param threshold the threshold to set
	 */
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}



}