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

		Defer defer = getDefer();
		LocalDate deferStartDate = (defer != null) ? defer.getDate() : null;
		LocalDate deferEndDate = (defer != null) ? deferStartDate.plusMonths(defer.getLengthMonths()) : null;
		float deferRate = (defer != null) ? defer.getRate() / 100f : 1f;

		float loanPayment = getLoanAmount() / periodCount;
		if (defer != null) {
			periodCount += defer.getLengthMonths();
		}

		float balance = getLoanAmount();
		LocalDate startDate = LocalDate.now();
		Float payedInTotal = 0f;

		for (int i = 0; i < periodCount; i++) {
			boolean isDeferement = defer != null && !startDate.isBefore(deferStartDate)
					&& !startDate.isAfter(deferEndDate);
			float interest = balance * periodRate;
			float totalPayment = isDeferement ? (loanPayment * deferRate) + interest : loanPayment + interest;
			float principal = totalPayment - interest;
			payedInTotal += totalPayment;
			rate = isDeferement ? defer.getRate() : getYearlyRate();
			if (balance < 0) {
				break;
			}

			if (isDeferement) {
				balance += (loanPayment - totalPayment);
			}
			balance -= principal;

			Payment payment = new Payment(i + 1, startDate, rate, interest, principal, totalPayment, payedInTotal);
			payments.add(payment);

			startDate = startDate.plusMonths(1);
		}

		return payments;
	}
}
