% Sorteaza si elimina dublurile
sort_no_dup([], []). % Lista vida ramane vida
sort_no_dup([H|T], R) :-
    sort_no_dup(T, R1), % Sorteaza recursiv restul listei
    insert_no_dup(R1, H, R). % Insereaza fara dubluri

% Insereaza un element intr-o lista sortata, fara dubluri
insert_no_dup([], X, [X]). % Adauga elementul intr-o lista vida
insert_no_dup([H|T], X, [H|R]) :-
    X > H, !, % Continua recursiv daca X > H
    insert_no_dup(T, X, R).
insert_no_dup([H|T], X, [H|T]) :-
    X =:= H, !. % Ignora daca X este deja in lista
insert_no_dup([H|T], X, [X,H|T]) :-
    X < H, !. % Insereaza X inaintea lui H
