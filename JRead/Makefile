JFLAGS = -O -classpath .:jopt-simple-3.0.jar 
JC = javac
OUTPUT = JRead
OUTPUTEXE = jmrc-read
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
  Params.java \
	PGMReader.java \
	SEGReader.java \
	Pixel.java \
	Graph.java \
	Edge.java \
	Node.java \
	JRead.java

default: classes
	jar cmfv manifest $(OUTPUT).jar *.class

	-mkdir ../bin/
	cp $(OUTPUT).jar ../bin/

	#Generate an "executable" for the java program
	echo "#/bin/bash" > $(OUTPUTEXE)
	echo -n "java -jar $(OUTPUT).jar " >> $(OUTPUTEXE)
	echo -n $$'\"\044@\"' >> $(OUTPUTEXE)

	chmod +x $(OUTPUTEXE)

	cp $(OUTPUTEXE) ../bin/
	

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

realclean: 
	$(RM) *.class
	$(RM) $(OUTPUT).jar
	$(RM) $(OUTPUTEXE)
	$(RM) ../bin/$(OUTPUT).jar
	$(RM) ../bin/$(OUTPUTEXE)

