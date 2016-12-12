/**
 *
 */
package fer.projekt.neuralnetwork.classification.dataset1;

public class ClassificationOutput {

	private int clas1;
	private int clas2;
	private double threshold;

	/**
	 * @param clas1
	 * @param clas2
	 * @param bestThreshold
	 */
	public ClassificationOutput(int clas1, int clas2) {
		super();
		this.clas1 = clas1;
		this.clas2 = clas2;
		this.threshold = clas1;
	}

	public int getClas1() {
		return clas1;
	}

	public void setClas1(int clas1) {
		this.clas1 = clas1;
	}

	public int getClas2() {
		return clas2;
	}

	public void setClas2(int clas2) {
		this.clas2 = clas2;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

}
