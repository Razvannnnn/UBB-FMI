;; Model matematic
;; preorder(tree):
;;    1. nil, daca tree este vid
;;    2. nod U preorder(subarbore1) U preorder(subarbore2) U ... U preorder(subarboren), altfel: nod = car(tree), subarborei = cdr(tree)
;; 

(defun preorder (tree)
  (if (null tree)
      ;; Arborele este vid => returneaza nil
      nil
      ;; Nodul curent + apel recursiv pe subarbori
      (cons (car tree)                ;; Nodul curent
            (mapcan #'preorder        ;; Aplica preorder pe fiecare subarbore
                    (cdr tree)))))     ;; Subarborii sunt restul listei

