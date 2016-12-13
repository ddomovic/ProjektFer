package fer.projekt.neuralnetwork.classification.dataset1;

/**
 * Predstavlja jedan test iz dataseta.
 */
public class Data {
	/**
	 * Temperatura pacijenta
	 */
	private double Temperature;
	/**
	 * Da li je pacijentu muka/ ima li mučninu? 1 == da, 0 == ne
	 */
	private int nausea;
	/**
	 * bol u donjem dijelu leđa? 1 == da, 0 == ne
	 */
	private int lumbPain;
	/**
	 * Stalna potreba za pišanjem? 1 == da, 0 == ne
	 */
	private int urinePush;
	/**
	 * bol pri mokrenju? 1 == da, 0 == ne
	 */
	private int mictPain;
	/**
	 * bol mokraćne cijevi
	 */
	private int burn;
	/**
	 * upala? 1 == da, 0 == ne
	 */
	private int inflammation;
	/**
	 * nefritis? 1 == da, 0 == ne
	 */
	private int nephritis;

	/**
	 * Inicijalizacija novog podatka
	 * @param temperature
	 * @param nausea
	 * @param lumbPain
	 * @param urinePush
	 * @param mictPain
	 * @param burn
	 * @param inflammation
	 * @param nephrihis
	 */
	public Data(double temperature, int nausea, int lumbPain, int urinePush, int mictPain, int burn, int inflammation,
			int nephrihis) {
		super();
		Temperature = temperature;
		this.nausea = nausea;
		this.lumbPain = lumbPain;
		this.urinePush = urinePush;
		this.mictPain = mictPain;
		this.burn = burn;
		this.inflammation = inflammation;
		this.nephritis = nephrihis;
	}

	/**
	 * @return the temperature
	 */
	public double getTemperature() {
		return Temperature;
	}

	/**
	 * @param temperature the temperature to set
	 */
	public void setTemperature(double temperature) {
		Temperature = temperature;
	}

	/**
	 * @return the nausea
	 */
	public int getNausea() {
		return nausea;
	}

	/**
	 * @param nausea the nausea to set
	 */
	public void setNausea(int nausea) {
		this.nausea = nausea;
	}

	/**
	 * @return the lumbPain
	 */
	public int getLumbPain() {
		return lumbPain;
	}

	/**
	 * @param lumbPain the lumbPain to set
	 */
	public void setLumbPain(int lumbPain) {
		this.lumbPain = lumbPain;
	}

	/**
	 * @return the urinePush
	 */
	public int getUrinePush() {
		return urinePush;
	}

	/**
	 * @param urinePush the urinePush to set
	 */
	public void setUrinePush(int urinePush) {
		this.urinePush = urinePush;
	}

	/**
	 * @return the mictPain
	 */
	public int getMictPain() {
		return mictPain;
	}

	/**
	 * @param mictPain the mictPain to set
	 */
	public void setMictPain(int mictPain) {
		this.mictPain = mictPain;
	}

	/**
	 * @return the burn
	 */
	public int getBurn() {
		return burn;
	}

	/**
	 * @param burn the burn to set
	 */
	public void setBurn(int burn) {
		this.burn = burn;
	}

	/**
	 * @return the inflammation
	 */
	public int getInflammation() {
		return inflammation;
	}

	/**
	 * @param inflammation the inflammation to set
	 */
	public void setInflammation(int inflammation) {
		this.inflammation = inflammation;
	}

	/**
	 * @return the nephritis
	 */
	public int getNephritis() {
		return nephritis;
	}

	/**
	 * @param nephritis the nephritis to set
	 */
	public void setNephritis(int nephritis) {
		this.nephritis = nephritis;
	}

}
