/**
 * 
 */
package com.brc.whitemercurydg.generator.engine.json;


import generator.engine.ProgressUpdateObservable;
import generator.engine.ProgressUpdateObserver;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import generator.misc.Constants;
import generator.misc.RandomiserType;
import generator.misc.Utils;
import org.apache.log4j.Logger;

import com.brc.whitemercurydg.generator.misc.JSONDataFileDefinition;
import com.brc.whitemercurydg.generator.misc.JSONDataFileItem;

import generator.extenders.IRandomiserFunctionality;
import generator.extenders.RandomiserInstance;


public class Generator implements ProgressUpdateObservable {
    Vector<RandomiserType> vRandomiserTypes;
    Vector<RandomiserInstance> vRandomiserInstances;
    Vector<JSONDataFileDefinition> vDataFileDefinitions;
    JSONDataFileDefinition dataFileDefinition;
    
    Logger logger = Logger.getLogger(Generator.class);
    
    ProgressUpdateObserver observer;
    
    public void setEngineData(Vector<RandomiserType> vRT, Vector<RandomiserInstance> vRI, Vector<JSONDataFileDefinition> vDFD) {
        this.vRandomiserTypes = vRT;
        this.vRandomiserInstances = vRI;
        this.vDataFileDefinitions = vDFD;
    }
    
    public void setFileDefinitionOutput(JSONDataFileDefinition dfd) {
        this.dataFileDefinition = dfd;
    }
    
    public boolean setFileDefinitionOutput(String fileDefinition) {
        boolean found=false;
        JSONDataFileDefinition dfd;
        
        logger.debug("Searching definition: " + fileDefinition);
        
        for (int i=0; i<vDataFileDefinitions.size(); i++) {
            dfd = vDataFileDefinitions.elementAt(i);
            if (dfd.getName().equalsIgnoreCase(fileDefinition)) {
                dataFileDefinition = dfd;
                found = true;
                break;
            }
        }
        
        if(dataFileDefinition!=null) {
            logger.debug("Searching definition: Done (loaded)");
        } else {
            logger.warn("Searching definition, failed.");
        }
        
        return found;
    }
    
    private BufferedWriter createOutputFile(String filename) {
        BufferedWriter fileWriter = null;
        
        try {
            FileWriter out = new FileWriter(filename);
            fileWriter = new BufferedWriter(out);
        } catch (IOException ioe) {
            logger.error("Output file not created",ioe);
        }
        
        return fileWriter;
    }
    
    private RandomiserInstance getRandomiserInstance(String riName) {
        RandomiserInstance ri = null;
        boolean found = false;
        int i=0;
        
        logger.debug("Retrieving randomiserInstance object for:"+riName);
        while (i<vRandomiserInstances.size() && !found) {
            ri   = vRandomiserInstances.elementAt(i);
            if (ri.getName().equalsIgnoreCase(riName)) {
                found = true;
            }
            i++;
        }
        
        logger.debug("Retrieving the randomiserInstance for:"+riName + ". Found:"+found);
        return ri;
    }
    
    private RandomiserType getRandomiserType(String randomiserType) {
        RandomiserType rt = null;
        boolean found = false;
        int i=0;
        
        logger.debug("Retrieving randomiserInstance object for:"+randomiserType);
        while (i<vRandomiserTypes.size() && !found) {
            rt = vRandomiserTypes.elementAt(i);
            if (rt.getName().equalsIgnoreCase(randomiserType)) {
                found = true;
            }
            i++;
        }
        
        logger.debug("Retrieving the randomiserType for:"+randomiserType + ". Found:"+found +", class:"+rt.getName());
        return rt;
    }
    
    private String padLeft(String s, int width) {
        int pad = width - s.length();
        String temp="", retValue;
        
        for(int i=0; i<pad; i++)
            temp=temp +" ";
        retValue = temp +s;
        
        return retValue;
    }
    
    private String padRight(String s, int width) {
        int pad = width - s.length();
        String temp="", retValue;
        
        for(int i=0; i<pad; i++)
            temp=temp +" ";
        retValue = s + temp;
        
        return retValue;
    }
    
    private String padCenter(String s, int width) {
        int pad = (width - s.length())/2;
        String temp="", retValue;
        
        for(int i=0; i<pad; i++)
            temp=temp +" ";
        retValue = temp + s + temp;
        if(retValue.length()<width)
            retValue=" "+retValue;
        else if(retValue.length()>width)
            retValue=retValue.substring(0, retValue.length()-1);
        return retValue;
    }
    
    public void generate() {
        long start = System.currentTimeMillis();
        Vector<JSONDataFileItem> vDataItems;
        BufferedWriter fileWriter;
        int i;
        long numOfRecs;
        boolean error, cancelled;
        String value, outLine, riName, enclChar, name, type;
        IRandomiserFunctionality aGenerator[];
        Utils utils;
        RandomiserInstance ri;
        RandomiserType rt;
        Object objValue;
        StringBuffer sb;
        
        fileWriter = createOutputFile( dataFileDefinition.getOutFilename() );
        if (fileWriter==null) {
            logger.error("Error while creating output file:" + dataFileDefinition.getOutFilename());
            observer.datageGenError("Error while creating the output file.");
            return;
        }
        
        vDataItems = dataFileDefinition.getOutDataItems();
        aGenerator = new IRandomiserFunctionality[vDataItems.size()];
        utils = new Utils();
        
        notifyInit();
        notifyMaxProgressValue(vDataItems.size());
        try {
            for (int j=0; j<vDataItems.size(); j++) {
                JSONDataFileItem dataItem = vDataItems.elementAt(j);
                riName = dataItem.getRandomizerInstanceName();
                logger.debug("Loading class for :"+ riName);
                
                cancelled = notifyProgrssUpdate("Loading initializer for:"+ riName, j+1);
                
                ri = getRandomiserInstance(riName);
                
                rt = getRandomiserType( ri.getRandomiserType() );
                
                aGenerator[j] = (IRandomiserFunctionality) utils.createObject(rt.getGenerator());
                aGenerator[j].setRandomiserInstance(ri);
                logger.debug("Loading class for :"+ riName  +". Done");
            }
        } catch (Exception e) {
            logger.error("Error while initialising a generator",e);
            observer.datageGenError("Error while initialising a generator:"+e);
            return;
        }
        
        numOfRecs = dataFileDefinition.getNumOfRecs();        
        notifyMaxProgressValue((int)numOfRecs);
        
        i = 0;
        error = false;
        cancelled = false;
        
        try {
            fileWriter.write("[");
            fileWriter.newLine();
        } catch (IOException ioe) {
            logger.warn("Exception error while writing data", ioe);
            error=true;
        }
        
        while (i<numOfRecs && !error && !cancelled) {
            try {
                outLine="";
                sb = new StringBuffer();
                sb.append("\t{\n");
                for (int j=0; j<vDataItems.size(); j++) {
                    JSONDataFileItem dataItem = vDataItems.elementAt(j);
                    objValue = aGenerator[j].generate();
                    if (objValue==null) {
                        objValue="null";
                    }
                    
                    value = objValue.toString();
                    
                    enclChar = "\"";
                    name = dataItem.getName();
                    type = dataItem.getType();
                    if (value.length()<dataItem.getWidth()) {
                        if (dataItem.getAlignment()==Constants.ALIGN_LEFT) {
                            value = padRight(value,dataItem.getWidth());
                        } else if (dataItem.getAlignment()==Constants.ALIGN_RIGHT) {
                            value = padLeft(value,dataItem.getWidth());
                        } else {
                            value = padCenter(value,dataItem.getWidth());
                        }
                    }
                    
                    sb.append("\t\t");
                    sb.append(enclChar);
                    sb.append(name);
                    sb.append(enclChar);
                    sb.append(" : ");
                    if (type.equalsIgnoreCase("string") && !(value.equalsIgnoreCase("null"))) {
                        sb.append(enclChar);
                        sb.append(value);
                        sb.append(enclChar);
                    } else {
                        sb.append(value);
                    }
                    sb.append(dataFileDefinition.getDelimiter());
                    sb.append("\n");
                }
                
                cancelled = notifyProgrssUpdate("Generating ("+ (i+1) + "/" +numOfRecs+")", i+1 );
                
                sb.deleteCharAt(sb.lastIndexOf(dataFileDefinition.getDelimiter()));
                if ((i+1) == numOfRecs) {
                    sb.append("\t}");
                } else {
                    sb.append("\t},");
                }
                outLine = sb.toString();
                fileWriter.write(outLine);
                fileWriter.newLine();
                logger.debug(outLine);
                i++;
            } catch (IOException ioe) {
                logger.warn("Exception error while writing data, will exit after loops:"+i, ioe);
                error=true;
            }
        }
        
        try {
            fileWriter.write("]");
            fileWriter.newLine();
            fileWriter.close();
        } catch (IOException ioe) {
            logger.warn("Exception error while closing file:", ioe);
            observer.datageGenError("Error during a file operation!");
            return;
        }
        
        long stop = System.currentTimeMillis();
        logger.debug("Time in millis:" + (stop-start) );
        
        if (error) {
            observer.datageGenError("There were errors during the data generation progress, possibly a file write error.");
            return;
        }
        
        notifyEnd();
    }
    
    public void registerObserver(ProgressUpdateObserver observer) {
        this.observer = observer;
    }
    
    public void unregisterObserver() {
        this.observer = null;
    }
    
    public void notifyInit() {
        if (observer!=null) {
            observer.dataGenStarted();
        }
    }
    
    public void notifyMaxProgressValue(int max) {
        if (observer!=null) {
            observer.dataGenMaxProgressValue(max);
        }
    }
    
    public boolean notifyProgrssUpdate(String msg, int progress) {
        boolean shouldContinue = false;
        
        if (observer!=null) {
            shouldContinue = observer.dataGenProgressContinue(msg, progress);
        } else {
            shouldContinue = false;
        }
        
        return shouldContinue;
    }
    
    public void notifyEnd() {
        if (observer!=null) {
            observer.dataGenEnd();
            observer=null;
        }
    }
    
    public void datageGenError(String msg) {
        if (observer!=null) {
            observer.datageGenError(msg);
            observer.dataGenEnd();
        }
    }
    
}
