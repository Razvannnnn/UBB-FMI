#!/bin/bash

./lexer < "$1" > tokens.txt
if [ $? -ne 0 ]; then
    echo "Lexerul a detectat erori. Analiza s-a oprit."
    exit 1
fi

python3 tokenizer_flex.py < tokens.txt
