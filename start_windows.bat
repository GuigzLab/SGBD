@echo on
javac -d Code/out/production/ Code/src/up/mi/sgbdr/*.java
java -cp ./Code/out/production/ up.mi.sgbdr.Main
pause
