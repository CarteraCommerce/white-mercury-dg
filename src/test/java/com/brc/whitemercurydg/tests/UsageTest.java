/**
 *
 */
package com.brc.whitemercurydg.tests;

import java.net.URL;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import com.brc.whitemercurydg.RunController;
import com.brc.whitemercurydg.RunMode;

/**
 * @author tim
 *
 */

public class UsageTest {

	private static final int USAGETEST_OUT_LEN = 1187;
	private static final int USAGETEST_WITH_DUMMY_ARG_OUT_LEN = 1396;
	private static final String DUMMY_ARG = "--dummyArg";

	@Test(groups = { "full", "smoke" })
	public void usageSmokeTest() {
		String output = null;
		RunController runner = new RunController();
		String[] args = {};
		RunMode runMode = RunMode.fromString(null);
		URL log4jConfig = this.getClass().getClassLoader().getResource("generator.xml");

		try {
			output = runner.runByMode(args, runMode, log4jConfig);
		} catch(Exception ex) {
			fail("Unexpected exception during test.", ex);
		}

		assertEquals(output.length(), USAGETEST_OUT_LEN);
	}

	@Test(groups = { "full" })
	public void usageSmokeTestWithDummyArg() {
		String[] args = { DUMMY_ARG };
		String output = null;
		RunController runner = new RunController();
		RunMode runMode = RunMode.fromString(null);
		URL log4jConfig = this.getClass().getClassLoader().getResource("generator.xml");

		try {
			output = runner.runByMode(args, runMode, log4jConfig);
		} catch(Exception ex) {
			fail("Unexpected exception during test.", ex);
		}

		assertEquals(output.length(), USAGETEST_WITH_DUMMY_ARG_OUT_LEN);
	}
}
