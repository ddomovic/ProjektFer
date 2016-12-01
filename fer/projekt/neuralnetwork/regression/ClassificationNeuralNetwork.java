package fer.projekt.neuralnetwork.regression;

public class ClassificationNeuralNetwork extends LinearRegressionNetwork{

public static final IMathFunction LEARNING_FUNC2 = new IMathFunction() {
		
		@Override
		public String getFuncName() {
			return "klasifikacijska";
		}
		
		@Override
		public double calculate(double input) {
			if(input%3 <= 1){
				return 1;
			}
			else return -1;
		}

		@Override
		public double getDomainMin() {
			return 0;
		}

		@Override
		public double getDomainMax() {
			return 6;
		}
	};
	
	public double runTests(double treshold , boolean takeRandomTests) {
		int brojTestova = 100;
		
		double totalPercErr = 0;
		int total = 0;
		double testIn = LEARNING_FUNC2.getDomainMin();
		double step = (LEARNING_FUNC2.getDomainMax() - LEARNING_FUNC2.getDomainMin()) / brojTestova;
		
		System.out.println("\t\t\tOCEKIVANO: DOBIVENO:    POGREŠKA[%]");
		for (int i = 0; i < brojTestova; i++, total++, testIn += step) {
			if (takeRandomTests) {
				testIn = Math.random() * (LEARNING_FUNC2.getDomainMax() - LEARNING_FUNC2.getDomainMin());
			}
			double correct = LEARNING_FUNC2.calculate(testIn);
			double out = this.run(testIn);
//			out = (out > treshold) ? 1 : -1;
			double percErr = Math.abs(out - correct) / Math.abs(correct) * 100;
			if (correct > 0.00001 ) {
				totalPercErr += percErr;
			}
			System.out.printf("%4d. %s(%.5f)= \t%.10f \t%.10f \t %.3f%% %n", i + 1, 
					LEARNING_FUNC2.getFuncName(), testIn, correct, out, percErr);
		}
		double avgPerc = totalPercErr / total;
		System.out.printf("%nProsječna pogreška je: %.5f%% %n", avgPerc);
		return avgPerc;
	}
	
	public static void main(String[] args) {
		
		
		ClassificationNeuralNetwork network = new ClassificationNeuralNetwork();
		network.runTests(2, false);
		

	}

}
