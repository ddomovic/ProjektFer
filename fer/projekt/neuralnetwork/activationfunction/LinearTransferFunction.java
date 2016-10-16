package fer.projekt.neuralnetwork.activationfunction;

/**
 * Aktivacijska funkcija linear neurona.
 *
 * @version 1.0
 */
public class LinearTransferFunction implements ITransferFunction {

	@Override
	public double applyFunction(double z) {
		return z;
	}

}