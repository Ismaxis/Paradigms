%# ==================================== Init ====================================
init(MAX_N) :- SQ is round(sqrt(MAX_N)), sieve(2, SQ, MAX_N).

sieve(R, R, _) :- !.
sieve(V, SQ, MAX_N) :-
    set_to_all_multiples(V, V, MAX_N),
    V1 is V + 1,
    sieve(V1, SQ, MAX_N).

set_if_not(M, _) :- min_divisor(M, _), !.
set_if_not(M, D) :- assertz(min_divisor(M, D)).

set_to_all_multiples(_, N, MAX_N) :- N > MAX_N, !.
set_to_all_multiples(V, _, _) :- min_divisor(V, D), V \= D, !.
set_to_all_multiples(V, N, MAX_N) :-
    N =< MAX_N,
    set_if_not(N, V),
    N1 is N + V,
    set_to_all_multiples(V, N1, MAX_N).

%# ==================================== Base ====================================
prime(N) :- min_divisor(N, D), N = D.
prime(N) :- \+ min_divisor(N, _).

composite(N) :- \+ prime(N).

sorted_primes(_, []).
sorted_primes(P, [H | T]) :-
    P =< H, prime(H), sorted_primes(H, T).

prime_divisors(1, []) :- !.
prime_divisors(N, [N]) :- number(N), prime(N), !.
prime_divisors(N, [H | T]) :-                                 %# N -> [H | T]
    number(N),
    min_divisor(N, H),
    N1 is N // H,
    prime_divisors(N1, T).
prime_divisors(N, [H | T]) :-                                 %# [H | T] -> N
    \+ number(N),
    sorted_primes(0, [H | T]),
    prime_divisors(N1, T),
    N is N1 * H.

%# ==================================== Compact ====================================
compact([H], [(H, 1)]) :- !.
compact([H | T], [C_H | C_T]) :-
    compact(T, [(D, P) | C_T]),
    H = D, inc((D, P), C_H), !.
compact([H | T], [(H, 1) | C_T]) :- compact(T, C_T).

inc((D, P), (D, P1)) :- P1 is P + 1.

sorted_compact_primes(_, []).
sorted_compact_primes(P, [(H, _) | T]) :-
    P =< H, prime(H), sorted_compact_primes(H, T).

compact_prime_divisors(1, []) :- !.
compact_prime_divisors(N, C_D) :-                             %# N -> C_D
    number(N),
    prime_divisors(N, D),
    compact(D, C_D).
compact_prime_divisors(N, [(D, P) | T]) :-                    %# [(D, P) | T] -> N
    \+ number(N),
    sorted_compact_primes(0, [(D, P) | T]),
    compact_prime_divisors(N1, T),
    N is N1 * round(D ** P).
