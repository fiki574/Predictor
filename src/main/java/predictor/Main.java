package predictor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class Main {
	public static void main(String[] args) throws InterruptedException, FileNotFoundException {
		File train = new File("C:/Users/Bruno/Desktop/train.csv");
		File test = new File("C:/Users/Bruno/Desktop/test.csv");
		Predictor predictor = new Predictor(3, 2, new FileInputStream(train));

		int run = 0;
		while (++run <= 20) {
			System.out.println(run + ". run");

			predictor.learn();
			List<PredictorResultDto> results = predictor.calculate(new FileInputStream(test));
			results.forEach(System.out::println);

			System.out.println();
			Thread.sleep(2000);
		}
	}
}