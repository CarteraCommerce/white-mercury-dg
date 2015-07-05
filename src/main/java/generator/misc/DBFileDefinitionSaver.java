/*
 * dgMaster: A versatile, open source data generator.
 *(c) 2007 M. Michalakopoulos, mmichalak@gmail.com
 */


package generator.misc;

import generator.db.DBGeneratorDefinition;
import generator.db.DBFieldGenerator;
import generator.db.DBTableGenerator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
 * Saves a database definition object in a user defined xml file
 * When a collection of new classes needs to be saved in an XML file, 
 * a similar class will have to be provided and the saveData method 
 * will have to be called. It is hard to convert these classes into some kind 
 * of design pattern, because each class will actually provide its own XML structure.
 */
public class DBFileDefinitionSaver
{
    private Logger logger = Logger.getLogger(DBFileDefinitionSaver.class);
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
    
    public void saveData( Vector<DBFileDefinition> vDBFileDefinitions)
    {
        //[*] maybe i can get the root element out of the dom?
        xmlSaver = new XMLSaver();
        dom = xmlSaver.createDocument("database-definitions");
        root = dom.getDocumentElement();        
        
        for(DBFileDefinition dbDef: vDBFileDefinitions)
        {
             addDBConfigFile(dbDef);  
        }
                     
        xmlSaver.writeXMLContent(dom, "DBFileDefinitions.xml" );        
    }
    

    
    private Element addDBConfigFile(DBFileDefinition dbDef)
    {
        logger.debug("db-config-file: start");
        
        Element elemDB = dom.createElement("data-output-definition");
        root.appendChild(elemDB);

        Element elemDBScenario = dom.createElement("scenario");
        elemDBScenario.setAttribute("value", dbDef.getScenario());
        elemDB.appendChild(elemDBScenario);
        
        Element elemDescription = dom.createElement("description");
        elemDescription.setTextContent(dbDef.getDescription());
        elemDB.appendChild(elemDescription);
        
        Element elemScenarioFile = dom.createElement("scenarioFile");
        elemScenarioFile.setAttribute("value", dbDef.getLinkFileDB());
        elemDB.appendChild(elemScenarioFile);        
        
        logger.debug("db-config: done");
        return elemDB;
    }
}
