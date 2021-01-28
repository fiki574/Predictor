package hr.bzg.nn;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import hr.bzg.nn.dto.PredictorResultDto;
import hr.bzg.nn.service.impl.PredictorServiceImpl;

public class Main {
	public static void main(String[] args) throws InterruptedException, IOException {
		// java -jar predictor-0.0.1-SNAPSHOT-jar-with-dependencies.jar
		// --in=3 --out=2 --train=./train.csv --test=./test.csv
		Arguments arg = Arguments.parse(args);
		File train = new File(arg.getTrainPath());
		File test = new File(arg.getTestPath());
		PredictorServiceImpl predictor = new PredictorServiceImpl(arg.getInputs(), arg.getOutputs());
		predictor.setup();
		predictor.addData(new FileInputStream(train));
		List<PredictorResultDto> results = predictor.calculate(new FileInputStream(test));
		results.forEach(System.out::println);
	}
}