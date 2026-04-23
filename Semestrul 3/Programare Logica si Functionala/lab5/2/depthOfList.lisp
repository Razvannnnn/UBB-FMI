;; model matematic
;; depth (l1 .. ln, currentMax) lista eterogena
;;    1. 1, daca l1 e atom , numar 
;;    2. max(currentMax, 1 + depth(l1)) , daca l1 e lista 
;;    3. depth(l2 .. ln, currentMax), altfel


(defun depth (lst)
  (if (listp lst)
      (+ 1 (reduce #'max (mapcar #'depth lst) :initial-value 0))
      0))

(defun main ()
  (let ((test-list '((1 2) (3 (4 5)) 6)))
    (format t "The depth of the list ~a is: ~d~%" test-list (depth test-list))))

(main)
