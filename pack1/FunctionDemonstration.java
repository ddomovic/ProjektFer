package pack1;

import fer.projekt.neuralnetwork.activationfunction.WaveTransferFunction;

public class FunctionDemonstration {

	public static void main(String[] args) {
		WaveTransferFunction funkcija = new WaveTransferFunction("D:/ProjektFer/PodaciZaAktivacijskuFunkciju.txt");
		System.out.println("x:            y:");
		for(double i = 0; i<100; i +=0.5){

			System.out.println(i + "            " + funkcija.applyFunction(i));

		}


	}

}
