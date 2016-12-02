package fer.projekt.neuralnetwork.classification.dataset1;

public class Data {
	/**
	 *Temperatura pacijenta
	 */
	private double Temperature;
	/**
	 * Da li je pacijentu muka/ ima li mučninu?
	 * 1 == da, 0 == ne
	 */
	private int nausea;
	/**
	 * bol u donjem dijelu leđa?
	 * 1 == da, 0 == ne
	 */
	private int lumbPain;
	/**
	 * Stalna potreba za pišanjem?
	 * 1 == da, 0 == ne
	 */
	private int urinePush;

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
		this.nephrihis = nephrihis;
	}
	public double getTemperature() {
		return Temperature;
	}
	public void setTemperature(double temperature) {
		Temperature = temperature;
	}
	public int getNausea() {
		return nausea;
	}
	public void setNausea(int nausea) {
		this.nausea = nausea;
	}
	public int getLumbPain() {
		return lumbPain;
	}
	public void setLumbPain(int lumbPain) {
		this.lumbPain = lumbPain;
	}
	public int getUrinePush() {
		return urinePush;
	}
	public void setUrinePush(int urinePush) {
		this.urinePush = urinePush;
	}
	public int getMictPain() {
		return mictPain;
	}
	public void setMictPain(int mictPain) {
		this.mictPain = mictPain;
	}
	public int getBurn() {
		return burn;
	}
	public void setBurn(int burn) {
		this.burn = burn;
	}
	public int getInflammation() {
		return inflammation;
	}
	public void setInflammation(int inflammation) {
		this.inflammation = inflammation;
	}
	public int getNephrihis() {
		return nephrihis;
	}
	public void setNephrihis(int nephrihis) {
		this.nephrihis = nephrihis;
	}
	private int mictPain;
	private int burn;
	private int inflammation;
	private int nephrihis;
}
