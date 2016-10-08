package pack2;
/**
 * klasa koja predstavlja jednu konekciju izmedu dva neurona
 * @author Nikola,
 *
 */
//koristimo klasu, a ne polja jer cemo poslije trebati jos informacija prenositi + ovo je najpoopcenitijiji
//nacin na koji to mozemo izvesti -Nikola
public class Connection {
	private final Neuron left;
	private final Neuron right;
	private double weight;
	private static int COUNTER = 0;
	private int id;


	public Connection(Neuron left, Neuron right){
		this.left = left;
		this.right = right;
		this.weight = Math.random();
		this.id = COUNTER++;
		right.addInConnection(this);
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

	public Neuron getLeft() {
		return left;
	}

	public Neuron getRight() {
		return right;
	}

}
