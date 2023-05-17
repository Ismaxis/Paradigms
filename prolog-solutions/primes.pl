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
sorted_primes(MAX_IN_HEAD, [H | T]) :-
    MAX_IN_HEAD =< H, prime(H), sorted_primes(H, T).

prime_divisors(1, []) :- !.
prime_divisors(N, [N]) :- number(N), prime(N), !.
prime_divisors(N, [H | T]) :-                                 %# N -> [H | T]
    number(N),
    min_divisor(N, H),
    N1 is N // H,
    prime_divisors(N1, T), !.
prime_divisors(N, [H | T]) :-                                 %# [H | T] -> N
    \+ number(N),
    sorted_primes(0, [H | T]),
    prime_divisors(N1, T),
    N is N1 * H.

%# ==================================== Compact ====================================
compact([H], [(H, 1)]) :- !.
compact([H | T], [C_H | C_T]) :-
    compact(T, [(Divisor, Power) | C_T]),
    H = Divisor,
    inc((Divisor, Power), C_H), !.
compact([H | T], [(H, 1) | C_T]) :-
     compact(T, C_T).

inc((D, P), (D, P1)) :- P1 is P + 1.

sorted_compact_primes(_, []).
sorted_compact_primes(MAX_IN_HEAD, [(H, _) | T]) :-
    MAX_IN_HEAD =< H, prime(H), sorted_compact_primes(H, T).

compact_prime_divisors(1, []) :- !.
compact_prime_divisors(N, C_D) :-                             %# N -> C_D
    number(N),
    prime_divisors(N, Divisors),
    compact(Divisors, C_D).

compact_prime_divisors(N, [(D, P)]) :-                        %# [(D, P)] -> N
    \+ number(N),
    N is round(D ** P), !.
compact_prime_divisors(N, [(D, P) | T]) :-                    %# [(D, P) | T] -> N
    \+ number(N),
    sorted_compact_primes(0, [(D, P) | T]),
    compact_prime_divisors(N1, T),
    N is N1 * round(D ** P).
