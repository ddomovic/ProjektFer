package fer.projekt.neuralnetwork.activationfunction;

/**
 * Predstavlja aktvacijsku funkciju
 *
 * @version 1.0
 */
public interface ITransferFunction {

	/**
	 * Aktivira neuron, izračunava izlaz za dani z.
	 *
	 * @param z sum(wi*xi)+ b
	 * @return izlaz neurona
	 */
	double applyFunction(double z);

}
