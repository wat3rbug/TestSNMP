/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testsnmp;

import java.awt.Color;
import javax.swing.JLabel;

/**
 *
 * @author douglas
 */
public class LanUtilWarning extends IntWarningLevels {
    
    public LanUtilWarning(int off, int warning, int danger) {
        super(off, warning, danger);
    }
    
    @Override
    public void updateWarning(JLabel label, int value) {
                if (value >= danger) {
            label.setBackground(Color.red);
            label.setForeground(Color.black);
        }
        if (value > warning && value <= danger) {
            label.setBackground(Color.yellow);
            label.setForeground(Color.black);
        }
        if (value <= warning && value >= off) {
            label.setBackground(null);
            label.setForeground(null);
        }
    }
}
