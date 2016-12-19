/**
 *
 */
package fer.projekt.neuralnetwork.alcoholconsumption;

/**
 * @author David
 *
 */
public interface DataConverter {
	public Double[] getMaxInputValues();

	public Double[] getMinInputValues();

	public double[] getInput(String[] parameters);

	public double[] getOutput(String[] parameters);

}
