/**
 *
 */
package com.brc.whitemercurydg;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;

import org.apache.log4j.xml.DOMConfigurator;
import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;

import com.brc.whitemercurydg.generator.engine.json.Generator;
import com.brc.whitemercurydg.generator.misc.JSONDataFileDefinition;
import com.brc.whitemercurydg.generator.misc.JSONDataFileOutputSAXHandler;

import generator.db.DBGeneratorDefinition;
import generator.db.DBTableGenerator;
import generator.gui.MainForm;
import generator.misc.ApplicationContext;
import generator.misc.RandomDefinitionsBuilder;

/**
 * @author tspaulding
 *
 */
public class RunController {

    private MainForm legacyUI;

    public String runByMode(String[] args, RunMode currRunMode, URL log4jConfig) throws Exception {
        String output = "";

        if (args.length > 0) {
            output = output + outputBanner("Argument List");

            for (String arg : args) {
                output = output + "Argument: " + arg + "\n";
            }
        }

        output = output + outputBanner("RunMode: " + currRunMode.getText());

        DOMConfigurator.configure(log4jConfig);

        switch (currRunMode) {
            case JSON:
                output = generateJSON(output, currRunMode.getDefinition());
                break;
            case TEXT:
                output = generateTextFile(output, currRunMode.getDefinition());
                break;
            case TEST:
                output = generateTextFile(output, currRunMode.getDefinition());
                break;
            default:
                output = generateUsage(output);
        }
        
        return output;
    }

    public String generateDatabase(String output) throws ClassNotFoundException, SQLException, Exception {
        StopWatch stopWatch = new LoggingStopWatch("whitemercurydgMain.generateDatabase()");

        com.brc.whitemercurydg.generator.engine.db.Generator generator = new com.brc.whitemercurydg.generator.engine.db.Generator();
        DBGeneratorDefinition currDefinition = new DBGeneratorDefinition();

        outputBanner("Generating database data...");

        populateDefinitionWithDefaults(currDefinition);

        output = output + currDefinition.outputDefinition();

        generator.setDbGenConfig(currDefinition);
        generator.generate();

        stopWatch.stop();

        return output;
    }

    private void populateDefinitionWithDefaults(DBGeneratorDefinition currDef) {
        DBTableGenerator currTblGen = new DBTableGenerator();

        currDef.setScenario("Default MySQL T_MASTER Load");
        currDef.setDescription("Loads the default number of rows into a local instance of mysql with the default structure for T_MASTER.");
        currDef.setDbDriver("com.mysql.jdbc.Driver");
        currDef.setDbURL(System.getProperty("dbUrl", "jdbc:mysql://localhost:3306/whitemercurydgtest"));
        currDef.setUser(System.getProperty("dbUser", "root"));
        currDef.setPassword(System.getProperty("dbPassword"));
        currDef.setCycles(Integer.parseInt(System.getProperty("cycles", "10")));

        currDef.setDBTableGenerators(new ArrayList<DBTableGenerator>());
        currTblGen.populateTableGeneratorWithDefaults();
        currDef.getDBTableGenerators().add(currTblGen);
    }

    private String generateJSON(String output, String fileDefinition) throws Exception {
        Generator generator = new Generator();
        WhiteMercuryProgressObserver observer = new WhiteMercuryProgressObserver();

        outputBanner("Generating " + "fileDefinition" + " json file...");

        generator.registerObserver(observer);
        generator.setEngineData(ApplicationContext.getInstance().getRandomiserTypes(), ApplicationContext.getInstance().getRandomiserInstances(), loadJSONDataFileDefinitions());
        if (generator.setFileDefinitionOutput("generate_tmaster_json_" + Integer.parseInt(System.getProperty("cycles", "100")))) {
            generator.generate();
        } else {
            throw new Exception("JSON file definition not found, will do nothing.");
        }

        return output;
    }

    @SuppressWarnings("unchecked")
    private Vector<JSONDataFileDefinition> loadJSONDataFileDefinitions() {
        Vector<JSONDataFileDefinition> vData;
        RandomDefinitionsBuilder builder = new RandomDefinitionsBuilder();

        builder.setFilename("JSONFileDefinitions.xml");
        builder.setSAXHandler(new JSONDataFileOutputSAXHandler() );
        vData = builder.getElements();

        return vData;
    }

    public void runLegacySwingUI(String[] args) {
        legacyUI = new MainForm();
        legacyUI.setVisible(true);
    }

    public void disposeLegacyUI() {
        legacyUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        legacyUI.dispose();
    }

    public String generateTextFile(String output, String fileDefinition) throws Exception {
        generator.engine.file.Generator generator = new generator.engine.file.Generator();
        WhiteMercuryProgressObserver observer = new WhiteMercuryProgressObserver();

        outputBanner("Generating " + fileDefinition + " csv file...");

        generator.registerObserver(observer);
        generator.setEngineData(ApplicationContext.getInstance().getRandomiserTypes(), ApplicationContext.getInstance().getRandomiserInstances(), ApplicationContext.getInstance().getDFD());
        if (generator.setFileDefinitionOutput(fileDefinition)) {
            generator.generate();
        } else {
            throw new Exception("File definition not found, will do nothing.");
        }

        return output;
    }

    private String outputBanner(String msg) {
        String output = "";

        output = "\n\n\n\n";
        output = output + "################################################################################\n";
        output = output + "##  " + msg + "\n";
        output = output + "################################################################################\n";
        output = output + "\n\n\n\n";

        return output;
    }

    public String generateUsage(String output) {
        output = output + outputBanner("White Mercury Usage");
        output = output + "Usage:\n";
        output = output + "\tjava [options] -jar [path to jar]white-mercury-dg-[version qualifer]-shaded.jar\n\n";
        output = output + "Description:\n";
        output = output + "\tThe White Mercury Data Generation tool is an executable shaded jar file.\n";
        output = output + "\tA \'shaded\' jar is a jar with all of it's run-time dependencies packed\n";
        output = output + "\tinside it.  The runMode option sets the primary operating mode for this\n";
        output = output + "\tutility.  All options are passed as Java system options.\n\n";
        output = output + "Options:\n";
        output = output + "\t-DrunMode=[json | text | test]\n";
        output = output + "\t\tjson - run the utility with default parameters to generate a json file.\n";
        output = output + "\t\ttext - run the utility with default parameters to generate a csv file.\n";
        output = output + "\t\ttest - run the utility with default parameters to generate a test csv file.\n";
        output = output + "\t-DrunModeDef=[file definition name]\n";
        output = output + "\t\tIndicates the name of the file definition to use for generating the data\n";
        output = output + "\t\tfile.  The default value is \'generate_test\'.\n";

        return output;
    }

}
