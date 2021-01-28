package hr.bzg.nn.service.impl;

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

import hr.bzg.nn.Constants;
import hr.bzg.nn.dto.PredictorDto;
import hr.bzg.nn.dto.PredictorResultDto;
import hr.bzg.nn.service.PredictorService;

public final class PredictorServiceImpl implements PredictorService {
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

	public PredictorServiceImpl(int inputs, int outputs) {
		this.inputs = inputs;
		this.outputs = outputs;
		maxIterations = Constants.DEFAULT_MAX_ITER;
		normMinVal = Constants.DEFAULT_NORM_MIN_VAL;
		normMaxVal = Constants.DEFAULT_NORM_MAX_VAL;
		learnRate = Constants.DEFAULT_LEARN_RATE;
		maxError = Constants.DEFAULT_MAX_ERROR;
		momentum = Constants.DEFAULT_MOMENTUM;
	}

	public PredictorServiceImpl(int inputs, int outputs, int maxIterations, double normMinVal, double normMaxVal,
	    double learnRate, double maxError, double momentum) {
		this.inputs = inputs;
		this.outputs = outputs;
		this.maxIterations = maxIterations;
		this.normMinVal = normMinVal;
		this.normMaxVal = normMaxVal;
		this.learnRate = learnRate;
		this.maxError = maxError;
		this.momentum = momentum;
	}

	@Override
	public void setup() {
		learningRule = new DynamicBackPropagation();
		learningRule.setErrorFunction(new MeanSquaredError());
		learningRule.setMaxIterations(maxIterations);
		learningRule.setLearningRate(learnRate);
		learningRule.setMaxError(maxError);
		learningRule.setMomentum(momentum);
		learningRule.setBatchMode(false);
		trainingSet = new DataSet(inputs, outputs);
	}

	@Override
	public void addData(double[] input, double[] output) {
		trainingSet.add(normalize(input), output);
	}

	@Override
	public void addData(InputStream csv) throws IOException {
		addTrainDataFromCsv(csv);
	}

	@Override
	public PredictorResultDto calculate(double[] input) {
		neuralNetwork = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, inputs, 2 * inputs + outputs, outputs);
		neuralNetwork.learn(trainingSet, learningRule);
		neuralNetwork.setInput(normalize(input));
		neuralNetwork.calculate();
		PredictorResultDto result = new PredictorResultDto();
		result.setInput(input);
		result.setCurrentIteration(learningRule.getCurrentIteration());
		result.setTotalNetworkError(learningRule.getTotalNetworkError());
		result.setOutput(neuralNetwork.getOutput());
		return result;
	}

	@Override
	public List<PredictorResultDto> calculate(InputStream csv) throws IOException {
		return calculateForTestDataFromCsv(csv);
	}

	@Override
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

	private double[] normalize(double[] values) {
		double[] normalized = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			normalized[i] = 1.0 - ((values[i] - normMinVal) / (normMaxVal - normMinVal));
		}
		return normalized;
	}

	private void addTrainDataFromCsv(InputStream csv) throws IOException {
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
	}

	private List<PredictorResultDto> calculateForTestDataFromCsv(InputStream csv) throws IOException {
		List<PredictorResultDto> results = new ArrayList<>();
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
		return results;
	}
}