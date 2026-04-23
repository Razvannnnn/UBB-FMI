set -e

rm -f parser.tab.c parser.tab.h lex.yy.c my_compiler output.asm output.o program

echo "--- 1. Generating Parser & Lexer ---"
bison -d parser.y
flex lexer.l

echo "--- 2. Compiling the Compiler ---"
gcc -o my_compiler lex.yy.c parser.tab.c -lfl

echo "--- 3. Running Compiler on test.txt ---"
./my_compiler test.txt

echo "--- 4. Assembling (NASM) ---"
nasm -f elf64 output.asm -o output.o

echo "--- 5. Linking ---"
gcc output.o -o program -no-pie

echo "--- 6. Executing Result ---"
echo "Enter two numbers (e.g., 5 2) followed by Enter:"
./program