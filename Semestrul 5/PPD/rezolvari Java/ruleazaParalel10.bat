@echo off
setlocal enabledelayedexpansion

REM === Directorul unde sunt fisierele .java / .class ===
set JAVA_PATH=C:\Users\razva\Desktop\PPD\rezolvari Java\rezolvare_java\src

REM === Fisierul CSV unde salvam timpii ===
set OUTPUT_CSV=C:\Users\razva\Desktop\PPD\rezolvari Java\rezolvare_java\outputs_lab\timpi_paralel.csv

pushd "%JAVA_PATH%"

REM === Compilam fisierele Java (daca e nevoie) ===
echo Compilare fisiere Java...
javac *.java
if errorlevel 1 (
    echo Eroare la compilare! Scriptul se opreste.
    pause
    exit /b
)

REM === Stergem CSV-ul vechi (daca exista) ===
if exist "%OUTPUT_CSV%" del "%OUTPUT_CSV%"

REM === Scriem header-ul CSV ===
echo nr_iteratie,timp_linii,timp_coloane > "%OUTPUT_CSV%"

REM === Rulam programul Java de 10 ori ===
for /l %%i in (1,1,10) do (
    echo Rulare %%i ...
    set "count=0"

    REM Rulam clasa Main si capturam cele doua timpi afisati
    for /f "tokens=*" %%t in ('java -cp . Main %%i') do (
        set /a count+=1
        if !count! equ 1 set "timp_linii=%%t"
        if !count! equ 2 set "timp_coloane=%%t"
    )

    echo %%i,!timp_linii!,!timp_coloane! >> "%OUTPUT_CSV%"
)

popd

echo.
echo Gata! Programul Java s-a rulat de 10 ori.
pause
