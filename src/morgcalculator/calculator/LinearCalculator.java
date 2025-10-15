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

		Defer defer = getDefer();
		LocalDate deferStartDate = (defer != null) ? defer.getDate() : null;
		LocalDate deferEndDate = (defer != null) ? deferStartDate.plusMonths(defer.getLengthMonths()) : null;
		float deferRate = (defer != null) ? defer.getRate() / 100f : 1f;

//		if (defer != null) {
//			periodCount += defer.getLengthMonths();
//			loanPayment = getLoanAmount() / periodCount;
//		}

		for (int i = 0; i < periodCount; i++) {
			boolean isDeferement = defer != null && !startDate.isBefore(deferStartDate)
					&& !startDate.isAfter(deferEndDate);

			float interest = balance * periodRate;
			float totalPayment = loanPayment + interest;
			float principal = loanPayment;

			if (isDeferement) {
				totalPayment *= deferRate;
				principal = totalPayment - interest;
				System.out.println("principal: " + principal);
				System.out.println("to pay: " + loanPayment);
				System.out.println("leftover:" + (loanPayment - principal));
			}
			balance -= principal;

			Payment payment = new Payment(i + 1, startDate, rate, interest, principal, totalPayment);
			payments.add(payment);

			startDate = startDate.plusMonths(1);
		}

		return payments;
	}
}
