package morgcalculator.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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
	private DatePicker dateFrom;
	@FXML
	private DatePicker dateTo;
	@FXML
	private Button filterBtn;
	@FXML
	private Button exportBtn;
	@FXML
	private Button deferBtn;
	@FXML
	private Button graphBtn;
	@FXML
	private Button rmFilterBtn;

	public MortgageCalculator calculator;
	private List<Payment> payments;
	private List<Payment> filteredPayments;

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
		filterBtn.setDisable(false);

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
			myWriter.write(MyUtils.convertToCSV(payments));
			myWriter.close(); // must close manually

		} catch (IOException e) {
			System.out.println("Error occured: " + e.getMessage());
		}
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
		LineChart lc = getGraphLineChart(payments, otherPayments, calculator.getClass());

		Stage stage = new Stage();
		Group root = new Group(lc);
		Scene scene = new Scene(root, 900, 500);
		stage.setScene(scene);
		stage.setTitle("Graph of schedules");
		stage.show();
	}

	private LineChart getGraphLineChart(List<Payment> dataList, List<Payment> dataList1, Class dataList1Class) {
		// https://www.tutorialspoint.com/javafx/line_chart.htm
		// Defining the y axis
		NumberAxis xAxis = new NumberAxis(0, loanTermYearInput * 12 + loanTermMonthsInput, 12);
		xAxis.setLabel("Months");
		xAxis.setMinorTickVisible(true);
		xAxis.setMinorTickCount(5);
		xAxis.setTickLabelsVisible(true);

		int upperBoundForPayments = 0;
		for (int i = 0; i < dataList.size(); i++) {
			Payment payment = dataList.get(i);
			if (MyUtils.roundUpToNextNiceNumber(payment.getTotalPayment()) > upperBoundForPayments) {
				upperBoundForPayments = MyUtils.roundUpToNextNiceNumber(payment.getTotalPayment());
			}
			payment.setPlaceInMonth(i);
		}

		for (int i = 0; i < dataList1.size(); i++) {
			Payment payment = dataList1.get(i);
			if (MyUtils.roundUpToNextNiceNumber(payment.getTotalPayment()) > upperBoundForPayments) {
				upperBoundForPayments = MyUtils.roundUpToNextNiceNumber(payment.getTotalPayment());
			}
			payment.setPlaceInMonth(i);
		}

		// Defining the y axis
		int step = MyUtils.getBase(upperBoundForPayments);
		NumberAxis yAxis = new NumberAxis(0, upperBoundForPayments, step);
		yAxis.setLabel("Total payment");
		yAxis.setMinorTickVisible(true);
		yAxis.setMinorTickCount(5);
		yAxis.setTickLabelsVisible(true);

		xAxis.setAutoRanging(true);
		yAxis.setAutoRanging(true);
		LineChart<?, ?> lc = new LineChart(xAxis, yAxis);

		XYChart.Series annuitySeries = new XYChart.Series();
		annuitySeries.setName("Annuity schedule");
		XYChart.Series linearSeries = new XYChart.Series();
		linearSeries.setName("Linear schedule");

		if (dataList1Class == AnnuityCalculator.class) {
			dataList1.forEach(p -> annuitySeries.getData().add(p.getChartData()));
			dataList.forEach(p -> linearSeries.getData().add(p.getChartData()));
		} else {
			dataList.forEach(p -> annuitySeries.getData().add(p.getChartData()));
			dataList1.forEach(p -> linearSeries.getData().add(p.getChartData()));
		}

		lc.getData().addAll(annuitySeries, linearSeries);
		lc.setCreateSymbols(true); // Enable data point symbols
		lc.setPrefSize(800, 400);
		return lc;
	}

	public void handleDeferAction(ActionEvent event) {
		System.out.println("Defer");
	}

	public void handleFilterAction(ActionEvent event) {
		rmFilterBtn.setDisable(false);
		if (filteredPayments == null) {
			filteredPayments = new ArrayList<Payment>();
		} else if (!filteredPayments.isEmpty()) {
			filteredPayments.clear();
		}

		if (!loanPaymentTable.getItems().isEmpty()) {
			loanPaymentTable.getItems().clear();
		}

		LocalDate from = dateFrom.getValue();
		LocalDate to = dateTo.getValue();

		filteredPayments = payments.stream().filter(p -> p.isInDateRange(from, to)).collect(Collectors.toList());

		Payment lastRowInfo = new Payment(0f);
		for (Payment payment : filteredPayments) {
			payment.setParentContainer(loanPaymentTable);
			lastRowInfo.setTotalPayment(lastRowInfo.getTotalPayment() + payment.getTotalPayment());
		}
		loanPaymentTable.getItems().addAll(filteredPayments);
		loanPaymentTable.getItems().add(lastRowInfo);

	}

	public void handleRmFilterAction(ActionEvent event) {
		if (!loanPaymentTable.getItems().isEmpty()) {
			loanPaymentTable.getItems().clear();
		}

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
		rmFilterBtn.setDisable(true);

	}
}
