#!/bin/bash
echo "[INFO] Rulare 10000x10000 par_p2 de 10 ori..."

total=0

for i in {1..10}; do
    echo "[INFO] Incep rularea $i..."
    # Extrage timpul din ultima coloană a output-ului conv.exe
    time_ns=$(./conv.exe 10000 par 2 | awk '{print $NF}')
    time_ns_clean=$(echo $time_ns | tr -d '[:space:]')
    total=$((total + time_ns_clean))
    echo "[INFO] Rularea $i s-a terminat."
done

avg=$((total / 10))

echo "[RESULT] Timp total: $total ns"
echo "[RESULT] Timp mediu:  $avg ns"
