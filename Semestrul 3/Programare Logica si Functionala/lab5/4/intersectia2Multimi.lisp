;; trebuie sa facem intersectia a doua multimi
;;
;; model matematic
;; contains?(element, l1 l2 .. ln):
;;    1. false, n = 0
;;    2. true, daca l1 = element
;;    3. contains?(element, l2 .. ln)
;;
;; contains?(E: element, L : lista)

(defun contains?(element lst)
  (cond
    ((null lst) nil)                    ;; lista e goala
    ((equal element (car lst)) t)       ;; elementul coincide cu primul
    (t (contains? element (cdr lst)))))


;; model matematic
;;
;; intersectie(l1 l2 .. ln , r1 r2 .. rm)
;;    1. [], daca n = 0
;;    2. l1 + intersectie(l2 .. ln , r1 r2 .. rm), daca contains?(l1 , r1 r2 .. rm)
;;    3. intersectie(l2 .. ln, r1 r2 .. rm), altfel
;;
;; intersectie(L1 : lista, L2 : lista

(defun intersectie (lst1 lst2)
  (cond
    ((null lst1) nil)                                 ;; Daca prima lista e goala, returnam lista goala.
    ((contains? (car lst1) lst2)                      ;; Daca elementul din primul set se afla in al doilea set,
     (cons (car lst1) (intersectie (cdr lst1) lst2))) ;; Adauga-l in rezultat si continua cu restul listei.
    (t (intersectie (cdr lst1) lst2))))               ;; Altfel, continua cu restul listei din lst1.
