# Rehaan Mehta Quantcast test

## Running main program

To run the main test:
- In the terminal write: 
- `java most_active_cookie <name of test file> -d <date>`

## Running the test suite
- The test suite is uses JUnit. All required jar files are already included in the lib folder.
- If you have ant installed on your machine, tests can be run by running `ant test`.
- Alternatively if ant is not available, to run tests, in the terminal write: 
- `java -cp /absolute/path/for/compiled/classes:/absolute/path/to/junit-4.13.2.jar:/absolute/path/to/hamcrest-core-1.3.jar org.junit.runner.JUnitCore your.package.TestClassName`
- ***Note:** The junit jar and the hamcrest jar are already included in the lib folder. Only the absolute file path needs to be provided to run the test suite.*