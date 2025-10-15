package morgcalculator.calculator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LinearCalculator extends MortgageCalculator {
	public LinearCalculator(float loanAmount, float yearlyRate, int loanTermYear, int loanTermMonth) {
		super(loanAmount, yearlyRate, loanTermYear, loanTermMonth);
	}

	@Override
	public List<Payment> calculatePayments() {
		// Meant to be overrided
		List<Payment> payments = new ArrayList<Payment>();

		float rate = getYearlyRate();
		float periodRate = getRateForPeriod(rate);
		int periodCount = getNumberOfPeriods();
		float loanPayment = getLoanAmount() / periodCount;

		float balance = getLoanAmount();

		LocalDate startDate = LocalDate.now();

		for (int i = 0; i < periodCount; i++) {
			float interest = balance * periodRate;
			float totalPayment = loanPayment + interest;
			balance -= loanPayment;

			Payment payment = new Payment(i + 1, startDate, rate, interest, loanPayment, totalPayment);
			payments.add(payment);

			startDate = startDate.plusMonths(1);
		}

		return payments;
	}
}
