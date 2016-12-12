/**
 *
 */
package fer.projekt.neuralnetwork.regression.brodicidataset;

/**
 * @author Nikola
 *
 */
public class Data {
	private double in1;
	private double in2;
	private double in3;
	private double in4;
	private double in5;
	private double in6;
	private double out;


	/**
	 * @param in1
	 * @param in2
	 * @param in3
	 * @param in4
	 * @param in5
	 * @param in6
	 * @param out
	 */
	public Data(double in1, double in2, double in3, double in4, double in5, double in6, double out) {
		super();
		this.in1 = in1;
		this.in2 = in2;
		this.in3 = in3;
		this.in4 = in4;
		this.in5 = in5;
		this.in6 = in6;
		this.out = out;
	}
	/**
	 * @return the in1
	 */
	public double getIn1() {
		return in1;
	}
	/**
	 * @param in1 the in1 to set
	 */
	public void setIn1(double in1) {
		this.in1 = in1;
	}
	/**
	 * @return the in2
	 */
	public double getIn2() {
		return in2;
	}
	/**
	 * @param in2 the in2 to set
	 */
	public void setIn2(double in2) {
		this.in2 = in2;
	}
	/**
	 * @return the in3
	 */
	public double getIn3() {
		return in3;
	}
	/**
	 * @param in3 the in3 to set
	 */
	public void setIn3(double in3) {
		this.in3 = in3;
	}
	/**
	 * @return the in4
	 */
	public double getIn4() {
		return in4;
	}
	/**
	 * @param in4 the in4 to set
	 */
	public void setIn4(double in4) {
		this.in4 = in4;
	}
	/**
	 * @return the in5
	 */
	public double getIn5() {
		return in5;
	}
	/**
	 * @param in5 the in5 to set
	 */
	public void setIn5(double in5) {
		this.in5 = in5;
	}
	/**
	 * @return the in6
	 */
	public double getIn6() {
		return in6;
	}
	/**
	 * @param in6 the in6 to set
	 */
	public void setIn6(double in6) {
		this.in6 = in6;
	}
	/**
	 * @return the out
	 */
	public double getOut() {
		return out;
	}
	/**
	 * @param out the out to set
	 */
	public void setOut(double out) {
		this.out = out;
	}

}
