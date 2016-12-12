package pack1;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

public class Interpolation {

	public static double[] linearInterp (double[] x, double[] y, double[] xi) {

		LinearInterpolator li = new LinearInterpolator();
		PolynomialSplineFunction psf = li.interpolate(x, y);

		double[] yi = new double[xi.length];
		for (int i = 0; i < xi.length; i++) {

			yi[i] = psf.value(xi[i]);

		}

		return yi;

	}

	public static void main(String[] args) {
		Path file = Paths.get("D:/ProjektFer/PodaciZaAktivacijskuFunkciju.txt");
		double[] x;
		double[] y;

		try (BufferedReader reader = Files.newBufferedReader(file)) {
			String line = null;
			ArrayList<Double> tempx = new ArrayList<>();
			ArrayList<Double> tempy = new ArrayList<>();

			while ((line = reader.readLine()) != null) {
				line = line.replaceAll(",", ".");
				String[] parts = line.split(" ");
				tempx.add(Double.parseDouble(parts[0].trim()));
				tempy.add(Double.parseDouble(parts[1].trim()));

			}

			x = new double[tempx.size()];
			y = new double[tempy.size()];

			for (int i = 0; i < tempx.size(); i++) {
				x[i] = tempx.get(i);
				y[i] = tempy.get(i);
			}

			System.out.println("   X        Y");
			for (int i = 0; i < x.length; i++) {
				System.out.println(x[i] + "   " + y[i]);
			}

			//test
			double[] xi = new double[245*3 + 1];
			double[] yi = new double[xi.length];

			for (int i = 0; i < xi.length; i++) {
				xi[i] = 1400 + i/3.0;
			}

			yi = linearInterp(x, y, xi);
			for (int i = 0; i < xi.length; i++) {
				System.out.printf("%6.2f  %6.6f\n", xi[i], yi[i]);
			}

			FunctionPlot fp = new FunctionPlot(xi, yi, 0, 0);
			JFrame frame = new JFrame("Graph");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(fp);
			frame.setSize(1000, 1000);
			frame.setVisible(true);

		} catch (IOException ex) {
			System.err.println(ex);
		}

	}

}
