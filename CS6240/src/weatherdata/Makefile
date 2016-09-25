
weatherdata.jar: Main.java build.gradle Makefile
	gradle build
	gradle shadowJar
	cp build/libs/weatherdata-all.jar weatherdata.jar

run: weatherdata.jar
	rm -rf output
	java -jar weatherdata.jar 1877.csv

clean:
	rm -rf build weatherdata.jar output .gradle
