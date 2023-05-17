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

composite(N) :- \+ prime(N).

sorted_primes(_, []).
sorted_primes(MAX_IN_HEAD, [H]) :- MAX_IN_HEAD =< H, prime(H).
sorted_primes(MAX_IN_HEAD, [H1, H2 | T]) :-
    MAX_IN_HEAD =< H1,
    H1 =< H2,
    prime(H1),
    prime(H2),
    sorted_primes(H2, T).

prime_divisors(1, []) :- !.
prime_divisors(N, [N]) :- number(N), prime(N), !.
prime_divisors(N, [H | T]) :-
    number(N),
    min_divisor(N, H),
    N1 is N // H,
    prime_divisors(N1, T).
prime_divisors(N, [H | T]) :-
    \+ number(N), number(H),
    sorted_primes(0, [H | T]),
    prime_divisors(N1, T),
    N is N1 * H.