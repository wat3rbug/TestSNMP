package testsnmp;

/**
 * This simple class stores the service name and the associated OID.  I have not
 * done research on it yet, but I suspect that is what I will need to display 
 * it.
 * 
 * @author Douglas Gardiner
 */
public class Service {
    
    public String serviceName;
    public String serviceOID;
    
    public Service (String name, String oid) {
        serviceName = name;
        serviceOID = oid;
    }
}
