package morgcalculator.calculator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Payment {
	private int id;
	private int year;
	private int month;
	private float percent;
	private float interest;
	private float payment;
	private float totalPayment;
}
