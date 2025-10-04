module mortgage_calculator {
	requires javafx.controls;
	requires javafx.fxml;
	
	opens morgcalculator.main to javafx.graphics, javafx.fxml;
}
