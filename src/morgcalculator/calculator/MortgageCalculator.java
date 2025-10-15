package morgcalculator.calculator;

import java.time.LocalDate;
import java.util.List;

public class MortgageCalculator {
	public static enum Schedule {
		ANNUITY, LINEAR;
	};

	private float loanAmount;
	// TODO change to double
	private float yearlyRate;
	private int loanTermYear;
	private int loanTermMonth;
	private Defer defer;

	public MortgageCalculator(float loanAmount, float yearlyRate, int loanTermYear, int loanTermMonth) {
		this.loanAmount = loanAmount;
		this.yearlyRate = yearlyRate;
		this.loanTermYear = loanTermYear;
		this.loanTermMonth = loanTermMonth;
	}

	public float getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(float loanAmount) {
		if (loanAmount <= 0) {
			throw new IllegalArgumentException("laon amount > 0");
		}
		this.loanAmount = loanAmount;
	}

	public float getYearlyRate() {
		return yearlyRate;
	}

	public void setYearlyRate(float yearlyRate) {
		this.yearlyRate = yearlyRate;
	}

	public int getLoanTermYear() {
		return loanTermYear;
	}

	public void setLoanTermYear(int loanTermYear) {
		this.loanTermYear = loanTermYear;
	}

	public int getLoanTermMonth() {
		return loanTermMonth;
	}

	public void setLoanTermMonth(int loanTermMonth) {
		this.loanTermMonth = loanTermMonth;
	}

	public Defer getDefer() {
		return defer;
	}

	public void setDefer(Defer defer) {
		this.defer = defer;
	}

	// Other
	public static Schedule getSchedule(int type) {
		if (type == 0) {
			return Schedule.ANNUITY;
		} else if (type == 1) {
			return Schedule.LINEAR;
		}
		return null;
	}

	protected float getRateForPeriod(float rate) {
		return rate / 100 / 12;
	}

	protected int getNumberOfPeriods() {
		return loanTermYear * 12 + loanTermMonth;
	}

	public List<Payment> calculatePayments() {
		// Meant to be overriden
		return null;
	}

	public List<Payment> calculateDeferredPayments(List<Payment> payments, LocalDate deferStart, int deferMonthsCount) {
		return null;
	}
}
