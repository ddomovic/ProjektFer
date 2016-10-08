package pack2;
/**
 * klasa koja predstavlja jednu konekciju izmedu dva neurona
 * @author Nikola,
 *
 */
//koristimo klasu, a ne polja jer cemo poslije trebati jos informacija prenositi + ovo je najpoopcenitijiji
//nacin na koji to mozemo izvesti -Nikola
public class Connection {
	private final Neuron input;
	private final Neuron output;
	private double weight;
	private static int COUNTER = 0;
	private int id;

	//originalno sam mislio i weight odmah u konstruktoru setat, ali ispada da nam to komplicira
	//zivot pri inicijalizaciji na random vrijednosti -Nikola
	public Connection(Neuron input, Neuron output){
		this.input = input;
		this.output = output;
		this.weight = Math.random();
		this.id = COUNTER++;

		output.addInConnection(this);
	}

	/**
	 * Metoda za dobivanje ID konekcije nad kojom se zove
	 * @return ID
	 * @author Nikola
	 */
	public int getId(){
		return id;
	}

	/**
	 * Metoda koja vraca tezinu konekcije nad kojom se zove
	 * @return weight
	 * @author Nikola
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Metoda za postavljanje tezine konekcije nad kojom se zove
	 * @param weight
	 * @author Nikola
	 */
	public void setWeight(double w) {
		weight = w;
	}

	public Neuron getInput() {
		return input;
	}

	public Neuron getOutput() {
		return output;
	}

}
