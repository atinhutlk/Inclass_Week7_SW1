import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CalculatorController {

    @FXML private TextField number1Field;
    @FXML private TextField number2Field;
    @FXML private Label resultLabel;

    @FXML
    private void onCalculateClick() {
        try {
            double num1 = Double.parseDouble(number1Field.getText());
            double num2 = Double.parseDouble(number2Field.getText());

            double sum = num1 + num2;
            double difference = num1 - num2;
            double product = num1 * num2;

            Double division = null;
            if (num2 != 0) {
                division = num1 / num2;
            }

            String divisionText = (division == null) ? "undefined (division by zero)" : String.valueOf(division);
            resultLabel.setText("Sum: " + sum
                    + ", Difference: " + difference
                    + ", Product: " + product
                    + ", Division: " + divisionText);

            // Save to DB
            ResultService.saveResult(num1, num2, sum, difference, product, division);

        } catch (NumberFormatException e) {
            resultLabel.setText("Please enter valid numbers!");
        }
    }
}