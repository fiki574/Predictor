package hr.bzg.nn;

public class Arguments {
	private int inputs;
	private int outputs;
	private String trainPath;
	private String testPath;

	public Arguments(int inputs, int outputs, String trainPath, String testPath) {
		this.inputs = inputs;
		this.outputs = outputs;
		this.trainPath = trainPath;
		this.testPath = testPath;
	}

	public static Arguments parse(String[] args) {
		int inputs = 0, outputs = 0;
		String trainPath = "", testPath = "";
		for (String arg : args) {
			if (arg.contains("--in=") && inputs == 0) {
				inputs = Integer.parseInt(arg.substring("--in=".length()));
			} else if (arg.contains("--out=") && outputs == 0) {
				outputs = Integer.parseInt(arg.substring("--out=".length()));
			} else if (arg.contains("--train=") && trainPath.isEmpty()) {
				trainPath = arg.substring("--train=".length());
			} else if (arg.contains("--test=") && testPath.isEmpty()) {
				testPath = arg.substring("--test=".length());
			}
		}
		return new Arguments(inputs, outputs, trainPath, testPath);
	}

	public int getInputs() {
		return inputs;
	}

	public int getOutputs() {
		return outputs;
	}

	public String getTrainPath() {
		return trainPath;
	}

	public String getTestPath() {
		return testPath;
	}
}