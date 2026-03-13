all: Compiler 

Compiler: Compiler.java lParser
	javac Compiler.java

lParser: l.cfg
	java -jar ../sablecc/sablecc.jar l.cfg

clean:
	-rm *.class
	-rm sc/*.class
	-rm sa/*.class
	-rm c3a/*.class
	-rm ts/*.class
	-rm nasm/*.class
	-rm fg/*.class
	-rm ig/*.class
	-rm util/intset/*.class
	-rm util/graph/*.class
	-rm -r lParser
	-rm -r saParser
	-rm -r c3aParser
	-rm -r nasmParser

