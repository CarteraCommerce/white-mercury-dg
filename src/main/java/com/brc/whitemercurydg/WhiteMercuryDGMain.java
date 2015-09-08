/**
 *
 */
package com.brc.whitemercurydg;

import java.net.URL;


/**
 * @author tim
 *
 */
public class WhiteMercuryDGMain {

    public static final int EXIT_FAILURE = 1;

    /**
     * @param args
     */
    public static void main(String[] args) {
        String output = null;
        RunController runner = new RunController();
        RunMode wmdgRunMode = RunMode.fromString(System.getProperty("runMode"));
        wmdgRunMode.setDefinition(System.getProperty("runModeDef", "generate_test"));
        URL log4jConfig = WhiteMercuryDGMain.class.getResource("/generator.xml");

        try {
            output = runner.runByMode(args, wmdgRunMode, log4jConfig);
            
            if (output != null) {
                System.out.println(output);
            }
        } catch (Exception ex) {
            System.err.println(ex.toString());
            System.exit(EXIT_FAILURE);
        }
    }

}
