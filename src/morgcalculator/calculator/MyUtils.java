package morgcalculator.calculator;

import java.util.List;

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

	public static String convertToCSV(List<Payment> dataList) {
		if (dataList.isEmpty()) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		// Headers
		sb.append("id,").append("date,").append("percent,").append("interest,").append("periodPayment,")
				.append("totalPayment").append("\n");
		// Data
		dataList.forEach(payment -> sb.append(payment + "\n"));

		return sb.toString();
	}

}
