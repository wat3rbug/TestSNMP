/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testsnmp;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author douglas
 */
public class HostDetailsListener extends MouseAdapter {
    
    private HostDetails details;
    private boolean isVisible;
    
    public HostDetailsListener(SNMPHost host) {
        details = new HostDetails(host);
        isVisible = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        details.setVisible((isVisible = !isVisible));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // not used
    }

    @Override
    public void mouseReleased(MouseEvent e) {
         // not used
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //
    }
}
