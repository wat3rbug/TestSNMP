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
public class IntWarningLevels implements IWarning{
    
    protected int off;
    protected int warning;
    protected int danger;
    
    public IntWarningLevels(int off, int warning, int danger) {
    
        this.off = off;
        this.warning = warning;
        this.danger = danger;
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
        if (value <= warning && value > off) {
            label.setBackground(null);
            label.setForeground(null);
        }
        if (value == off) {
            label.setBackground(Color.blue);
            label.setForeground(Color.white);
        }
    }
}
