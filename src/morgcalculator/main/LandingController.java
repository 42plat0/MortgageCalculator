package morgcalculator.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

	@FXML
	private void handleCalculateAction(ActionEvent event) {
		Float loanAmount = null;
		Integer loanTermYear = null;
		Integer loanTermMonths = null;
		Float loanYearlyRate = null;
		int loanSchedule = loanScheduleCombo.getSelectionModel().getSelectedIndex();
		try {
			loanAmount = Float.valueOf(loanAmountField.getText());
			loanYearlyRate = Float.valueOf(loanYearlyRateField.getText());
			loanTermYear = Integer.valueOf(loanTermYearField.getText());
			loanTermMonths = Integer.valueOf(loanTermMonthsField.getText());
		} catch (NumberFormatException e) {
			errorText.setFill(Color.FIREBRICK);
			errorText.setText("Įvestis gali būti tik skaičiai!");
			if (loanAmount == null) {
				loanAmountField.setText("");
			}
			if (loanTermYear == null) {
				loanTermYearField.setText("");
			}
			if (loanTermMonths == null) {
				loanTermMonthsField.setText("");
			}
			if (loanYearlyRate == null) {
				loanYearlyRateField.setText("");
			}
			loanAmount = 10000f;
			loanYearlyRate = 5f;
			loanTermYear = 1;
			loanTermMonths = 0;

//			return;
		}

		if (!errorText.getText().isEmpty()) {
			errorText.setText("");
		}

		exportBtn.setDisable(false);
		deferBtn.setDisable(false);
		graphBtn.setDisable(false);

		MortgageCalculator.Schedule schedule = MortgageCalculator.getSchedule(loanSchedule);

		if (MortgageCalculator.Schedule.ANNUITY == schedule) {
			calculator = new AnnuityCalculator(loanAmount, loanYearlyRate, loanTermYear, loanTermMonths);
		} else if (MortgageCalculator.Schedule.LINEAR == schedule) {
			calculator = new LinearCalculator(loanAmount, loanYearlyRate, loanTermYear, loanTermMonths);
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
		System.out.println("Graph");
	}

	public void handleDeferAction(ActionEvent event) {
		System.out.println("Defer");
	}

}
