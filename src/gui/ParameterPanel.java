package gui;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;

public abstract class ParameterPanel extends JPanel {
    private final JTextField inputField;

    public ParameterPanel(String label) {
        super(new GridLayout(1, 2));
        add(new JLabel(label));
        inputField = new JTextField();
        ((PlainDocument) inputField.getDocument()).setDocumentFilter(new IntegerFilter());
        add(inputField);
        autoAssign();
    }

    public int getValue() {
        String value = inputField.getText();
        if (value.isEmpty()) {
            return 0;
        } else {
            return Integer.parseInt(value);
        }
    }

    public void setValue(int value) {
        inputField.setText(Integer.toString(value));
    }

    public abstract void assignValue();

    public abstract void autoAssign();
}
