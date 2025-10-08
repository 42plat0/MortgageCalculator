package morgcalculator.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import morgcalculator.calculator.AnnuityCalculator;
import morgcalculator.calculator.LinearCalculator;
import morgcalculator.calculator.MortgageCalculator;
import morgcalculator.calculator.MyUtils;
import morgcalculator.calculator.Payment;

public class LandingController {
	@FXML
	private TextField loanAmountField;
	@FXML
	private TextField loanTermYearField;
	@FXML
	private TextField loanTermMonthsField;
	@FXML
	private TextField loanYearlyRateField;
	@FXML
	private Button loanCalculateBtn;
	@FXML
	private Text errorText;
	@FXML
	private ComboBox loanScheduleCombo;
	@FXML
	private TableView loanPaymentTable;
	@FXML
	private Button exportBtn;
	@FXML
	private Button deferBtn;
	@FXML
	private Button graphBtn;

	public MortgageCalculator calculator;
	private List<Payment> payments;

	Float loanAmountInput;
	Integer loanTermYearInput;
	Integer loanTermMonthsInput;
	Float loanYearlyRateInput;
	int loanScheduleInput;

	@FXML
	private void handleCalculateAction(ActionEvent event) {
		loanScheduleInput = loanScheduleCombo.getSelectionModel().getSelectedIndex();
		try {
			loanAmountInput = Float.valueOf(loanAmountField.getText());
			loanYearlyRateInput = Float.valueOf(loanYearlyRateField.getText());
			loanTermYearInput = Integer.valueOf(loanTermYearField.getText());
			loanTermMonthsInput = Integer.valueOf(loanTermMonthsField.getText());
		} catch (NumberFormatException e) {
			errorText.setFill(Color.FIREBRICK);
			errorText.setText("Įvestis gali būti tik skaičiai!");
			if (loanAmountInput == null) {
				loanAmountField.setText("");
			}
			if (loanTermYearInput == null) {
				loanTermYearField.setText("");
			}
			if (loanTermMonthsInput == null) {
				loanTermMonthsField.setText("");
			}
			if (loanYearlyRateInput == null) {
				loanYearlyRateField.setText("");
			}
			loanAmountInput = 10000f;
			loanYearlyRateInput = 5f;
			loanTermYearInput = 1;
			loanTermMonthsInput = 0;

//			return;
		}

		if (!errorText.getText().isEmpty()) {
			errorText.setText("");
		}

		exportBtn.setDisable(false);
//		deferBtn.setDisable(false);
		graphBtn.setDisable(false);

		MortgageCalculator.Schedule schedule = MortgageCalculator.getSchedule(loanScheduleInput);

		if (MortgageCalculator.Schedule.ANNUITY == schedule) {
			calculator = new AnnuityCalculator(loanAmountInput, loanYearlyRateInput, loanTermYearInput,
					loanTermMonthsInput);
		} else if (MortgageCalculator.Schedule.LINEAR == schedule) {
			calculator = new LinearCalculator(loanAmountInput, loanYearlyRateInput, loanTermYearInput,
					loanTermMonthsInput);
		}

		if (calculator == null) {
			throw new NullPointerException("no calculator");
		}

		payments = calculator.calculatePayments();

		Payment lastRowInfo = new Payment(0f);
		for (Payment payment : payments) {
			payment.setParentContainer(loanPaymentTable);
			lastRowInfo.setTotalPayment(lastRowInfo.getTotalPayment() + payment.getTotalPayment());

		}

		if (!loanPaymentTable.getItems().isEmpty()) {
			loanPaymentTable.getItems().clear();
		}
		loanPaymentTable.getItems().addAll(payments);
		loanPaymentTable.getItems().add(lastRowInfo);
	}

	@SuppressWarnings("unchecked")
	@FXML
	public void initialize() {
		// ComboBox
		loanScheduleCombo.getItems().removeAll(loanScheduleCombo.getItems());
		loanScheduleCombo.getItems().addAll("Anuiteto", "Linijinis");
		loanScheduleCombo.getSelectionModel().selectFirst();

		// PaymentTable
		TableColumn idCol = new TableColumn("#");
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

		TableColumn yearCol = new TableColumn("Metai");
		yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

		TableColumn monthCol = new TableColumn("Mėnesis");
		monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));

		TableColumn percentCol = new TableColumn("%");
		percentCol.setCellValueFactory(new PropertyValueFactory<>("percent"));

		TableColumn interestCol = new TableColumn("Palūkanos");
		interestCol.setCellValueFactory(new PropertyValueFactory<>("interest"));

		TableColumn periodPaymentCol = new TableColumn("Įmoka");
		periodPaymentCol.setCellValueFactory(new PropertyValueFactory<>("periodPayment"));

		TableColumn totalCol = new TableColumn("Viso mokėti");
		totalCol.setCellValueFactory(new PropertyValueFactory<>("totalPayment"));

		TableColumn payCol = new TableColumn("");
		payCol.setCellValueFactory(new PropertyValueFactory<>("payBtn"));

		loanPaymentTable.getColumns().addAll(idCol, yearCol, monthCol, percentCol, interestCol, periodPaymentCol,
				totalCol, payCol);
	}

	public void handleExportAction(ActionEvent event) {
		try {
			File file = new File("src/resources/output.csv");
			if (file.createNewFile()) {
				System.out.println("Created file");
			} else {
				System.out.println("Exists");
			}
			FileWriter myWriter = new FileWriter("src/resources/output.csv");
			myWriter.write(convertToCSV(payments));
			myWriter.close(); // must close manually

		} catch (IOException e) {
			System.out.println("Error occured: " + e.getMessage());
		}
	}

	private String convertToCSV(List<Payment> dataList) {
		if (dataList.isEmpty()) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		// Headers
		sb.append("id,").append("year,").append("month,").append("percent,").append("interest,")
				.append("periodPayment,").append("totalPayment").append("\n");
		// Data
		dataList.forEach(payment -> sb.append(payment + "\n"));

		return sb.toString();
	}

	public void handleGraphAction(ActionEvent event) {
		// Just show both calculations for payments in same graph
		MortgageCalculator otherCalculator;
		if (calculator.getClass() == AnnuityCalculator.class) {
			calculator = new LinearCalculator(loanAmountInput, loanYearlyRateInput, loanTermYearInput,
					loanTermMonthsInput);
		} else if (calculator.getClass() == LinearCalculator.class) {
			calculator = new AnnuityCalculator(loanAmountInput, loanYearlyRateInput, loanTermYearInput,
					loanTermMonthsInput);
		}

		List<Payment> otherPayments = calculator.calculatePayments();
		LineChart lc = getGraphLineChart(List.of(payments, otherPayments));

		System.out.println(calculator.getClass());
	}

	private LineChart getGraphLineChart(List<List<Payment>> dataList) {
		// https://www.tutorialspoint.com/javafx/line_chart.htm
		// Defining the y axis
		NumberAxis xAxis = new NumberAxis(1, loanTermYearInput * 12 + loanTermMonthsInput, 12);
		xAxis.setLabel("Months");

		// Defining the y axis
		int upperBoundForPayments = 0;

		for (int i = 0; i < dataList.getFirst().size(); i++) {
			Payment payment = dataList.getFirst().get(i);
			if (MyUtils.roundUpToNextNiceNumber(payment.getTotalPayment()) > upperBoundForPayments) {
				upperBoundForPayments = MyUtils.roundUpToNextNiceNumber(payment.getTotalPayment());
				System.out.println();
			}
		}

		for (int i = 0; i < dataList.get(1).size(); i++) {
			Payment payment = dataList.get(1).get(i);
			if (MyUtils.roundUpToNextNiceNumber(payment.getTotalPayment()) > upperBoundForPayments) {
				upperBoundForPayments = MyUtils.roundUpToNextNiceNumber(payment.getTotalPayment());
			}
		}

		int step = MyUtils.getBase(upperBoundForPayments);
		NumberAxis yAxis = new NumberAxis(0, upperBoundForPayments, step);
		yAxis.setLabel("Total payment");

		LineChart lc = new LineChart(xAxis, yAxis);

		return lc;
	}

	public void handleDeferAction(ActionEvent event) {
		System.out.println("Defer");
	}

}
