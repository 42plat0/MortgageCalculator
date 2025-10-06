package morgcalculator.ui;

import javafx.scene.control.Button;

public class GraphButton extends Button {

	public GraphButton() {
		super("Graph");
		setOnAction(e -> {
			System.out.println("Graph");
		});
	}

}
