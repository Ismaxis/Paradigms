get_key(node(Key, _, _, _, _), Key).
get_value(node(_, Value, _, _, _), Value).
get_left(node(_, _, Left, _, _), Left).
get_right(node(_, _, _, Right, _), Right).
get_height(node(_ , _, _, _, Height), Height).
get_height(empty_map, 0).

max(L, R, R) :- L =< R.
max(L, R, L) :- R < L.
calc_height(Left, Right, R) :- get_height(Left, HL), get_height(Right, HR), max(HL, HR, M), R is M + 1.
calc_height_and_set(node(K, V, Left, Right, _), node(K, V, Left, Right, Height)) :- calc_height(Left, Right, Height).

get_balance(node(_, _, Left, Right, _), R) :- get_height(Left, HL), get_height(Right, HR), R is HL - HR.

%# ===== BUILD =====
map_build([], empty_map) :- !.
map_build([(K, V) | T], R) :-
    map_build(T, R1),
    map_put(R1, K, V, R).

%# ===== GET =====
map_get(node(K, V, _, _, _), K, V) :- !.
map_get(node(K1, _, Left, _, _), K, V) :-
    K < K1, !,
    map_get(Left, K, V).
map_get(node(K1, _, _, Right, _), K, V) :-
    K > K1,
    map_get(Right, K, V).

%# ===== PUT =====
put_cond(KL, K) :- K < KL.
put_cond(KR, K) :- K > KR.

put(empty_map, K, V, _, node(K, V, empty_map, empty_map, 1)).
put(node(K, VOld, Left, Right, Height), K, VNew, [_, Update], node(K, VResult, Left, Right, Height)) :-
    G =.. [Update, VOld, VNew, VResult],
    call(G).
put(node(KN, VN, Left, Right, H), K, V, [Recur, _], R) :-
    K < KN, !,
    G =.. [Recur, Left, K, V, L1],
    call(G),
    check_for_disbalance(node(KN, VN, L1, Right, H), K, [get_key, put_cond], R).
put(node(KN, VN, Left, Right, H), K, V, [Recur, _], R) :-
    K > KN, !,
    G =.. [Recur, Right, K, V, R1],
    call(G),
    check_for_disbalance(node(KN, VN, Left, R1, H), K, [get_key, put_cond], R).

take_new(_, V, V).
map_put(Node, K, V, R) :- put(Node, K, V, [map_put, take_new], R).

%# ===== PUT-IF-ABSENT =====
take_old(V, _, V).
map_putIfAbsent(Node, K, V, R) :- put(Node, K, V, [map_putIfAbsent, take_old], R).

%# ===== REMOVE =====
remove_cond(B, _) :- 0 =< B.
remove_cond(B, _) :- 0 >= B.

map_remove(empty_map, _, empty_map) :- !.
map_remove(node(K, _, Left, empty_map, _), K, Left) :- !.
map_remove(node(K, _, empty_map, Right, _), K, Right) :- !.
map_remove(node(KN, _, Left, Right, _), K, R) :-
    KN = K,
    min_node_in_subtree(Right, NextK, NextV, NewRight),
    calc_height_and_set(node(NextK, NextV, Left, NewRight, _), R).
map_remove(node(KN, VN, Left, Right, H), K, R) :-
    K < KN, 
    map_remove(Left, K, L1),
    check_for_disbalance(node(KN, VN, L1, Right, H), _, [get_balance, remove_cond], R).
map_remove(node(KN, VN, Left, Right, H), K, R) :-
    K > KN, 
    map_remove(Right, K, R1),
    check_for_disbalance(node(KN, VN, Left, R1, H), _, [get_balance, remove_cond], R).

%# ===== BALANCE =====
check_for_disbalance(Node, K, Fs, R) :-
    get_balance(Node, B),
    B > 1,
    left_case(Node, K, Fs, R), !.
check_for_disbalance(Node, K, Fs, R) :-
    get_balance(Node, B),
    B < -1,
    right_case(Node, K, Fs, R), !.
check_for_disbalance(Node, _, _, R) :-
    get_balance(Node, B),
    -1 =< B, B =< 1,
    calc_height_and_set(Node, R).

left_case(node(KN, V, Left, Right, Height), K, [Get, Cond], R) :-
    G =.. [Get, Left, GetR], call(G),
    C =.. [Cond, GetR, K],
    (call(C),
        right_rotate(node(KN, V, Left, Right, Height), R); 
        left_rotate(Left, RotatedLeft), right_rotate(node(KN, V, RotatedLeft, Right, Height), R)).

right_case(node(KN, V, Left, Right, Height), K, [Get, Cond], R) :-
    G =.. [Get, Right, GetR], call(G),
    C =.. [Cond, GetR, K],
    (call(C), 
        left_rotate(node(KN, V, Left, Right, Height), R);
        right_rotate(Right, RotatedRight), left_rotate(node(KN, V, Left, RotatedRight, Height), R)).

left_rotate(node(KZ, VZ, T1, node(KY, VY, T2, X, H1), _), 
            node(KY, VY, node(KZ, VZ, T1, T2, H2), X, H3)) :- 
            calc_height(T1, T2, H2),
            calc_height(node(_, _, _, _, H2), X, H3).

right_rotate(node(KZ, VZ, node(KY, VY, X, T3, H1), T4, _), 
            node(KY, VY, X, node(KZ, VZ, T3, T4, H2), H3)) :-
            calc_height(T3, T4, H2),
            calc_height(X, node(_, _, _, _, H2), H3).

is_left_empty_map(node(_, _, empty_map, _, _)).

min_node_in_subtree(node(K, V, empty_map, Right, H), K, V, Right) :- !. 
min_node_in_subtree(node(K, V, Left, Right, _), KL, VL, R) :- 
    is_left_empty_map(Left), !,
    get_key(Left, KL), get_value(Left, VL), get_right(Left, NewLeft),
    calc_height_and_set(node(K, V, NewLeft, Right, _), R).
min_node_in_subtree(node(KN, VN, Left, Right, _), K, V, R) :- 
    min_node_in_subtree(Left, K, V, NewLeft), 
    calc_height_and_set(node(KN, VN, NewLeft, Right, _), R).
