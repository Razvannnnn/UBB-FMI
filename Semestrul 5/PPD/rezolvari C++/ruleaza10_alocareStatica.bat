@echo off

pushd "C:\\Users\\razva\\Desktop\\PPD\\rezolvari C++\\rezolvare_alocare_statica_cpp\\x64\\Debug"

REM Loop pentru 10 rulari
for /l %%i in (1,1,10) do (
    for /f "tokens=*" %%t in ('"rezolvare_secventiala_cpp" %%i') do (
        echo %%i,%%t >> "C:\\Users\\razva\\Desktop\\PPD\\rezolvari C++\\rezolvare_alocare_statica_cpp\\outputs_lab\\timpi.csv"
    )
)
popd
echo Gata! Executabilul s-a rulat de 10 ori.
pause
