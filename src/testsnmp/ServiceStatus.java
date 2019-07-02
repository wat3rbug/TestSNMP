package testsnmp;

/**
 * This class keeps the boolean value for the service.  At this time, I am not 
 * sure if this is used and the class may be deleted.
 * @author douglas
 */
public class ServiceStatus {
    
    private String name;
    private boolean status;
    
    /**
     * This constructor is used for storing a boolean value and the name of the
     * service.
     * 
     * @param name The name of the service such as 'httpd'.
     * @param status The status of the service whether 'true' for running, or 
     * 'false' because it is not running.
     */
    
    public ServiceStatus(String name, boolean status) {
        this.name = name;
        this.status = status;
    }
}
