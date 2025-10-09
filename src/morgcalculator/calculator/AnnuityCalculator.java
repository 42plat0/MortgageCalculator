package morgcalculator.calculator;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AnnuityCalculator extends MortgageCalculator {

	public AnnuityCalculator(float loanAmount, float yearlyRate, int loanTermYear, int loanTermMonth) {
		super(loanAmount, yearlyRate, loanTermYear, loanTermMonth);
	}

	@Override
	public List<Payment> calculatePayments() {
		// Meant to be overrided
		List<Payment> payments = new ArrayList<Payment>();
		float periodRate = getRateForPeriod();
		int periodCount = getNumberOfPeriods();

		int currentYear = Year.now().getValue();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int currentMonth = cal.get(Calendar.MONTH);

		int paymentYear = currentYear;
		int paymentMonth = currentMonth + 1;

		float balance = getLoanAmount();
		float constantPaymentEachMonth = (float) getPeriodPayment();
		for (int month = 1; month < periodCount + 1; month++) {
			float interest = balance * periodRate;
			float principal = constantPaymentEachMonth - interest;

			payments.add(new Payment(month, paymentYear, paymentMonth, getYearlyRate(), interest, principal,
					constantPaymentEachMonth));

			paymentMonth += 1;
			if (paymentMonth > 12) {
				paymentYear += 1;
				paymentMonth = 1;
			}
			balance -= principal;
		}

		return payments;
	}

	@Override
	public List<Payment> calculateDeferredPayments(List<Payment> payments, LocalDate deferStart, int deferMonthsCount) {
		if (payments == null || payments.isEmpty()) {
			return null;
		}
		// Atidejimas turi priimti procenta, t.y. nauja metini procenta, kuri mokes tuos
		// menesius.

		List<Payment> deferredPayments = new ArrayList<Payment>(payments.size() + deferMonthsCount);

		for (Payment payment : payments) {
			deferredPayments.add(payment);

		}

		return deferredPayments;
	}

	private double getPeriodPayment() {
		float totalAmount = getLoanAmount();
		float periodRate = getRateForPeriod();
		float periodCount = getNumberOfPeriods();

		return totalAmount * (periodRate * Math.pow(1 + periodRate, periodCount))
				/ (Math.pow(1 + periodRate, periodCount) - 1);
	}
}
