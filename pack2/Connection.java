package pack2;
/**
 * klasa koja predstavlja jednu konekciju izmedu dva neurona
 * @author Nikola,
 *
 */
//koristimo klasu, a ne polja jer cemo poslije trebati jos informacija prenositi + ovo je najpoopcenitijiji
//nacin na koji to mozemo izvesti -Nikola
public class Connection {
	private final Neuron leftN,rightN;
	private double weight;
	private static int counter = 0;
	private int id = 0;
	//originalno sam mislio i weight odmah u konstruktoru setat, ali ispada da nam to komplicira
	//zivot pri inicijalizaciji na random vrijednosti -Nikola
	public Connection(Neuron left, Neuron right){
		this.leftN = left;
		this.rightN = right;
		this.id = counter;
		counter++;
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
    
    /**
     * Metoda koja vraca lijevi/pocetni neuron u konekciji  
     * @return neuron
     * @author Nikola
    */
    public Neuron getLeft(){
    	return leftN;
    }
    
    /**
     * Metoda koja vraca desni/krajnji neuron u konekciji
     * @return Neuron
     * @author Nikola
     */
    public Neuron getRight(){
    	return rightN;
    }
}
