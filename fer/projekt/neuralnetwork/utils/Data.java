/**
 *
 */
package fer.projekt.neuralnetwork.utils;

/**
 * @author David
 *
 */
public class Data {
	/**
	 * podaci dataseta koji se koriste kao input
	 */
	double[] input;
	/**
	 * podaci dataseta koji se koriste kao output
	 */
	double[] output;
	/**
	 * generira objekt tipa data koji predstavlja jedinični skup podataka iz dataseta
	 * @param input
	 * @param output
	 */
	public Data(double[] input, double[] output){
		this.input = input;
		this.output = output;
	}
	/**
	 * @return the input
	 */
	public double[] getInput() {
		return input;
	}
	/**
	 * @param input the input to set
	 */
	public void setInput(double[] input) {
		this.input = input;
	}
	/**
	 * @return the output
	 */
	public double[] getOutput() {
		return output;
	}
	/**
	 * @param output the output to set
	 */
	public void setOutput(double[] output) {
		this.output = output;
	}


}
