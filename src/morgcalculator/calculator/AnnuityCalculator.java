package morgcalculator.calculator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AnnuityCalculator extends MortgageCalculator {

	public AnnuityCalculator(float loanAmount, float yearlyRate, int loanTermYear, int loanTermMonth) {
		super(loanAmount, yearlyRate, loanTermYear, loanTermMonth);
	}

	@Override
	public List<Payment> calculatePayments() {
		// Meant to be overrided
		List<Payment> payments = new ArrayList<Payment>();
		List<Payment> deferredPayments = new ArrayList<Payment>();
		List<Payment> normalPayments = new ArrayList<Payment>();

		int periodCount = getNumberOfPeriods();
		float balance = getLoanAmount();
		LocalDate startDate = LocalDate.now();
		float periodRate = getRateForPeriod(getYearlyRate());

		Defer defer = getDefer();
		LocalDate deferStartDate = null;
		LocalDate deferEndDate = null;
		Float payedInTotal = 0f;

		float constantPaymentEachMonth = (float) getPeriodPayment(balance, periodRate, periodCount);

		if (defer != null) {
			deferStartDate = defer.getDate();
			deferEndDate = deferStartDate.plusMonths(defer.getLengthMonths());
			LocalDate defEndClone = deferStartDate;
			for (int i = 0; i < defer.getLengthMonths(); i++) {
				float interest = balance * periodRate;
				float paymentThisMonth = constantPaymentEachMonth * (defer.getRate() / 100f) + interest;
				float principal = paymentThisMonth - interest;
				payedInTotal += paymentThisMonth;
				System.out.println("i: " + i + " principal: " + principal);
				Payment payment = new Payment(i + 1, defEndClone, defer.getRate(), interest, principal,
						paymentThisMonth, payedInTotal);
				deferredPayments.add(payment);
				defEndClone = defEndClone.plusMonths(1);
				balance -= principal;
			}
		}

		constantPaymentEachMonth = (float) getPeriodPayment(balance, periodRate, periodCount);
		if (defer != null) {
			periodCount += defer.getLengthMonths();
		}
		for (int i = 0; i < periodCount; i++) {
			boolean isDeferement = defer != null && !startDate.isBefore(deferStartDate)
					&& !startDate.isAfter(deferEndDate);
			float rate = getYearlyRate();
			float interest = balance * periodRate;
			float paymentThisMonth = constantPaymentEachMonth;
			float principal = paymentThisMonth - interest;
			if (isDeferement) {
				startDate = startDate.plusMonths(1);
				continue;
			}

			payedInTotal += paymentThisMonth;
			Payment payment = new Payment(i + 1, startDate, rate, interest, principal, paymentThisMonth, payedInTotal);
			normalPayments.add(payment);

			startDate = startDate.plusMonths(1);
			balance -= principal;
		}

		if (deferredPayments.size() > 0) {
			payments.addAll(normalPayments);
			payments.addAll(deferredPayments);
			payments.sort(Comparator.comparing(Payment::getDate));
			for (int i = 0; i < payments.size(); i++) {
				payments.get(i).setId(i + 1);
			}
		} else {
			payments.addAll(normalPayments);
		}

		return payments;
	}

	private double getPeriodPayment(float totalAmount, float rate, float periodCount) {
		return totalAmount * (rate * Math.pow(1 + rate, periodCount)) / (Math.pow(1 + rate, periodCount) - 1);
	}
}
