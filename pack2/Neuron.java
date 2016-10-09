package pack2;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Predstavlja neuron.
 *
 * @version 1.0
 */
public class Neuron {
	/**
	 * Brojac neurona.
	 */
	private static int COUNTER = 0;

	/**
	 * ID neurona.
	 */
	private int id;
	/**
	 * Izlaz neurona
	 */
	private double output;
	/**
	 * Ulaz neurona
	 */
	private double input = 0d;
	/**
	 * Konekcije koje ulaze u neuron
	 */
	private ArrayList<Connection> Inconnections = new ArrayList<Connection>();
	/**
	 * Mapira id neurona na konekciju kojom je spojen na ovaj neuron
	 */
	private HashMap<Integer, Connection> connectionLookup = new HashMap<Integer, Connection>();
	/**
	 * Transferna(aktivacijska) funkcija
	 */
	private ITransferFunction tfunction;
	/**
	 * Bias
	 */
	private double bias;

	/**
	 * Stvori i inicijalizira novi neuron.
	 *
	 * @param tfunction transferna funkcija
	 */
	public Neuron(ITransferFunction tfunction){
		this.bias = Math.random() * 2 - 1;
		this.id = COUNTER++;
		this.tfunction = tfunction;
	}

	/**
	 * Metoda koja izračunava izlaz neurona
	 *
	 * @return output double vrijednost izlaza
	 *
	 */
	public double calculateOutput(){
		if (output != 0) {
			return output;
		}

		double sum = input;
		for(Connection connection : Inconnections){
			Neuron inputNeuron = connection.getLeft();
			sum += inputNeuron.calculateOutput() * connection.getWeight();
		}
		output = tfunction.applyFunction(sum + bias);
		return output;
	}

	/**
	 * Sluzi za resetiranje vrijednosti izlaza neurona
	 */
	public void reset(){
		output = 0;
	}

	/**
	 * Metoda za istovremeno dodavanje vise konekcija trenutnom neuronu
	 *
	 * @param inNeurons lista ulaznih neurona
	 */
	public void addInConnectionsS(ArrayList<Neuron> inNeurons) {
		for (Neuron n : inNeurons) {
			Connection con = new Connection(n, this);
			Inconnections.add(con);
			connectionLookup.put(n.id, con);
		}
	}

	/**
	 * Metoda koja vraca konekciju neurona za dani index neurona
	 *
	 * @param neuronIndex index neurona za kojeg trazimo konekciju
	 * @return Connection konekciju neurona
	 */
	public Connection getConnection(int neuronIndex) {
		return connectionLookup.get(neuronIndex);
	}

	/**
	 * Metoda za dodavanje konekcija trenutnom neuronu
	 *
	 * @param con nova ulazna konekcija
	 */
	public void addInConnection(Connection con) {
		Inconnections.add(con);
	}

	/**
	 * Postavlja input neurona.
	 * <br> Ova metoda se treba koristit samo za postavlja neurona input layera.
	 *
	 * @param input
	 */
	public void setInput(double input){
		this.input = input;
	}

	/**
	 * Vraća listu svih ulaznih konekcija.
	 *
	 * @return lista konekcija
	 */
	public ArrayList<Connection> getAllInConnections() {
		return Inconnections;
	}
}
