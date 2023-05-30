:- load_library('alice.tuprolog.lib.DCGLibrary').

%# ===== OPERATIONS ======
operation(op_add, A, B, R) :- R is A + B.
operation(op_subtract, A, B, R) :- R is A - B.
operation(op_multiply, A, B, R) :- R is A * B.
operation(op_divide, A, B, R) :- R is A / B.
operation(op_negate, A, R) :- R is -A.

op_p(op_add) --> ['+'].
op_p(op_subtract) --> ['-'].
op_p(op_multiply) --> ['*'].
op_p(op_divide) --> ['/'].
op_p(op_negate) --> { atom_chars("negate", C) }, C.

%# ===== EVAL =====
evaluate(const(Value), _, Value).
evaluate(variable(Name), Vars, R) :- lookup(Name, Vars, R).
evaluate(operation(UnOp, A), Vars, R) :- evaluate(A, Vars, AV), operation(UnOp, AV, R).
evaluate(operation(Op, A, B), Vars, R) :-
    evaluate(A, Vars, AV),
    evaluate(B, Vars, BV),
    operation(Op, AV, BV, R).

%# ===== HELPERS =====
lookup(K, [(K, V) | _], V).
lookup(K, [_ | T], V) :- lookup(K, T, V).

nonvar(V, _) :- var(V).
nonvar(V, T) :- nonvar(V), call(T).

is_digit(C) :- member(C, ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9']).

number_p([]) --> [].
number_p(L) --> negative_p(L).
number_p(L) --> int_p(L).

negative_p(L) --> valve_p(L, '-', int_p).

int_p([]) --> [].
int_p(L) --> valve_p(L, '.', frac_p).
int_p(L) --> digits_p(L, int_p).

frac_p([]) --> [].
frac_p(L) --> digits_p(L, frac_p).

valve_p([H | T], Symbol, Next) --> { H = Symbol, T = [_ | _]}, [H], call_p(T, Next).

digits_p([H | T], Next) --> { is_digit(H) }, [H], call_p(T, Next).

call_p(T, Next) --> { G =.. [Next, T] }, G.

plus_p(S, Next) --> [S], Next.
ws_p_plus --> plus_p(' ', ws_p).
ws_p --> [].
ws_p --> [' '], ws_p.

%# ===== INFIX =====
infix_p(variable(Name)) --> [Name], {member(Name, [x,y,z])}.
infix_p(const(Value)) -->
    { nonvar(Value, number_chars(Value, Chars)) },
    number_p(Chars),
    { Chars = [_ | _], number_chars(Value, Chars) }.
infix_p(operation(BinOp, A, B)) -->
    ['('], ws_p, infix_p(A), ws_p_plus, op_p(BinOp), ws_p_plus, infix_p(B), ws_p, [')'].
infix_p(operation(UnOp, A)) --> op_p(UnOp), ws_p_plus, infix_p(A).

trim_p(E) --> ws_p, infix_p(E), ws_p.

infix_str(E, A) :- ground(E), phrase(trim_p(E), C), atom_chars(A, C), !.
infix_str(E, A) :- atom(A), atom_chars(A, C), phrase(trim_p(E), C), !.
