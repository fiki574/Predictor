package predictor;

import java.util.Arrays;
import java.util.Objects;

public class PredictorResultDto {
	private int currentIteration;
	private double totalNetworkError;
	private double[] output;
	private double[] input;

	public int getCurrentIteration() {
		return currentIteration;
	}

	public void setCurrentIteration(int currentIteration) {
		this.currentIteration = currentIteration;
	}

	public double getTotalNetworkError() {
		return totalNetworkError;
	}

	public void setTotalNetworkError(double totalNetworkError) {
		this.totalNetworkError = totalNetworkError;
	}

	public double[] getOutput() {
		return output;
	}

	public void setOutput(double[] output) {
		this.output = output;
	}

	public double[] getInput() {
		return input;
	}

	public void setInput(double[] input) {
		this.input = input;
	}

	@Override
	public String toString() {
		return "PredictorResultDto [currentIteration=" + currentIteration + ", totalNetworkError=" + totalNetworkError
		    + ", output=" + Arrays.toString(output) + ", input=" + Arrays.toString(input) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Objects.hash(currentIteration, totalNetworkError);
		result = prime * result + Arrays.hashCode(input);
		result = prime * result + Arrays.hashCode(output);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof PredictorResultDto)) {
			return false;
		}
		PredictorResultDto other = (PredictorResultDto) obj;
		return currentIteration == other.currentIteration && Arrays.equals(input, other.input)
		    && Arrays.equals(output, other.output)
		    && Double.doubleToLongBits(totalNetworkError) == Double.doubleToLongBits(other.totalNetworkError);
	}
}