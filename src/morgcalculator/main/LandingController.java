package morgcalculator.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

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

	}

	@SuppressWarnings("unchecked")
	@FXML
	public void initialize() {
		loanScheduleCombo.getItems().removeAll(loanScheduleCombo.getItems());
		loanScheduleCombo.getItems().addAll("Anuiteto", "Linijinis");
	}
}
