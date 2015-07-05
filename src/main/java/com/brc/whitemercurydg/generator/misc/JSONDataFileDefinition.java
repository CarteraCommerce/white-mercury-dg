/**
 * 
 */

package com.brc.whitemercurydg.generator.misc;

import java.util.Vector;

public class JSONDataFileDefinition
{
    private String name;
    private String description;
    private Vector<JSONDataFileItem> outDataItems;
    private String outFilename;
    private String delimiter;
    private long numOfRecs;
    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vector<JSONDataFileItem> getOutDataItems() {
        return outDataItems;
    }

    public void setOutDataItems(Vector<JSONDataFileItem> vOutData) {
        this.outDataItems = vOutData;
    }

    public String getOutFilename() {
        return outFilename;
    }

    public void setOutFilename(String outFilename) {
        this.outFilename = outFilename;
    }

    public String getDescription() {
        return this.description;        
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public long getNumOfRecs() {
        return numOfRecs;
    }

    public void setNumOfRecs(long numOfRecs) {
        this.numOfRecs = numOfRecs;
    }
    
    public String toString() {
        return name;        
    }
}
