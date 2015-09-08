/**
 *
 */
package com.brc.whitemercurydg.tests;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;
import static org.testng.Assert.assertNotNull;

import com.brc.whitemercurydg.RunController;
import com.brc.whitemercurydg.RunMode;

/**
 * @author tspaulding
 *
 */
public class TextTest {

    private static final int TEXTTEST_OUT_LEN = 101;
    private static final String RUN_MODE_SYS_PROPERTY_VALUE = "test";
    private static final String RUN_MODE_DEF_SYS_PROPERTY_VALUE = "generate_test";
    private static final String TEXT_TEST_FILE = "white-mercury-dg_test_file.csv";

    @BeforeMethod
    public void removeTestFileForTextSmokeTest() {
        File testFile = new File(TEXT_TEST_FILE);

        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test(groups = { "full", "smoke" })
    public void textSmokeTest() {
        String output = null;
        RunController runner = new RunController();
        String[] args = {};
        RunMode runMode = RunMode.fromString(RUN_MODE_SYS_PROPERTY_VALUE);
        runMode.setDefinition(RUN_MODE_DEF_SYS_PROPERTY_VALUE);
        URL log4jConfig = this.getClass().getClassLoader().getResource("generator.xml");
        int count = 0;

        try {
            output = runner.runByMode(args, runMode, log4jConfig);
            count = lineCount(TEXT_TEST_FILE);
            assertEquals(count, TEXTTEST_OUT_LEN);
        } catch (Exception ex) {
            System.err.println(ex);
            ex.printStackTrace();
            fail("Unexpected exception during test.", ex);
        }

        assertNotNull(output);
    }

    private int lineCount(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }
}
