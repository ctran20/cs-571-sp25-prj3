default: test

# Target to compile all Java files
compile: bin
	javac -d bin src/*.java 

test: compile test1 test2 test3 test4

test1:
	java -cp bin TestPart1

test2:
	java -cp bin TestPart2

test3:
	java -cp bin TestPart3

test4:
	java -cp bin TestPart4

test5:
	java -cp bin TestPart5

# Target to create bin directory if it doesn't exist
bin:
	mkdir bin

clean:
	rm -rf bin/*.class 
