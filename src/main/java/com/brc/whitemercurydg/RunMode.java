/**
 * 
 */
package com.brc.whitemercurydg;

/**
 * @author tspaulding
 *
 */
public enum RunMode {
    UNSET("unset"),
    LEGACY("legacy"),
    TEST("test"),
    TEXT("text"),
    JSON("json"),
    DATABASE("database");

    private String text;
    private String definition;
    
    RunMode(String text) {
        if (text == null) {
            this.text = "unset";
        } else {
            this.text = text;
        }
    }
    
    public String getText() {
        return this.text;
    }
    
    public static RunMode fromString(String text) {
        RunMode currMode = UNSET;
        
        if (text != null) {
            for (RunMode rm : RunMode.values()) {
                if (text.equalsIgnoreCase(rm.text)) {
                    currMode = rm;
                    break;
                }
            }
        }

        return currMode;

    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
