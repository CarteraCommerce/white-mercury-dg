/*
 * dgMaster: A versatile, open source data generator.
 *(c) 2007 M. Michalakopoulos, mmichalak@gmail.com
 */


package generator.misc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import generator.extenders.RandomiserInstance;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.apache.log4j.Logger;

/**
 *
 * @author Michael
 * Saves a data file definitions vector in TextFileDefinitions.xml
 * When a collection of new classes needs to be saved in an XML file, 
 * a similar class will have to be provided and the saveData method 
 * will have to be called. It is hard to convert these classes into some kind 
 * of design pattern, because each class will actually provide its own XML structure.
 */
public class FileOutDataSaver
{
    private Logger logger = Logger.getLogger(FileOutDataSaver.class);
    private XMLSaver xmlSaver;
    private Document dom=null;
    private Element  root;
    
    
    /** 
     * Saves the data to the XML file. This is the method that the caller will
     * actually call to save the DataDefinition data. When a collection of new
     * classes needs to be saved in an XML file, a similar class will have to be
     * provided and the saveData method will have to be called. It is hard to 
     * convert these classes into some kind of design pattern, becase each 
     * class will actually provide its own XML structure.
     *
     * <p> Preconditions: Vector contains valid DataFileDefinition objects
     * <p> Post-effects: Contents of vector are saved in TextFileDefinitions.xml
     *                   CAUTION: Any actual data existing in file will be overwritten!!!
     *
     * @param vData a Vector of DataDefinition objects to be saved in file TextFileDefinitions.xml
     */
    public void saveData(Vector<DataFileDefinition> vData)
    {
        //[*] maybe i can get the root element out of the dom?
        xmlSaver = new XMLSaver();
        dom = xmlSaver.createDocument("datafile-definitions");
        root = dom.getDocumentElement();
        
        //all data will be linked here
        Element elemDataFileDefinition;
        
        //each dataFileDefinition has a vector linked to it. The vector has
        //DataFileItem objects
        DataFileDefinition dataFileDefinition;
        Vector<DataFileItem> vDataFileItems;
        DataFileItem dataItem;
                
        for(int i=0; i<vData.size(); i++)
        {
            dataFileDefinition     = vData.elementAt(i);
            elemDataFileDefinition = addElementDFD(dataFileDefinition);
            addElementDFDDescription(elemDataFileDefinition, dataFileDefinition);
            vDataFileItems = dataFileDefinition.getOutDataItems();
            for(int j=0; j<vDataFileItems.size(); j++)
            {
                dataItem = vDataFileItems.elementAt(j);
                addDataItem(elemDataFileDefinition,dataItem);
            }
        }        
        xmlSaver.writeXMLContent(dom, "TextFileDefinitions.xml");
    }
    

    private Element addElementDFDDescription(Element elemDFD, DataFileDefinition dfd)
    {
        Element description = dom.createElement("description");
        description.setTextContent(dfd.getDescription());
        elemDFD.appendChild(description);
        logger.debug("description: "+dfd.getDescription());
        return elemDFD;
    }    
    
    private Element addElementDFD(DataFileDefinition dfd)
    {
        Element elemDFD = dom.createElement("File-output-definition");
        elemDFD.setAttribute("name",dfd.getName());
        elemDFD.setAttribute("filename",dfd.getOutFilename());
        elemDFD.setAttribute("delimiter",dfd.getDelimiter());
        elemDFD.setAttribute("numOfRecs",""+dfd.getNumOfRecs());
        root.appendChild(elemDFD);
        logger.debug("File-output-definition: "+dfd.getName());
        return elemDFD;
    }

    
    private void addDataItem(Element elemDFD, DataFileItem dataItem)
    {
        Element elemDataItem = dom.createElement("data-item");
        elemDataItem.setAttribute("randomiser-instance",dataItem.getRandomiserInstanceName());
        elemDataItem.setAttribute("width",""+dataItem.getWidth());
        elemDataItem.setAttribute("encloseChar",dataItem.getEncloseChar());
        elemDataItem.setAttribute("alignment",""+dataItem.getAlignment());
        
        elemDFD.appendChild(elemDataItem);
        logger.debug("added a data-item element");        
    }    
    
}
