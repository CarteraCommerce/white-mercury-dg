/**
 * 
 */

package com.brc.whitemercurydg.generator.misc;

public class JSONDataFileItem {
    private String randomizerInstanceName;
    private String name;
    private String type;
    private int alignment;
    private String encloseChar;
    private int width;
    private String separator;
    
    public String getRandomizerInstanceName() {
        return randomizerInstanceName;
    }

    public void setRandomizerInstanceName(String randomizerInstanceName) {
        this.randomizerInstanceName = randomizerInstanceName;
    }

    public int getAlignment() {
        return alignment;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public String getEncloseChar() {
        return encloseChar;
    }

    public void setEncloseChar(String encloseChar) {
         this.encloseChar = encloseChar;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}