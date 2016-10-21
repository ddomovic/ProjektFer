package fer.projekt.neuralnetwork.activationfunction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

public class WaveTransferFunction implements ITransferFunction {
	
	private PolynomialSplineFunction psf;
	
	public WaveTransferFunction(String path) {
		
		Path file = Paths.get(path);
		double[] x;
		double[] y;
		
		try (InputStream in = Files.newInputStream(file);
		    BufferedReader reader =
		      new BufferedReader(new InputStreamReader(in))) {
		    String line = null;
		    ArrayList<Double> tempx = new ArrayList<>();
		    ArrayList<Double> tempy = new ArrayList<>();
		    
		    while ((line = reader.readLine()) != null) {

		    	line = line.replaceAll(",", ".");
		    	String[] parts = line.split(" ");
		        tempx.add(Double.parseDouble(parts[0]));
		        tempy.add(Double.parseDouble(parts[1]));
		        
		    }
		    
		    x = new double[tempx.size()];
		    y = new double[tempy.size()];
		    
		    for (int i = 0; i < tempx.size(); i++) {
		    	
		    	x[i] = tempx.get(i);
		    	y[i] = tempy.get(i);
		    	
		    }
		    
		    LinearInterpolator li = new LinearInterpolator();
			this.psf = li.interpolate(x, y);
		
		}
		catch (IOException ex) {
			
		    System.err.println(ex);
		
		}
		
	}

	@Override
	public double applyFunction(double z) {
				
		return this.psf.value(Math.abs(z%245) + 1400)*10_000%2200-1100; 
		//skaliranje ulaza nesto ne radi dobro, z%245+1400 na neku foru daje brojeve manje od 1400
		//skaliranje izlaza na [-1,1]
	}

}
