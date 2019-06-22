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
    
    public static ArrayList<SNMPHost> BuildHostArray(String hostfile) 
            throws IOException, ParserConfigurationException, SAXException {
        
        ArrayList<SNMPHost> listing = new ArrayList<>();

        File hostFile = new File(hostfile);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
        Document doc = dbBuilder.parse(hostFile);
        doc.getDocumentElement().normalize();
        String root = doc.getDocumentElement().getNodeName();
        NodeList nList = doc.getElementsByTagName("host");
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                SNMPHost temp = new SNMPHost();
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
                NodeList tempSrv =  eElement.getElementsByTagName("services");
                getService(tempSrv, temp);
                listing.add(temp);

            }
        }
        return listing;        
    }
    
    private static void getService(NodeList node, SNMPHost host) {

        for (int i = 0; i < node.getLength(); i++) {
            Node temp = node.item(i);
            if (temp.getNodeType() == Node.ELEMENT_NODE) {
                Service tempSrv = new Service();
                host.addService(tempSrv);
            }

        }
        
    }
}
