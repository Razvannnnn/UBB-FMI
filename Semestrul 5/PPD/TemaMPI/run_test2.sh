#!/bin/bash

# =====================================
# Executabile
# =====================================
# S-a revenit la executabilele separate, conform scriptului tau original
SEQ_EXE="./main_seq"
PAR_V1_EXE="./main_par_v1"
PAR_V2_EXE="./main_par_v2"
PAR_V3_EXE="./main_par_v3"

# =====================================
# Teste pentru V1
# =====================================
TESTS_V1_N1=("16" "10000" "100")
TESTS_V1_N2=("16" "10000" "100000")
PROCS_V1=(5 9 17)    # special: prima intrare folosește doar 5!

# =====================================
# Teste pentru V2
# =====================================
TESTS_V2_N1=("16" "1000" "100")
TESTS_V2_N2=("16" "1000" "100000")
PROCS_V2=(4 8 16)

# =====================================
# Teste pentru V3
# =====================================
TESTS_V3_N1=("16" "10000" "100")
TESTS_V3_N2=("16" "10000" "100000")
PROCS_V3=(5 9 17)

RUNS=10

# =====================================
# Verificare corectitudine
# =====================================
function verifica() {
    if diff -q "Numar_3_secvential.txt" "Numar_3.txt" > /dev/null ; then
        echo "    ✔ CORECT"
    else
        echo "    ✘ DIFERIT!"
    fi
}

# =====================================
# Rulare 10 execuții + media
# =====================================
function ruleaza_10() {
    local EXE=$1
    local LABEL=$2
    local P=$3
    local N1=$4
    local N2=$5

    echo "→ $LABEL | N1=$N1 N2=$N2 | Procese=$P"
    echo "------------------------------------------"

    total=0.0 # Initializam ca float pentru awk

    for ((i=1;i<=RUNS;i++)); do
        echo "    ➤ Rulare $i/$RUNS"

        rm -f Numar_3.txt

        # Se apeleaza executabilul specific (ex: ./main_par_v1)
        T=$( ( /usr/bin/time -f "%e" mpirun -np $P $EXE > /dev/null ) 2>&1 )
        
        # Verificam daca T este un numar valid (accepta . sau ,)
        if [[ "$T" =~ ^[0-9]+([.,][0-9]+)?$ ]]; then
             # Inlocuim , cu . pentru a fi compatibil cu awk
             T_valid=$(echo "$T" | tr ',' '.')
             total=$(awk "BEGIN {print $total + $T_valid}")
        else
            echo "    ! Eroare la executie, T=$T"
            total=$(awk "BEGIN {print $total + 0}")
        fi
        
        verifica
    done

    # Folosim awk pentru calculul mediei si formatare
    media=$(awk "BEGIN {printf \"%.4f\", $total / $RUNS}")
    echo "    ⏱ Media: $media secunde"
    echo ""
}


# =====================================
# Funcție generică pentru variante V1/V2/V3
# =====================================
function ruleaza_varianta() {
    local EXE=$1
    local LABEL=$2
    local -n L_N1=$3
    local -n L_N2=$4
    local -n L_PROCS=$5

    echo ""
    echo "=========================================="
    echo "============    $LABEL    =================="
    echo "=========================================="

    for idx in ${!L_N1[@]}; do
        N1=${L_N1[$idx]}
        N2=${L_N2[$idx]}

        echo ""
        echo "===== TEST: N1=$N1 , N2=$N2 ====="

        rm -f Numar_3.txt Numar_3_secvential.txt

        cp "Numar_1_${N1}.txt" "Numar_1.txt"
        cp "Numar_2_${N2}.txt" "Numar_2.txt"

        # Se apeleaza executabilul secvential
        $SEQ_EXE > /dev/null
        mv "Numar_3.txt" "Numar_3_secvential.txt"

        if [[ "$idx" == "0" ]]; then
            ruleaza_10 "$EXE" "$LABEL" "${L_PROCS[0]}" "$N1" "$N2"
        else
            for p in "${L_PROCS[@]}"; do
                ruleaza_10 "$EXE" "$LABEL" "$p" "$N1" "$N2"
            done
        fi
    done
}

# =====================================
# RULARE FINALĂ
# =====================================
# Asigura-te ca fisierele de input (ex: Numar_1_16.txt) exista!
# Si ca executabilele ./main_seq, ./main_par_v1, etc. exista!

ruleaza_varianta "$PAR_V1_EXE" "V1" TESTS_V1_N1 TESTS_V1_N2 PROCS_V1
ruleaza_varianta "$PAR_V2_EXE" "V2" TESTS_V2_N1 TESTS_V2_N2 PROCS_V2
ruleaza_varianta "$PAR_V3_EXE" "V3" TESTS_V3_N1 TESTS_V3_N2 PROCS_V3
