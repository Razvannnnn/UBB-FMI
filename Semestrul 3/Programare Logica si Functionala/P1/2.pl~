cmmdc(0,B,R):-R = B.
cmmdc(A,0,R):-R = A.
cmmdc(A,B,R):-
    A < B,!,
    C is B/A,
    cmmdc(A,C,R).
cmmdc(A,B,R):-
    A > B,!,
    C is A/B,
    cmmdc(C,B,R).

list_cmmmc(A,R):-list_cmmmc(A,1,R).
list_cmmmc([],C,R):-R is C.
list_cmmmc([H|T],C,R):-
    cmmdc(H,C,R1),
    A = H*C,
    B = A/R1,
    list_cmmc(T,B,R).
