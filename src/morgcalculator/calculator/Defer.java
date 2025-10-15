package morgcalculator.calculator;

import java.time.LocalDate;

public class Defer {
	private LocalDate date;
	private Integer lengthMonths;
	private Float rate;

	public Defer(LocalDate date, Integer lengthMonths, Float rate) {
		this.date = date;
		this.lengthMonths = lengthMonths;
		this.rate = rate;
	}

	public LocalDate getDate() {
		return date;
	}

	public Integer getLengthMonths() {
		return lengthMonths;
	}

	public Float getRate() {
		return rate;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setLengthMonths(Integer lengthMonths) {
		this.lengthMonths = lengthMonths;
	}

	public void setRate(Float rate) {
		this.rate = rate;
	}

}
