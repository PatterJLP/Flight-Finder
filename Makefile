runFDTests:Frontend.class FrontendDeveloperTests.class
	java -jar ../junit5.jar -cp . -c FrontendDeveloperTests
Frontend.class:Frontend.java
	javac Frontend.java

FrontendDeveloperTests.class:FrontendDeveloperTests.java
	javac -cp .:../junit5.jar FrontendDeveloperTests.java

clean: *.class
	rm *.class

runBDTests: BackendDeveloperTests.class
	java -jar ../junit5.jar -cp . -c BackendDeveloperTests
BackendDeveloperTests.class: BackendDeveloperTests.java
	javac -cp ../junit5.jar *.java
