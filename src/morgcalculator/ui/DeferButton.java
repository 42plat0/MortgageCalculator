package morgcalculator.ui;

import javafx.scene.control.Button;

public class DeferButton extends Button {

	public DeferButton() {
		super("Defer");
		setOnAction(e -> {
			System.out.println("Deferred");
		});
	}

}
