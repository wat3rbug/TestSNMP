package testsnmp;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * This class reads the XML file and builds all of the information needed by the
 * SNMPHosts so that polling can begin on them.  It does all of the XML parsing,
 * so if there is an error, this mess is where you go.  Yes, it is convoluted 
 * and Yes, the parsing is brittle.  Take care.
 * 
 * @author Douglas Gardiner
 */

public class SNMPHostFactory {
    
    /**
     * The reads an xml file, if one is given and returns an array of SNMPHOSTs
     * based.  It's a simple factory method for giving SNMPHOSTS based on that 
     * file.  I have some debate on whether to move the exception handling to 
     * this method or not, since the application should just quit if the file
     * is not to the standards for XML.  If I move it here, I have to deal with
     * nulls at the application and complicate this portion.  It's already 
     * complicated.
     * 
     * @param hostfile . The XML file to parse for hosts.
     * @return A list of hosts to be used by the application.
     * @throws IOException A default exception when the others don't happen.
     * @throws ParserConfigurationException Because the file can't be parsed.
     * @throws SAXException Because the XML parsing error is more specific.
     */
    
    public static ArrayList<SNMPHost> BuildHostArray(String hostfile) 
            throws IOException, ParserConfigurationException, SAXException {
        
        ArrayList<SNMPHost> listing = new ArrayList<>();

        File hostFile = new File(hostfile);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
        Document doc = dbBuilder.parse(hostFile);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("host");
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            SNMPHost temp = new SNMPHost();
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {            
                Element eElement = (Element)nNode;
                temp.function = eElement.getElementsByTagName("function")
                        .item(0).getTextContent();
                temp.Hostname = eElement.getAttribute("name");
                temp.fqdn = eElement.getElementsByTagName("fqdn").item(0)
                        .getTextContent();
                temp.communityString = eElement
                        .getElementsByTagName("rocommunity").item(0)
                        .getTextContent();
		temp.color = eElement.getElementsByTagName("color").item(0)
			.getTextContent();
                Node srvNode = eElement.getElementsByTagName("services").item(0);
                if (srvNode != null) {
                    getServices(srvNode, temp);
                }
            }
            listing.add(temp);
        }
        return listing;        
    }
    
    private static void getServices(Node basenode, SNMPHost host) {

        Element elem = (Element)basenode;
        int maxNodes = elem.getElementsByTagName("name").getLength();
        for (int i = 0; i < maxNodes; i++) {
            Node firstName = elem.getElementsByTagName("name").item(i);
            Node firstOid = elem.getElementsByTagName("oid").item(i);
            if (firstName != null && firstOid != null) {
                String name = firstName.getTextContent();
                String oid = firstOid.getTextContent();
                Service srv = new Service(name, oid);
                host.addService(srv);
            }
        }    
    }
}
