# Demo integration-tests
The simple set of 4 tests which will validate the searching of a product and addition of product to the CART.

#Technology Stack
Leveraging the APIs of Cucumber along with DollarX for efficient Xpath is utilised for meeting the BDD framework guidelines

#How to RUN the Tests
The pre-requisite for setting up the Test Suite to install JAVA8, MAVEN and other dependencies mentioned in the pom.xml.After the completion of setup tests can be triggered by running command mvn verify on terminal.
Also tests can be triggered by Running class RunCucumberIt under src > test > java. In order to run the tests individually we can run the feature files one by one by clicking on the feature file and select the run command.

In case of running each files one by one manually, you may need to add com.foreach.cuke com.e2e.integrationtests.step.definition in the Glue of the edit configuration window.

The tests run in non-headless mode with dynamic configuration for browser window size.

The headless mode execution can be triggered by changing the flag from application.yml 