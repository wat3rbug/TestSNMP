package testsnmp;

/**
 * This simple class stores the service name and the associated OID.  I use it 
 * for special cases since many of the Raspberry Pis to poll do not have the 
 * same services.  Some have database apps running and some have web services, 
 * etc.
 * 
 * @author Douglas Gardiner
 */

public class Service {
    
    public String serviceName;
    public String serviceOID;
    
    /**
     * This constructor is used for the name of the service and the OID used to 
     * get the status of the service.
     * 
     * @param name The name such as 'httpd' or something.
     * @param oid The OID number such as .1.3.6.1.2.1.8.1 or something.
     */
    
    public Service (String name, String oid) {
        serviceName = name;
        serviceOID = oid;
    }
}
