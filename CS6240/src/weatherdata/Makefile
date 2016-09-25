
weatherdata.jar: Main.java build.gradle Makefile
	gradle build
	gradle shadowJar
	cp build/libs/weatherdata-all.jar weatherdata.jar

local: weatherdata.jar
	rm -rf output
	java -Xmx2048m -jar weatherdata.jar input.csv

clean:
	rm -rf build weatherdata.jar output .gradle
