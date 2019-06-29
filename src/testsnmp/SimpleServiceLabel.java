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
public class SimpleServiceLabel extends JLabel {
    
    private Service service;
    
    public SimpleServiceLabel(Service srv) {
        super(srv.serviceName);
        setText("Unknown");
        setForeground(Color.white);
        setBackground(Color.blue);
        setOpaque(true);
        service = srv;
    }
    
    public Service getService() {
        return service;
    }
    public String getOIDName() {
        return service.serviceOID;
    }
    
    public void updateServiceStatus(int value) {
        if (value >0) {
            this.setOpaque(true);
            this.setBackground(Color.green);
            this.setForeground(Color.black);
            this.setText("Running");
        } else {
            this.setOpaque(true);
            this.setBackground(Color.red);
            this.setForeground(Color.black);
            this.setText("Stopped");
        }
    }
}
