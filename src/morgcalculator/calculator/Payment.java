package morgcalculator.calculator;

public class Payment {
	private int id;
	private int year;
	private int month;
	private float percent;
	private float interest;
	private float periodPayment;
	private float totalPayment;

	public Payment(int id, int year, int month, float percent, float interest, float periodPayment,
			float totalPayment) {
		this.id = id;
		this.year = year;
		this.month = month;
		this.percent = percent;
		this.interest = interest;
		this.periodPayment = periodPayment;
		this.totalPayment = totalPayment;
	}

	public int getId() {
		return id;
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public float getPercent() {
		return percent;
	}

	public float getInterest() {
		return interest;
	}

	public float getPeriodPayment() {
		return periodPayment;
	}

	public float getTotalPayment() {
		return totalPayment;
	}

}
