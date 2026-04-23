#!/bin/bash
echo "[INFO] Rulare 1000x1000 par_p16 de 10 ori..."

total=0

for i in {1..10}; do
    echo "[INFO] Incep rularea $i..."
    # Extrage timpul din ultima coloană a output-ului conv.exe
    time_ns=$(./conv.exe 1000 par 16 | awk '{print $NF}')
    time_ns_clean=$(echo $time_ns | tr -d '[:space:]')
    total=$((total + time_ns_clean))
    echo "[INFO] Rularea $i s-a terminat."
done

avg=$((total / 10))

echo "[RESULT] Timp total: $total ns"
echo "[RESULT] Timp mediu:  $avg ns"
