package fer.projekt.neuralnetwork.regression;

/**
 * Interface koji sluzi za generaliziranje funkcije koju {@link LinearRegressionNetwork} uci.
 */
public interface IMathFunction {

	/**
	 * Izračunava izlaz za zadani input.
	 * 
	 * @param input input funkcije
	 * @return izlaz funkcije
	 */
	public double calculate(double input);
	
	/**
	 * Vraća ime funkcije koju mreža opisuje.
	 * 
	 * @return ime fje
	 */
	public String getFuncName();

	/**
	 * Vraća minimalnu mogucu vrijednost domene.
	 * 
	 * @return min
	 */
	public double getDomainMin();
	
	/**
	 * Vraća maximalnu mogucu vrijednost domene.
	 * 
	 * @return max
	 */
	public double getDomainMax();
	
}
