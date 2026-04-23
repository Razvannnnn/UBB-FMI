;; Model matematic
;; adancime (l k):
;;    1. k, daca l este atom
;;    2. max(adancime(subarbore1) U adancime(subarbore2) U ... U adancime(subarboren)), altfel: k=k+1


(defun adancime (l k)
  (cond
    ((atom l) k)
    (t (apply #'max(mapcar #'(lambda (x) (adancime x (+ k 1))) l)))))

;; Model matematic
;; main (l):
;;    1. adancime(l 0)


(defun main(l)
  (adancime l 0))
