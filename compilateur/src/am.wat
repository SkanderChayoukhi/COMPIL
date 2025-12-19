(module
  (memory 1 10) ;; min max memory (number of 64K pages)
  (global $HEAP (mut i32) (i32.const 0)) ;; heap pointer initialized to 0
  (global $ENV  (mut i32) (i32.const 0)) ;; env pointer initialized to NIL
  (global $ACC  (mut i32) (i32.const 999)) ;; accumulator initialized to 999
  (global $LIST i32 (i32.const 1))       ;; LIST tag (for non empty lists)
  (global $NIL  i32 (i32.const 0))       ;; NIL tag (for empty lists)

  ;; table of closure bodies
  (table funcref
    (elem
      $eleven ;; index 0
    )
  )
  ;; stores a pair on the heap and returns a pointer to the pair
  ;; final state (initially result = HEAP):
  ;;             ----------
  ;;   result -> | first  |
  ;;             ----------
  ;;             | second |
  ;;             ----------
  ;;    HEAP   ->
  (func $pair (param $first i32) (param $second i32) (result i32)
    (local $result i32)
    ;; result = HEAP
    (local.set $result (global.get $HEAP))
    ;; [HEAP] = first
    (i32.store (global.get $HEAP) (local.get $first))
    ;; HEAP = HEAP + 4 (ie the 4 bytes of first)
    (global.set $HEAP (i32.add (global.get $HEAP) (i32.const 4)))
    ;; [HEAP] = second
    (i32.store (global.get $HEAP) (local.get $second))
    ;; HEAP = HEAP + 4 (ie the 4 bytes of first)
    (global.set $HEAP (i32.add (global.get $HEAP) (i32.const 4)))
    ;; return result
    (local.get $result)
    return)
  ;; stores a cons on the heap and returns a pointer to the cons
  ;; a cons is stored as a block of 3 words: a LIST tag, the head and the tail
  ;;             ----------
  ;;   result -> | LIST   |
  ;;             ----------
  ;;             | head   |
  ;;             ----------
  ;;             | tail   |
  ;;             ----------
  ;;    HEAP   ->
(func $cons (param $head i32) (param $tail i32) (result i32)
    (local $result i32)
    (local.set $result (global.get $HEAP))
    (i32.store (global.get $HEAP) (global.get $LIST))
    (global.set $HEAP (i32.add (global.get $HEAP) (i32.const 4)))
    (i32.store (global.get $HEAP) (local.get $head))
    (global.set $HEAP (i32.add (global.get $HEAP) (i32.const 4)))
    (i32.store (global.get $HEAP) (local.get $tail))
    (global.set $HEAP (i32.add (global.get $HEAP) (i32.const 4)))
    (local.get $result)
    return)
  ;; returns the head of a list, i.e. [list + 4]
  (func $head (param $list i32) (result i32)
    (i32.load (i32.add (local.get $list (i32.const 4))))
    return)
  ;; returns the head of a list, i.e. [list + 8]
  (func $tail (param $list i32) (result i32)
    (i32.load (i32.add (local.get $list (i32.const 8))))
    return)
  ;; retrieves the element $n of the list $list (starting from 0)
  ;; precondition: the size of the list is greater than $n
  (func $search (param $n i32) (param $list i32) (result i32)
    (local.get $n)
    (if (result i32)
      (then            ;; n is non zero
       ;; push n-1
       (i32.sub (local.get $n) (i32.const 1))
       ;; push list
       (local.get $list)
       ;; tail(list) (pops list, n-1 is on top of the stack)
       (call $tail)
       ;; tail is on top of the stack, with n-1 below
       ;; search(n-1, tail)
       (call $search))
      (else            ;; n is zero
       ;; push list
       (local.get $list)
       ;; call head(list)
       (call $head)))
    return)
  ;; applies a closure
  ;; its parameters are
  ;;   $W : the value of the argument
  ;;   $C : the closure, a pointer to a pair (i, e)
  (func $apply (param $W i32)(param $C i32)(result i32)
      (local $e i32) ;; the environment e stored in the closure
      (local.get $W) ;; element 0 of the environment
      (local.get $C) ;; element 1 of the environment
    ;; retrieve the environment in the closure (2nd element of a pair)
      (local.set $e (i32.load (i32.add (local.get $C)(i32.const 4))))
    ;; extend the environment e to <W, <C, e>>
      (local.get $e)
      (call $cons)
      (call $cons)
      (global.set $ENV)
    ;; retrieve index of closure body and execute the body
    (call_indirect (result i32) (i32.load (local.get $C)))
  )

  ;; for testing purposes
  (func $eleven (result i32)
    (i32.const 11)
    (return)
  )

  (func (export "main") (result i32)
    (i32.const 1)
    (i32.const 123) ;; element 0
    (i32.const 246) ;; element 1
    (global.get $NIL)
    (call $cons)
    (call $cons)
    (call $search)
    (global.get $ENV)
    (call $cons)
    (global.get $ENV)
    (global.set $ENV)
    (call $head)
    return)

  ;; should return 11
  (func (export "test_indirect") (result i32)
    (call_indirect (result i32)(i32.const 0)))


  (func (export "test_heap") (result i32)
    (global.get $HEAP)
    (return))

  ;; should return 11
  (func (export "test_apply") (result i32)
    ;; dummy W
    (i32.const 0)
    ;; MkClos (0, $ENV)
    (i32.const 0)
    (global.get $ENV)
    (call $pair)
    ;; test apply
    (call $apply)
    return)
)