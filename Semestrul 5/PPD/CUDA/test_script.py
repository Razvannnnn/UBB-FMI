import subprocess
import os
import statistics
import time

EXECUTABLE = "main" 
SOURCE_FILE = "main.cu"
OUTPUT_FILE = "rezultate_laborator2.txt"

TEST_CASES = [
    (10, "seq", [1]),
    (10, "cuda", [4]), 
    (1000, "seq", [1]),
    (1000, "cuda", [2, 4, 8, 16]), 
    (10000, "seq", [1]), 
    (10000, "cuda", [2, 4, 8, 16])
]

def compile_code():
    print(f"Compilare {SOURCE_FILE}...")
    cmd = ["nvcc", SOURCE_FILE, "-o", EXECUTABLE, "-O2"]
    try:
        subprocess.check_call(cmd)
        print("Compilare reusita.\n")
    except subprocess.CalledProcessError:
        print("Eroare la compilare! Verifica instalarea CUDA Toolkit.")
        exit(1)

def run_test(size, mode, param):
    times = []
    for _ in range(10):
        cmd = [f"./{EXECUTABLE}", str(size), mode, str(param)]
        if os.name == 'nt':
            cmd[0] = EXECUTABLE + ".exe"
            
        try:
            result = subprocess.run(cmd, capture_output=True, text=True, check=True)
            ns = int(result.stdout.strip())
            times.append(ns)
        except Exception as e:
            print(f" Eroare: {e}")
            return None

    return statistics.mean(times)

def main():
    compile_code()
    
    with open(OUTPUT_FILE, "w") as f_out:
        header = f"{'Tip':<10} | {'Size':<10} | {'Param(P)':<10} | {'Timp Mediu (ms)':<15}"
        print(header)
        print("-" * 55)
        f_out.write(header + "\n" + "-"*55 + "\n")

        for size, mode, params in TEST_CASES:
            for p in params:
                print(f"Rulare: N={size}, {mode}, P={p} ... ", end="", flush=True)
                
                avg_ns = run_test(size, mode, p)
                
                if avg_ns is not None:
                    avg_ms = avg_ns / 1_000_000.0
                    print(f"Gata. {avg_ms:.4f} ms")
                    line = f"{mode:<10} | {size:<10} | {p:<10} | {avg_ms:<15.4f}"
                    f_out.write(line + "\n")
                else:
                    print("Esec.")

    print(f"\nRezultatele complete sunt in {OUTPUT_FILE}")

if __name__ == "__main__":
    main()