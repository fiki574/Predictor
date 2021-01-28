package hr.bzg.nn.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import hr.bzg.nn.dto.PredictorDto;
import hr.bzg.nn.dto.PredictorResultDto;

public interface PredictorService {
	void setup();

	void addData(double[] input, double[] output);

	void addData(InputStream csv) throws IOException;

	PredictorResultDto calculate(double[] input);

	List<PredictorResultDto> calculate(InputStream csv) throws IOException;

	PredictorDto toDto();
}