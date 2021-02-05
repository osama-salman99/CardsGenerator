package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ParametersPanel extends JPanel {
    ArrayList<ParameterPanel> parameterPanels;

    public ParametersPanel() {
        super(new GridLayout(0, 1));
        parameterPanels = new ArrayList<>();
    }

    public void add(ParameterPanel parameterPanel) {
        super.add(parameterPanel);
        parameterPanels.add(parameterPanel);
    }

    public void autoAssignAll() {
        for (ParameterPanel parameterPanel : parameterPanels) {
            parameterPanel.autoAssign();
        }
    }

    public void assignValues() {
        for (ParameterPanel parameterPanel : parameterPanels) {
            parameterPanel.assignValue();
        }
    }
}
