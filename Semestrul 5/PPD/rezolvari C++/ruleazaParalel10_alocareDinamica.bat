@echo off
setlocal enabledelayedexpansion

pushd "C:\\Users\\razva\\Desktop\\PPD\\rezolvari C++\\rezolvare_alocare_dinamica_cpp\\x64\\Debug"

set "OUTPUT=C:\\Users\\razva\\Desktop\\PPD\\rezolvari C++\\rezolvare_alocare_dinamica_cpp\\outputs_lab\\timpi_paralel.csv"

if exist "%OUTPUT%" del "%OUTPUT%"
echo nr_iteratie,timp_linii,timp_coloane > "%OUTPUT%"

REM Loop pentru 10 rulari
for /l %%i in (1,1,10) do (
    set "count=0"
    for /f "tokens=*" %%t in ('"rezolvare_alocare_dinamica_cpp" %%i') do (
        set /a count+=1
        if !count! equ 1 set "timp_linii=%%t"
        if !count! equ 2 set "timp_coloane=%%t"
    )
    echo %%i,!timp_linii!,!timp_coloane! >> "%OUTPUT%"
)

popd
echo Gata! Executabilul s-a rulat de 10 ori.
pause
