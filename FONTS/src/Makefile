#NOM_VAR := path \ path
DIR_DOMINI = classes/domini
DIR_PERSISTENCIA = classes/persistencia
DIR_PRESENTACIO = classes/presentacio
DIR_CONTROLADOR = controladors
DIR_DRIVER = driver
DIR_IMAGES = images

LIBS = ./lib/junit-4.12.jar:./lib/hamcrest-core-1.3.jar:./lib/gson-2.10.1.jar

CLASS_INPUT = $(DIR_DOMINI)/*java \
				$(DIR_PERSISTENCIA)/*java \
				$(DIR_PRESENTACIO)/*java \
				$(DIR_CONTROLADOR)/*java 


SRC_FILES = $(wildcard $(DIR_DOMINI)/*.java) \
            $(wildcard $(DIR_PERSISTENCIA)/*.java) \
            $(wildcard $(DIR_PRESENTACIO)/*.java) \
            $(wildcard $(DIR_CONTROLADOR)/*.java)

SRC_DRIVER = $(wildcard $(DIR_DRIVER)/*.java)


JAR_OUTPUT = ../../EXE/JAR/
CLASS_OUTPUT = ../../EXE/CLASS

all: run 

run: jars
	java -cp ../../EXE/JAR/DriverPresentacio.jar:./lib/junit-4.12.jar:./lib/hamcrest-core-1.3.jar:./lib/gson-2.10.1.jar driver.DriverPresentacio

	
jars: class
	mkdir -p $(JAR_OUTPUT)
	javac -cp $(CLASS_OUTPUT):$(LIBS) -d $(CLASS_OUTPUT) $(CLASS_INPUT) $(SRC_DRIVER)
	echo "Main-Class: driver.DriverPresentacio\nClass-Path: ./lib/junit-4.12.jar ./lib/hamcrest-core-1.3.jar ./lib/gson-2.10.1.jar" > ./driver/DriverPresentacio.mf
	jar cmvf ./driver/DriverPresentacio.mf $(JAR_OUTPUT)DriverPresentacio.jar -C $(CLASS_OUTPUT) . -C . $(DIR_IMAGES) 


class: $(SRC_FILES)
	javac -cp $(LIBS) -d $(CLASS_OUTPUT) $(SRC_FILES)

javadoc:
	javadoc -cp $(CLASS_OUTPUT):$(LIBS) -d ../../DOCS/javadoc/ -linksource $(CLASS_INPUT)


clean:
	rm -rf ../../EXE/CLASS/
	rm -rf ../../EXE/JAR/