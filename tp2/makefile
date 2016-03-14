all: CarreMagique.class

%.class: %.java
	javac -cp .:choco-solver-3.3.3-with-dependencies.jar $<

run: CarreMagique.class
	java -cp .:choco-solver-3.3.3-with-dependencies.jar CarreMagique

clean:
	rm *.class
