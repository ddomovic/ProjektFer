package pack2;

public class SigmoidTransferFunction implements ITransferFunction {

	@Override
	public double applyFunction(double z) {
		return 1.0 / (1.0 + (Math.exp(-z)));
	}

}
