/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testsnmp;

/**
 *
 * @author douglas
 */
public class ServiceStatus {
    
    private String name;
    private boolean status;
    
    public ServiceStatus(String name, boolean status) {
        this.name = name;
        this.status = status;
    }
}
