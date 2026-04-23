@echo off
setlocal enabledelayedexpansion

pushd "D:\UBB INFO\UBB INFO - ANUL III (2025-2026)\Semestrul 5\PPD\Teme-Lab\Lab1\rezolvari Bonus\rezolvare_bonus_cpp\x64\Debug"

set "OUTPUT=D:\UBB INFO\UBB INFO - ANUL III (2025-2026)\Semestrul 5\PPD\Teme-Lab\Lab1\rezolvari Bonus\rezolvare_bonus_cpp\outputs_lab\timpi_paralel.csv"

if exist "%OUTPUT%" del "%OUTPUT%"
echo nr_iteratie,timp_blocuri,timp_delta_linear,timp_delta_cyclic > "%OUTPUT%"

REM Loop pentru 10 rulari
for /l %%i in (1,1,10) do (
    set "count=0"
    for /f "tokens=*" %%t in ('"rezolvare_bonus_cpp" %%i') do (
        set /a count+=1
        if !count! equ 1 set "timp_blocuri=%%t"
        if !count! equ 2 set "timp_delta_linear=%%t"
	if !count! equ 3 set "timp_delta_cyclic=%%t"
    )
    echo %%i,!timp_blocuri!,!timp_delta_linear!,!timp_delta_cyclic! >> "%OUTPUT%"
)

popd
echo Gata! Executabilul s-a rulat de 10 ori.
pause
