/**
 * 
 */

package com.brc.whitemercurydg.generator.misc;


import java.util.Vector;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import generator.misc.Constants;
import generator.misc.SAXDataHandler;


public class JSONDataFileOutputSAXHandler extends SAXDataHandler {
    private Logger logger = Logger.getLogger(JSONDataFileOutputSAXHandler.class);
    private Vector<JSONDataFileDefinition> vDFDs;
    private Vector<JSONDataFileItem> vDataItems;
    private JSONDataFileDefinition dataFileDefinition;
    private final int DESCRIPTION = 1;
    private final int NONE = -1;
    private int parsedElement=NONE;
    
    public JSONDataFileOutputSAXHandler() {
        vDFDs = new Vector<JSONDataFileDefinition>();
    }
    
    public void startDocument() throws SAXException {
        logger.debug("Document parsing started");
    }
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        String attribValue = null;
        
        logger.debug("Element found:"+qName);
        
        if (qName.equalsIgnoreCase("file-output-definition")) {
            dataFileDefinition = new JSONDataFileDefinition();
            attribValue = attributes.getValue("delimiter");
            dataFileDefinition.setDelimiter(attribValue);
            attribValue = attributes.getValue("filename");
            dataFileDefinition.setOutFilename(attribValue);
            
            try {
                attribValue = attributes.getValue("numOfRecs");
                dataFileDefinition.setNumOfRecs(Integer.parseInt(attribValue));
            } catch(Exception e) {
                logger.error("Error during integer conversion for numOfRecs:"+attribValue);
                dataFileDefinition.setNumOfRecs(0);
            }
            
            attribValue = attributes.getValue("name");
            dataFileDefinition.setName(attribValue);
            
            logger.debug("file-output-definition attributes:"+ dataFileDefinition.getName() + ", "
                    + dataFileDefinition.getOutFilename() + ", "
                    + dataFileDefinition.getDelimiter() + ", "
                    + dataFileDefinition.getNumOfRecs() );
        }
        
        if (qName.equalsIgnoreCase("description")) {
            parsedElement=DESCRIPTION;
        }        

        if (qName.equalsIgnoreCase("data-item")) {
            if (vDataItems==null) {
                vDataItems = new Vector<JSONDataFileItem>();
            }
            
            JSONDataFileItem dataItem = new JSONDataFileItem();
            
            attribValue = attributes.getValue("name");
            dataItem.setName(attribValue);
            attribValue = attributes.getValue("type");
            dataItem.setType(attribValue);

            try {
                attribValue = attributes.getValue("alignment");
                dataItem.setAlignment(Integer.parseInt(attribValue));
            } catch(Exception e) {
                logger.error("Error during integer conversion for alignment:"+attribValue);
                dataItem.setAlignment(Constants.ALIGN_LEFT);
            }
            
            attribValue = attributes.getValue("encloseChar");
            if (attribValue.length() > 0) {
                dataItem.setEncloseChar(attribValue.substring(0,1));
            } else {
                dataItem.setEncloseChar("");
            }
            attribValue = attributes.getValue("randomiser-instance");
            dataItem.setRandomizerInstanceName(attribValue);
            
            try {
                attribValue = attributes.getValue("width");
                dataItem.setWidth(Integer.parseInt(attribValue));
            } catch (Exception e) {
                logger.error("Error during integer conversion for width:"+attribValue);
                dataItem.setAlignment(Constants.ALIGN_LEFT);
            }

            vDataItems.add(dataItem);
        }
    }
    
    public void characters(char characters[], int start, int length) {
        String chData = (new String(characters, start, length)).trim();
        
        if (parsedElement==DESCRIPTION) {
            dataFileDefinition.setDescription(chData);
            parsedElement=NONE;
        }
    }
    
    public void endElement(String uri, String localName, String qName) throws SAXException {
        logger.debug("Element ending:"+qName);
        
        if (qName.equalsIgnoreCase("file-output-definition")) {
            //link the vector to the DFD, and reset it.
            dataFileDefinition.setOutDataItems(vDataItems);
            vDataItems=null;
            vDFDs.add(dataFileDefinition);
            logger.debug("Added one element, size is:"+vDFDs.size());
        }
    }
    
    public Vector<JSONDataFileDefinition> getData() {
        logger.debug("Returning vector, size is:"+vDFDs.size());
        return vDFDs;
    }
}
