package fer.projekt.neuralnetwork.activationfunction;

/**
 * Aktivacijska funkcija sigmoidalnih neurona.
 *
 * @version 1.0
 */
public class SigmoidTransferFunction implements ITransferFunction {

	@Override
	public double applyFunction(double z) {
		return 1.0 / (1.0 + (Math.exp(-z)));
	}

}
