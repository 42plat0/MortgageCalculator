package morgcalculator.main;

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
	private void handleCalculateAction(ActionEvent event) {
		Float loanAmount = null;
		Integer loanTermYear = null;
		Integer loanTermMonths = null;
		Float loanYearlyRate = null;
		Integer loanSchedule = loanScheduleCombo.getSelectionModel().getSelectedIndex();
		try {
			loanAmount = Float.valueOf(loanAmountField.getText());
			loanTermYear = Integer.valueOf(loanTermYearField.getText());
			loanTermMonths = Integer.valueOf(loanTermMonthsField.getText());
			loanYearlyRate = Float.valueOf(loanYearlyRateField.getText());
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
			return;
		}

		if (!errorText.getText().isEmpty()) {
			errorText.setText("");
		}

		Payment p = new Payment(1, 2025, 12, 0.6f, 123.3f, 200f, 300f);

		loanPaymentTable.getItems().add(p);
	}

	@SuppressWarnings("unchecked")
	@FXML
	public void initialize() {
		// ComboBox
		loanScheduleCombo.getItems().removeAll(loanScheduleCombo.getItems());
		loanScheduleCombo.getItems().addAll("Anuiteto", "Linijinis");

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

		TableColumn paymentCol = new TableColumn("Įmoka");
		paymentCol.setCellValueFactory(new PropertyValueFactory<>("payment"));

		TableColumn totalCol = new TableColumn("Viso");
		totalCol.setCellValueFactory(new PropertyValueFactory<>("totalPayment"));

		loanPaymentTable.getColumns().addAll(idCol, yearCol, monthCol, percentCol, interestCol, paymentCol, totalCol);
	}
}
