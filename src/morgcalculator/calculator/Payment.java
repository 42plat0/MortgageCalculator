package morgcalculator.calculator;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

public class Payment {
	private Integer id;
	private Integer year;
	private Integer month;
	private Float percent;
	private Float interest;
	private Float periodPayment;
	private Float totalPayment;

	public Button payBtn;
	public TableView parentContainer;
	private Integer placeInMonth; // used for graph

	public Payment(Integer id, Integer year, Integer month, Float percent, Float interest, Float periodPayment,
			Float totalPayment) {
		this.id = id;
		this.year = year;
		this.month = month;
		this.percent = percent;
		this.interest = interest;
		this.periodPayment = periodPayment;
		this.totalPayment = totalPayment;
		this.payBtn = new Button("payBtn");
		this.payBtn.setOnAction(getOnAction());
		this.payBtn.setText("Mokėti");
	}

	// Last row in table
	public Payment(Float totalPayment) {
		this.totalPayment = totalPayment;
	}

	public Integer getId() {
		return id;
	}

	public Integer getYear() {
		return year;
	}

	public Integer getMonth() {
		return month;
	}

	public Float getPercent() {
		return percent;
	}

	public Float getInterest() {
		return interest;
	}

	public Float getPeriodPayment() {
		return periodPayment;
	}

	public Float getTotalPayment() {
		return totalPayment;
	}

	public void setTotalPayment(Float totalPayment) {
		this.totalPayment = totalPayment;
	}

	public Button getPayBtn() {
		return payBtn;
	}

	public void setParentContainer(TableView parentContainer) {
		this.parentContainer = parentContainer;
	}

	public Integer getPlaceInMonth() {
		return placeInMonth;
	}

	public void setPlaceInMonth(int placeInMonth) {
		this.placeInMonth = placeInMonth;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(id == null ? "" : id).append(",");
		sb.append(year == null ? "" : year).append(",");
		sb.append(month == null ? "" : month).append(",");
		sb.append(percent == null ? "" : percent).append(",");
		sb.append(interest == null ? "" : interest).append(",");
		sb.append(periodPayment == null ? "" : periodPayment).append(",");
		sb.append(totalPayment == null ? "" : totalPayment);
		return sb.toString();
	}

	public XYChart.Data getChartData() {
		if (placeInMonth != null) {
			return new XYChart.Data(placeInMonth, totalPayment);
		}
		return null;
	}

	public EventHandler<ActionEvent> getOnAction() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				payBtn.setDisable(true);
				payBtn.setText("Apmokėta");
				if (parentContainer != null) {
					// Handle payments
					List<Payment> payments = parentContainer.getItems();
					Payment last = payments.getLast();

					List<Payment> newPayments = new ArrayList<>();
					last.setTotalPayment(last.getTotalPayment() - totalPayment);
					for (int i = 0; i < payments.size() - 1; i++) {
						newPayments.add(payments.get(i));
					}
					newPayments.add(last);
					parentContainer.getItems().clear();
					parentContainer.getItems().addAll(newPayments);
					parentContainer.refresh();

				}
			}
		};
	}
}
