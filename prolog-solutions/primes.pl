init(MAX_N) :- SQ is round(sqrt(MAX_N)), add(2, SQ, MAX_N).

add(R, R, _) :- !.
add(C, SQ, MAX) :-
    g(C, C, MAX),
    C1 is C + 1,
    add(C1, SQ, MAX).

set_if_not(V, _) :- min_divisor(V, _), !.
set_if_not(V, Divisor) :- assertz(min_divisor(V, Divisor)).

g(_, N, MAX) :-
    N > MAX, !.
g(C, _, _) :- min_divisor(C, Val), C \= Val, !.
g(C, N, MAX) :-
    N =< MAX,
    set_if_not(N, C),
    N1 is N + C,
    g(C, N1, MAX).

prime(N) :- min_divisor(N, D), D = N.
prime(N) :- \+ min_divisor(N, _).
