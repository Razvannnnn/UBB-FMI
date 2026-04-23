@echo off

pushd "C:\Users\razva\Desktop\PPD\Tema_Lab2_\Debug"

REM Loop pentru 10 rulari
for /l %%i in (1,1,10) do (
    echo [INFO] Incep rularea %%i...
    for /f "tokens=*" %%t in ('"conv.exe" 10 seq') do (
        echo %%i,%%t >> "C:\Users\razva\Desktop\PPD\Tema_Lab2_\outputs_lab\timpi_10x10_seq.csv"
    )
    echo [INFO] Rularea %%i s-a terminat.
)

popd
echo Gata! Executabilul s-a rulat de 10 ori.
pause
