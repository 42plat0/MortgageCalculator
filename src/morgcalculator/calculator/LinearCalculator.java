package morgcalculator.calculator;

import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LinearCalculator extends MortgageCalculator {
	public LinearCalculator(float loanAmount, float yearlyRate, int loanTermYear, int loanTermMonth) {
		super(loanAmount, yearlyRate, loanTermYear, loanTermMonth);
	}

	@Override
	public List<Payment> calculatePayments() {
		// Meant to be overrided
		List<Payment> payments = new ArrayList<Payment>();

		float periodRate = getRateForPeriod();
		int periodCount = getNumberOfPeriods();
		float loanPayment = getLoanAmount() / periodCount;

		int currentYear = Year.now().getValue();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int currentMonth = cal.get(Calendar.MONTH);

		int paymentYear = currentYear;
		int paymentMonth = currentMonth;

		float balance = getLoanAmount();
		for (int month = 1; month < periodCount + 1; month++) {
			float interest = balance * periodRate;
			float totalPayment = loanPayment + interest;
			balance -= loanPayment;
			paymentMonth += 1;

			if (paymentMonth > 12) {
				paymentYear += 1;
				paymentMonth = 1;
			}

			payments.add(new Payment(month, paymentYear, paymentMonth, getYearlyRate(), interest, loanPayment,
					totalPayment));
		}
		// TODO dynamic last row displaying total sum which changes when payment is paid

		return payments;
	}
}
