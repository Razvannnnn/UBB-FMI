#!/bin/bash

mkdir -p scripts
mkdir -p outputs_lab

sizes=(10 1000 10000)
threads=(1 2 4 8 16)

for N in "${sizes[@]}"; do
    for P in "${threads[@]}"; do
        if [ "$P" -eq 1 ]; then
            type="seq"
        else
            type="par_p$P"
        fi

        script_name="scripts/run_${N}x${N}_${type}.sh"

        cat > "$script_name" <<EOL
#!/bin/bash
echo "[INFO] Rulare ${N}x${N} ${type} de 10 ori..."

total=0

for i in {1..10}; do
    echo "[INFO] Incep rularea \$i..."
    # Extrage timpul din ultima coloană a output-ului conv.exe
    time_ns=\$(./conv.exe $N ${type%%_*} ${P} | awk '{print \$NF}')
    time_ns_clean=\$(echo \$time_ns | tr -d '[:space:]')
    total=\$((total + time_ns_clean))
    echo "[INFO] Rularea \$i s-a terminat."
done

avg=\$((total / 10))

echo "[RESULT] Timp total: \$total ns"
echo "[RESULT] Timp mediu:  \$avg ns"
EOL

        chmod +x "$script_name"
    done
done

echo "Cele 15 scripturi au fost generate în folderul 'scripts/'"
