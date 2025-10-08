package morgcalculator.calculator;

public class MyUtils {
	public static int roundUpToNextNiceNumber(double number) {
		double digits = Math.floor(Math.log10(number));
		double base = Math.pow(10, digits);
		if (number > base) {
			base *= 10;
		}
		return (int) (Math.ceil(number / base) * base);
	}

	public static int getBase(double number) {
		double digits = Math.floor(Math.log10(number));
		double base = Math.pow(10, digits);
		return (int) base;
	}
}
