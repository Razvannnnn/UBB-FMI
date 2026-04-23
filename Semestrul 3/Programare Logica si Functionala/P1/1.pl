nrinlista([],_):-false.
nrinlista([X|_],X):-true.
nrinlista([_|T],X):-nrinlista(T,X).


diferenta([H|T], L2, R):-
    nrinlista(L2,H),
    diferenta(T,L2,R).
diferenta([H|T], L2, R):-
    diferenta(T,L2,R1),
    R = [H|R1].


adauga1([],[]).
adauga1([H|T], R):-
    0 is mod(H,2),!,
    adauga1(T, R1),
    R = [H,1|R1].
adauga1([H|T], [H|R] ):-
    adauga1(T, R).
