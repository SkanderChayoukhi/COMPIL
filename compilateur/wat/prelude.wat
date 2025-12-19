;; Prélude WAT pour le compilateur PCF
;; Machine abstraite avec pile et environnement

;; Mémoire globale pour la pile et l'environnement
(memory 1)

;; Pointeur de pile (stack pointer)
(global $sp (mut i32) (i32.const 0))

;; Pointeur d'environnement (environment pointer)
(global $ep (mut i32) (i32.const 1000))

;; Constantes
(global $STACK_BASE i32 (i32.const 0))
(global $ENV_BASE i32 (i32.const 1000))

;; Push: empiler une valeur sur la pile
(func $push (param $val i32)
  ;; stack[sp] = val
  (i32.store (global.get $sp) (local.get $val))
  ;; sp += 4
  (global.set $sp (i32.add (global.get $sp) (i32.const 4)))
)

;; Pop: dépiler une valeur de la pile
(func $pop (result i32)
  ;; sp -= 4
  (global.set $sp (i32.sub (global.get $sp) (i32.const 4)))
  ;; return stack[sp]
  (i32.load (global.get $sp))
)

;; Pushenv: sauvegarder l'environnement actuel
(func $pushenv
  (call $push (global.get $ep))
)

;; Popenv: restaurer l'environnement sauvegardé
(func $popenv
  (global.set $ep (call $pop))
)

;; Extend: ajouter une valeur à l'environnement
(func $extend (param $val i32)
  ;; env[ep] = val
  (i32.store (global.get $ep) (local.get $val))
  ;; ep += 4
  (global.set $ep (i32.add (global.get $ep) (i32.const 4)))
)

;; Search: chercher une valeur dans l'environnement par son indice
(func $search (param $index i32) (result i32)
  (local $addr i32)
  ;; addr = ep - (index + 1) * 4
  (local.set $addr
    (i32.sub
      (global.get $ep)
      (i32.mul
        (i32.add (local.get $index) (i32.const 1))
        (i32.const 4)
      )
    )
  )
  ;; return env[addr]
  (i32.load (local.get $addr))
)

