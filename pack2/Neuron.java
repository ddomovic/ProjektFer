package pack2;
import java.util.ArrayList;
import java.util.HashMap;


public class Neuron {
	private static int COUNTER = 0;
	private int id;

	double output;
	double input = 0;
	//lista ulaznih konekcija u neuron
	ArrayList<Connection> Inconnections = new ArrayList<Connection>();
	//Mapa za dohvacanje i pregled konekcija na neuron izvana
	HashMap<Integer, Connection> connectionLookup = new HashMap<Integer, Connection>();

	private ITransferFunction tfunction;
	private double bias;

	public Neuron(ITransferFunction tfunction) {
		this(0, 0, tfunction);
	}

	public Neuron(double input, double bias, ITransferFunction tfunction){
		this.input = input;
		this.bias = bias;
		this.id = COUNTER++;
		this.tfunction = tfunction;
	}

	/**
	 * Metoda koja vraca izlaz neurona
	 * @return output
	 *
	 */
	public double calculateOutput(){
		if (output != 0) {
			return output;
		}

		double sum = input;
		for(Connection connection : Inconnections){
			Neuron inputNeuron = connection.getLeft();
			sum += inputNeuron.calculateOutput()*connection.getWeight();
		}
		output = tfunction.applyFunction(sum);
		return output;
	}

	/**
	 * Metoda za istovremeno dodavanje vise konekcija trenutnom neuronu
	 * @param inNeurons
	 * @author Nikola
	 */
	public void addInConnectionsS(ArrayList<Neuron> inNeurons) {
		for (Neuron n : inNeurons) {
			Connection con = new Connection(n, this);
			Inconnections.add(con);
			connectionLookup.put(n.id, con);
		}
	}

	/**
	 * Metoda koja nam vraca konekciju neurona nad kojim zovemo sa neuronom iz argumenta
	 * @param Index lijevog neurona
	 * @return Connection
	 * @author Nikola
	 */
	public Connection getConnection(int neuronIndex) {
		return connectionLookup.get(neuronIndex);
	}

	/**
	 * Metoda za dodavanje konekcija trenutnom neuronu
	 * @param connection
	 * @author Nikola
	 */
	public void addInConnection(Connection con) {
		Inconnections.add(con);
	}

	public ArrayList<Connection> getAllInConnections() {
		return Inconnections;
	}
}
