package predictor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.learning.error.MeanSquaredError;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.DynamicBackPropagation;
import org.neuroph.util.TransferFunctionType;

public final class Predictor {
	private static final int DEFAULT_MAX_ITER = 10000;
	private static final double DEFAULT_NORM_MIN_VAL = 1.0;
	private static final double DEFAULT_NORM_MAX_VAL = 99.0;
	private static final double DEFAULT_LEARN_RATE = 0.25;
	private static final double DEFAULT_MAX_ERROR = 0.15;
	private static final double DEFAULT_MOMENTUM = 0.25;

	private int inputs;
	private int outputs;
	private int maxIterations;
	private double normMinVal;
	private double normMaxVal;
	private double learnRate;
	private double maxError;
	private double momentum;
	private DataSet trainingSet;
	private DynamicBackPropagation learningRule;
	private MultiLayerPerceptron neuralNetwork;

	public Predictor(int inputs, int outputs) {
		this.inputs = inputs;
		this.outputs = outputs;
		maxIterations = DEFAULT_MAX_ITER;
		normMinVal = DEFAULT_NORM_MIN_VAL;
		normMaxVal = DEFAULT_NORM_MAX_VAL;
		learnRate = DEFAULT_LEARN_RATE;
		maxError = DEFAULT_MAX_ERROR;
		momentum = DEFAULT_MOMENTUM;
		setup();
	}

	public Predictor(int inputs, int outputs, InputStream csv) {
		this.inputs = inputs;
		this.outputs = outputs;
		maxIterations = DEFAULT_MAX_ITER;
		normMinVal = DEFAULT_NORM_MIN_VAL;
		normMaxVal = DEFAULT_NORM_MAX_VAL;
		learnRate = DEFAULT_LEARN_RATE;
		maxError = DEFAULT_MAX_ERROR;
		momentum = DEFAULT_MOMENTUM;
		setup();
		addTrainDataFromCsv(csv);
	}

	public Predictor(int inputs, int outputs, int maxIterations, double normMinVal, double normMaxVal, double learnRate,
	    double maxError, double momentum) {
		this.inputs = inputs;
		this.outputs = outputs;
		this.maxIterations = maxIterations;
		this.normMinVal = normMinVal;
		this.normMaxVal = normMaxVal;
		this.learnRate = learnRate;
		this.maxError = maxError;
		this.momentum = momentum;
		setup();
	}

	public Predictor(int inputs, int outputs, int maxIterations, double normMinVal, double normMaxVal, double learnRate,
	    double maxError, double momentum, InputStream csv) {
		this.inputs = inputs;
		this.outputs = outputs;
		this.maxIterations = maxIterations;
		this.normMinVal = normMinVal;
		this.normMaxVal = normMaxVal;
		this.learnRate = learnRate;
		this.maxError = maxError;
		this.momentum = momentum;
		setup();
		addTrainDataFromCsv(csv);
	}

	public void learn() {
		neuralNetwork = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, inputs, 2 * inputs + outputs, outputs);
		neuralNetwork.learn(trainingSet, learningRule);
	}

	public PredictorResultDto calculate(double[] input) {
		if (neuralNetwork != null) {
			neuralNetwork.setInput(normalize(input));
			neuralNetwork.calculate();
			PredictorResultDto result = getResult();
			result.setInput(input);
			return result;
		} else {
			return null;
		}
	}

	public List<PredictorResultDto> calculate(InputStream csv) {
		if (neuralNetwork != null) {
			return calculateForTestDataFromCsv(csv);
		}
		return null;
	}

	public void addData(double[] input, double[] output) {
		trainingSet.add(normalize(input), output);
	}

	public PredictorDto toDto() {
		PredictorDto dto = new PredictorDto();
		dto.setInputs(inputs);
		dto.setOutputs(outputs);
		dto.setLearnRate(learnRate);
		dto.setMaxError(maxError);
		dto.setMaxIterations(maxIterations);
		dto.setMomentum(momentum);
		dto.setNormMaxVal(normMaxVal);
		dto.setNormMinVal(normMinVal);
		return dto;
	}

	private PredictorResultDto getResult() {
		PredictorResultDto result = new PredictorResultDto();
		if (neuralNetwork != null && neuralNetwork.getOutput() != null) {
			result.setCurrentIteration(learningRule.getCurrentIteration());
			result.setTotalNetworkError(learningRule.getTotalNetworkError());
			result.setOutput(neuralNetwork.getOutput());
		}
		return result;
	}

	private void setup() {
		learningRule = new DynamicBackPropagation();
		learningRule.setErrorFunction(new MeanSquaredError());
		learningRule.setMaxIterations(maxIterations);
		learningRule.setLearningRate(learnRate);
		learningRule.setMaxError(maxError);
		learningRule.setMomentum(momentum);
		learningRule.setBatchMode(false);
		trainingSet = new DataSet(inputs, outputs);
	}

	private double[] normalize(double[] values) {
		double[] normalized = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			normalized[i] = 1.0 - ((values[i] - normMinVal) / (normMaxVal - normMinVal));
		}
		return normalized;
	}

	private void addTrainDataFromCsv(InputStream csv) {
		try {
			int lineNum = 0;
			BufferedReader reader = new BufferedReader(new InputStreamReader(csv));
			while (reader.ready()) {
				String line = reader.readLine();
				if (lineNum != 0) {
					String[] split = line.split(",");
					double[] inputValues = new double[inputs];
					double[] outputValues = new double[outputs];
					int pos = 0;
					for (int i = 0; i < inputs; i++) {
						try {
							inputValues[pos] = Double.parseDouble(split[i]);
						} catch (NumberFormatException ex) {
							inputValues[pos] = 0.0;
						}
						pos += 1;
					}
					pos = 0;
					for (int i = inputs; i < inputs + outputs; i++) {
						try {
							outputValues[pos] = Double.parseDouble(split[i]);
						} catch (NumberFormatException ex) {
							outputValues[pos] = 0.0;
						}
						pos += 1;
					}
					addData(inputValues, outputValues);
				}
				lineNum += 1;
			}
			reader.close();
			csv.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private List<PredictorResultDto> calculateForTestDataFromCsv(InputStream csv) {
		List<PredictorResultDto> results = new ArrayList<>();
		try {
			int lineNum = 0;
			BufferedReader reader = new BufferedReader(new InputStreamReader(csv));
			while (reader.ready()) {
				String line = reader.readLine();
				if (lineNum != 0) {
					String[] split = line.split(",");
					double[] inputValues = new double[inputs];
					int pos = 0;
					for (int i = 0; i < inputs; i++) {
						try {
							inputValues[pos] = Double.parseDouble(split[i]);
						} catch (NumberFormatException ex) {
							inputValues[pos] = 0.0;
						}
						pos += 1;
					}
					results.add(calculate(inputValues));
				}
				lineNum += 1;
			}
			reader.close();
			csv.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return results;
	}
}