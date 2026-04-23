;; model matematic
;; contains?(element, l1 l2 .. ln):
;;    1. false, n = 0
;;    2. true, daca l1 = element
;;    3. contains?(element, l2 .. ln)
;;
;;  contains?(E: element, L : lista

(defun contains?(element lst)
 (cond
    ((null lst) nil)                    ;; lista e goala
    ((equal element (car lst)) t)       ;; elementul coincide cu primul
    (t (contains? element (cdr lst))))) ;; verifica restul listei



;; model matematic
;; unique-elements(l1 l2 .. ln)
;;    1. [] , daca n = 0
;;    2. l1 (+) unique-elements(l2 .. ln) , daca contains?(l1, l2 .. ln)
;;    3. unique-elements(l2 .. ln), altfel
;; unique-elements(L : lista)

(defun unique-elements (lst)
  (cond
    ((null lst) nil)                                   ;; Daca lista e goala, returnam o lista goala
    ((contains? (car lst) (cdr lst))                   ;; Daca primul element este in restul listei,
     (unique-elements (cdr lst)))                      ;; il ignoram si continuam cu restul.
    (t (cons (car lst) (unique-elements (cdr lst)))))) ;; Adaugam elementul la rezultat.



;; functia asta adauga un element in lista sortata
;;
;; model matematic
;; insert-sorted(element, l1 l2 .. ln)
;;    1. [element], daca n = 0
;;    2. element (+) [l2 .. ln] , daca element <= l1
;;    3. insert-sorted(element, l2 .. ln)
;; insert-sorted(E : element, L : lista)

(defun insert-sorted (element lst)
  (cond
    ((null lst) (list element))                              ;; Daca lista e goala, returnam o lista cu elementul
    ((<= element (car lst)) (cons element lst))              ;; daca elementul e mai mic sau egal cu primul, il punem in fata
    (t (cons (car lst) (insert-sorted element (cdr lst)))))) ;; continuam recursiv cu restul listei


;; model matematic
;; sort-list(l1 l2 .. ln)
;;    1. [], daca n = 0
;;    2. insert-sorted(l1 , sort-list(l2 .. ln)) , altfel
;; sort-list(L : lista

(defun sort-list (lst)
 (cond
   ((null lst) nil) ;; daca lista e goala returnam lista goala
    (t (insert-sorted (car lst) (sort-list (cdr lst))))))
 
(defun main (lst)
  (sort-list ( unique-elements lst)))
