#!/bin/bash

# === CONFIG ===
SEQ_EXE="./main_seq"
PAR_V1_EXE="./main_par_v1"
PAR_V2_EXE="./main_par_v2"
PAR_V3_EXE="./main_par_v3"

# Fisiere de test (nume fara extensie)
declare -a TESTS_V1_N1=(
    "16"
    "10000"
    "100"
)

declare -a TESTS_V1_N2=(
    "16"
    "10000"
    "100000"
)

declare -a TESTS_V2_N1=(
    "16"
    "1000"
    "100"
)
declare -a TESTS_V2_N2=(
    "16"
    "1000"
    "100000"
)

declare -a TESTS_V3_N1=(
    "16"
    "10000"
    "100"
)
declare -a TESTS_V3_N2=(
    "16"
    "10000"
    "100000"
)

# Numar de procese de testat
declare -a PROCS_V1=(5 9 17)
declare -a PROCS_V2=(4 8 16)
declare -a PROCS_V3=(5 9 17)


# === Functie pentru verificarea corectitudinii ===
function verifica() {
    if diff -q "Numar_3_secvential.txt" "Numar_3.txt" > /dev/null ; then
        echo "CORECT"
    else
        echo "Rezultatul este DIFERIT!"
        diff "Numar_3_secvential.txt" "Numar_3.txt" | head -n 5
    fi
}

# === Testare pentru fiecare caz ===
for size in "${TESTS[@]}"; do
    echo "=========================================="
    echo "🔹 Test pentru N1 = N2 = $size"
    echo "=========================================="

    # Ruleaza varianta secventiala
    echo "→ Rulare varianta secvențială..."
    cp "Numar_1_${size}.txt" "Numar_1.txt"
    cp "Numar_2_${size}.txt" "Numar_2.txt"
    /usr/bin/time -f "Timp secvential: %E" $SEQ_EXE > /dev/null

    mv "Numar_3.txt" "Numar_3_secvential.txt"

    # Ruleaza toate variantele paralele
    for p in "${PROCS[@]}"; do
        echo ""
        echo "→ Test cu $p procese"
        echo "-----------------------------"

	echo "▶ Varianta 1"
        /usr/bin/time -f "Timp paralel (v1): %E" mpirun -np $p $PAR_V1_EXE > /dev/null
        verifica
        mv "Numar_3.txt" "Numar_3_v1_${size}_${p}.txt"

        echo "▶ Varianta 2 (scatter/gather)"
        /usr/bin/time -f "Timp paralel (v2): %E" mpirun -np $p $PAR_V2_EXE > /dev/null
        verifica
        mv "Numar_3.txt" "Numar_3_v2_${size}_${p}.txt"

        echo "▶ Varianta 3 (asincronă)"
        /usr/bin/time -f "Timp paralel (v3): %E" mpirun -np $p $PAR_V3_EXE > /dev/null
        verifica
        mv "Numar_3.txt" "Numar_3_v3_${size}_${p}.txt"
    done
done
