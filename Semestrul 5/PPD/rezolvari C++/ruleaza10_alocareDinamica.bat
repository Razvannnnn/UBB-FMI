@echo off

pushd "C:\\Users\\razva\\Desktop\\PPD\\rezolvari C++\\rezolvare_alocare_dinamica_cpp\\x64\\Debug"

REM Loop pentru 10 rulari
for /l %%i in (1,1,10) do (
    echo [INFO] Incep rularea %%i...
    for /f "tokens=*" %%t in ('"rezolvare_alocare_dinamica_cpp" %%i') do (
        echo %%i,%%t >> "C:\\Users\\razva\\Desktop\\PPD\\rezolvari C++\\rezolvare_alocare_dinamica_cpp\\outputs_lab\\timpi_secv.csv"
    )
    echo [INFO] Rularea %%i s-a terminat.
)

popd
echo Gata! Executabilul s-a rulat de 10 ori.
pause
