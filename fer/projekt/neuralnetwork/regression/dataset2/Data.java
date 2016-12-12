/**
 *
 */
package fer.projekt.neuralnetwork.regression.dataset2;

/**
 * @author Nikola
 *
 */
public class Data {
	private double motor;
	private double screw;
	private double pgain;
	private double vgain;
	private double out;

	/**
	 * @param motor
	 * @param screw
	 * @param pgain
	 * @param vgain
	 * @param out
	 */
	public Data(double motor, double screw, double pgain, double vgain, double out) {
		super();
		this.motor = motor;
		this.screw = screw;
		this.pgain = pgain;
		this.vgain = vgain;
		this.out = out;
	}


	/**
	 * @return the motor
	 */
	public double getMotor() {
		return motor;
	}

	/**
	 * @param motor the motor to set
	 */
	public void setMotor(double motor) {
		this.motor = motor;
	}
	/**
	 * @return the screw
	 */
	public double getScrew() {
		return screw;
	}
	/**
	 * @param screw the screw to set
	 */
	public void setScrew(double screw) {
		this.screw = screw;
	}
	/**
	 * @return the pgain
	 */
	public double getPgain() {
		return pgain;
	}
	/**
	 * @param pgain the pgain to set
	 */
	public void setPgain(double pgain) {
		this.pgain = pgain;
	}
	/**
	 * @return the vgain
	 */
	public double getVgain() {
		return vgain;
	}
	/**
	 * @param vgain the vgain to set
	 */
	public void setVgain(double vgain) {
		this.vgain = vgain;
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
