White Mercury Data Generator Project
====================================

Description
-----------

I usually deal with large amounts of data.  These large sets of data usually contain PII (Personally-Identifiable Information) data.  If you need to prototype or test out a concept, using production data is problematic.  In almost all cases, we achieve better results from using randomized data.  This project aspires to provide a complete set of tools for generating large sets of data in a variety of structures to a variety of data store types.  The tools are designed to be integrated with build and development tools so that they can be used in continuous integration and other agile development activities.

The data generation engine is based on the dgMaster open-sourced data generation toolkit.  You can take a look at the original project at the [dgMaster Web Site](http://dgmaster.sourceforge.net/).  The original project was published under the GPL v2 license which means that any derivative works (which includes our data generation tool) must be published under the same license.

The White Mercury Data Generation tool is an executable shaded jar file.  A 'shaded' jar is a jar with all of it's run-time dependencies packed inside it.  The runMode option sets the primary operating mode for this utility.  All options are passed as Java system options.

Usage
-----

	java [options] -jar [path to jar]white-mercury-dg-[version qualifer]-shaded.jar

Options
-------

	-DrunMode=[legacy | test | json | database]

		legacy - run the utility with the legacy Swing UI from dgMaster.
		test - run the utility with default parameters to generate a test file.
		database - run the utility to generate data in a database.

	-DdbUrl=[URL of target database]

		Defaults to localhost.  Only applicable when runMode is set to 'database'.

	-DdbUser=[user name for target database]

		Defaults to root.  Only applicable when runMode is set to 'database'.

	-DdbPassword=[password for target database]

		Only applicable when runMode is set to 'database'.

	-Dcycles=[number of rows to generate in target]

		Defaults to 100 rows.  Only applicable when runMode is set to 'database' or 'json'.
