java -jar antlr-4.8-complete.jar CPP14.g4
javac -cp ./antlr-4.8-complete.jar *.java
java -cp ".;antlr-4.8-complete.jar" org.antlr.v4.gui.TestRig  CPP14 translationunit -gui HelloWorld.cc
