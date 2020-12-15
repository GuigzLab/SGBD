@echo on
javac -d Code/out/production/Code/ Code/src/up/mi/sgbdr/*.java
java -cp ./Code/out/production/Code/ up.mi.sgbdr.Main
pause
