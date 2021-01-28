package hr.bzg.nn.dto;

import java.util.Objects;

public class PredictorDto {
	private int inputs;
	private int outputs;
	private int maxIterations;
	private double normMinVal;
	private double normMaxVal;
	private double learnRate;
	private double maxError;
	private double momentum;

	public int getInputs() {
		return inputs;
	}

	public void setInputs(int inputs) {
		this.inputs = inputs;
	}

	public int getOutputs() {
		return outputs;
	}

	public void setOutputs(int outputs) {
		this.outputs = outputs;
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public double getNormMinVal() {
		return normMinVal;
	}

	public void setNormMinVal(double normMinVal) {
		this.normMinVal = normMinVal;
	}

	public double getNormMaxVal() {
		return normMaxVal;
	}

	public void setNormMaxVal(double normMaxVal) {
		this.normMaxVal = normMaxVal;
	}

	public double getLearnRate() {
		return learnRate;
	}

	public void setLearnRate(double learnRate) {
		this.learnRate = learnRate;
	}

	public double getMaxError() {
		return maxError;
	}

	public void setMaxError(double maxError) {
		this.maxError = maxError;
	}

	public double getMomentum() {
		return momentum;
	}

	public void setMomentum(double momentum) {
		this.momentum = momentum;
	}

	@Override
	public String toString() {
		return "PredictorDto [inputs=" + inputs + ", outputs=" + outputs + ", maxIterations=" + maxIterations
		    + ", normMinVal=" + normMinVal + ", normMaxVal=" + normMaxVal + ", learnRate=" + learnRate + ", maxError="
		    + maxError + ", momentum=" + momentum + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(inputs, learnRate, maxError, maxIterations, momentum, normMaxVal, normMinVal, outputs);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof PredictorDto)) {
			return false;
		}
		PredictorDto other = (PredictorDto) obj;
		return inputs == other.inputs && Double.doubleToLongBits(learnRate) == Double.doubleToLongBits(other.learnRate)
		    && Double.doubleToLongBits(maxError) == Double.doubleToLongBits(other.maxError)
		    && maxIterations == other.maxIterations
		    && Double.doubleToLongBits(momentum) == Double.doubleToLongBits(other.momentum)
		    && Double.doubleToLongBits(normMaxVal) == Double.doubleToLongBits(other.normMaxVal)
		    && Double.doubleToLongBits(normMinVal) == Double.doubleToLongBits(other.normMinVal) && outputs == other.outputs;
	}
}