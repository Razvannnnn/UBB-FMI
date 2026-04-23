;; model matematic
;; vector-produs (l1 l2 .. ln, r1 r2 .. rn)
;;    1. 0, daca n = 0 , adica listele sunt goale
;;    2. l1 * r1 (+) vector-produs(l2 .. ln, r2 .. ln)
;;
;; vector-produs(L1 : lista , L2 : lista)

(defun vector-produs (v1 v2)
  (cond
    ((/= (length v1) (length v2)) (error "Vectorii trebuie sa aiba aceeasi dimensiune")) 
    ((null v1) nil)  
    (t (cons (* (car v1) (car v2))  
             (vector-produs (cdr v1) (cdr v2))))))


(defun main (v1 v2)
  (let ((produs (vector-produs v1 v2)))
    (format t "Produsul vectorial este: ~a~%" produs)))
