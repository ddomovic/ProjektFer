package fer.projekt.neuralnetwork.elements;

import java.util.Random;

/**
 * Razred koji predstavlja jednu konekciju izmedu dva neurona
 *
 * @version 1.0
 */
public class Connection {
	private static Random random = new Random();
	/**
	 * Counter konekcija
	 */
	private static int COUNTER = 0;

	/**
	 * Lijevi neuron konekcije
	 */
	private final Neuron left;
	/**
	 * Desni neuron konekcije
	 */
	private final Neuron right;
	/**
	 * Tezina veze izmedu dva neurona
	 */
	private double weight;
	/**
	 * ID konekcije
	 */
	private int id;

	/**
	 * Stvori i inicijalizira novu konekciju izmedu dva neurona. Tezine neurona se postavlja na
	 * random vrijednost izmedu dviju predanih u argumentu.
	 *
	 * @param left lijevi neuron
	 * @param right desni neuron
	 * @param minWeight maximalna tezina konekcije
	 * @param maxWeight minimalna tezina konekcije
	 */
	public Connection(Neuron left, Neuron right, double minWeight, double maxWeight){
		this(left, right, random.nextGaussian()*0.01* (maxWeight - minWeight) + (maxWeight -minWeight));
		//this(left, right, Math.random() * (maxWeight - minWeight) + minWeight);
	}

	/**
	 * Stvori i inicijalizira konekciju izmedu dva neurona.
	 *
	 * @param left lijevi neuron
	 * @param right desni neuron
	 * @param weight tezina neurona
	 */
	public Connection(Neuron left, Neuron right, double weight){
		this.left = left;
		this.right = right;
		this.weight = weight;
		this.id = COUNTER++;
	}

	/**
	 * Metoda za dobivanje ID konekcije nad kojom se zove
	 *
	 * @return ID
	 */
	public int getId(){
		return id;
	}

	/**
	 * Metoda koja vraca tezinu konekcije nad kojom se zove
	 *
	 * @return weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Metoda za postavljanje tezine konekcije nad kojom se zove
	 *
	 * @param weight nova tezina konekcije
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * Vraća lijevi neuron veze
	 *
	 * @return {@link Neuron}
	 */
	public Neuron getLeft() {
		return left;
	}

	/**
	 * Vraća desni neuron veze
	 *
	 * @return {@link Neuron}
	 */
	public Neuron getRight() {
		return right;
	}

}
