package morgcalculator.calculator;

import java.time.LocalDate;
import java.util.ArrayList;
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
		int periodCount = getNumberOfPeriods();
		float balance = getLoanAmount();
		float rate = getYearlyRate();
		LocalDate startDate = LocalDate.now();
		float periodRate = getRateForPeriod(rate);

		Defer defer = getDefer();

		if (defer != null) {
			LocalDate date = defer.getDate();
			float constantPaymentEachMonth = (float) getPeriodPayment(balance, getRateForPeriod(defer.getRate()),
					defer.getLengthMonths());

			for (int i = 0; i < periodCount; i++) {

				float interest = balance * periodRate;
				float principal = constantPaymentEachMonth - interest;

				if (defer != null && defer.getDate().compareTo(startDate) < 30) {
					Integer lengthDefer = defer.getLengthMonths();
					for (int deferI = 0; deferI < lengthDefer; deferI++) {
						constantPaymentEachMonth = (float) getPeriodPayment(balance, getRateForPeriod(defer.getRate()),
								lengthDefer);
						principal = constantPaymentEachMonth - interest;
						Payment payment = new Payment(deferI + 1, startDate, defer.getRate(), interest, principal,
								constantPaymentEachMonth);
						payments.add(payment);
						startDate = startDate.plusMonths(1);
						balance -= principal;
					}
					continue;
				}

				Payment payment = new Payment(i + 1, startDate, rate, interest, principal, constantPaymentEachMonth);
				payments.add(payment);

				startDate = startDate.plusMonths(1);
				balance -= principal;
			}

		}

		float constantPaymentEachMonth = (float) getPeriodPayment(balance, periodRate, periodCount);
		for (int i = 0; i < periodCount; i++) {
			float constantPaymentEachMonth = (float) getPeriodPayment(balance, periodRate, periodCount);

			float interest = balance * periodRate;
			float principal = constantPaymentEachMonth - interest;

			if (defer != null && defer.getDate().compareTo(startDate) < 30) {
				Integer lengthDefer = defer.getLengthMonths();
				for (int deferI = 0; deferI < lengthDefer; deferI++) {
					constantPaymentEachMonth = (float) getPeriodPayment(balance, getRateForPeriod(defer.getRate()),
							lengthDefer);
					principal = constantPaymentEachMonth - interest;
					Payment payment = new Payment(deferI + 1, startDate, defer.getRate(), interest, principal,
							constantPaymentEachMonth);
					payments.add(payment);
					startDate = startDate.plusMonths(1);
					balance -= principal;
				}
				continue;
			}

			Payment payment = new Payment(i + 1, startDate, rate, interest, principal, constantPaymentEachMonth);
			payments.add(payment);

			startDate = startDate.plusMonths(1);
			balance -= principal;
		}

		return payments;
	}

	private double getPeriodPayment(float totalAmount, float rate, float periodCount) {
		return totalAmount * (rate * Math.pow(1 + rate, periodCount)) / (Math.pow(1 + rate, periodCount) - 1);
	}
}
