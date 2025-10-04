package morgcalculator.calculator;

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

		return payments;
	}
}
