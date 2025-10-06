package morgcalculator.ui;

import javafx.scene.control.Button;

public class ExportButton extends Button {

	public ExportButton() {
		super("Export");
		setOnAction(e -> {
			System.out.println("Exported");
		});
	}

}
