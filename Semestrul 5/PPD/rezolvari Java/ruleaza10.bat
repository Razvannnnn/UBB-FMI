@echo off
setlocal enabledelayedexpansion

REM === Directorul unde sunt fisierele .class ===
set JAVA_PATH=C:\Users\razva\Desktop\PPD\rezolvari Java\rezolvare_java\src

REM === Fisierul CSV unde se salveaza timpii ===
set OUTPUT_CSV=C:\Users\razva\Desktop\PPD\rezolvari Java\rezolvare_java\outputs_lab\timpi_secv.csv

pushd "%JAVA_PATH%"

REM === Compilam toate fisierele Java (optional, in caz ca nu sunt deja compilate) ===
echo Compilare Java...
javac *.java
if errorlevel 1 (
    echo Eroare la compilare!
    pause
    exit /b
)

REM === Stergem fisierul CSV vechi (daca exista) ===
if exist "%OUTPUT_CSV%" del "%OUTPUT_CSV%"

REM === Rulam programul de 10 ori ===
for /l %%i in (1,1,10) do (
    echo Rulare %%i ...
    for /f "tokens=*" %%t in ('java -cp . Main %%i') do (
        echo %%i,%%t >> "%OUTPUT_CSV%"
    )
)

popd
echo Gata! Programul Java s-a rulat de 10 ori.
pause
