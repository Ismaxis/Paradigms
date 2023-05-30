:- load_library('alice.tuprolog.lib.DCGLibrary').

%# ===== OPERATIONS ======
operation(op_add, A, B, R) :- R is A + B.
operation(op_subtract, A, B, R) :- R is A - B.
operation(op_multiply, A, B, R) :- R is A * B.
operation(op_divide, A, 0, A) :- !.
operation(op_divide, A, B, R) :- R is A / B.
operation(op_negate, A, R) :- R is -A.

op_p(op_add) --> ['+'].
op_p(op_subtract) --> ['-'].
op_p(op_multiply) --> ['*'].
op_p(op_divide) --> ['/'].
op_p(op_negate) --> { atom_chars("negate", C) }, C.

%# ===== VAR-BOOLEAN =====
to_bool(N) :- N > 0.
from_bool(true, 1).
from_bool(false, 0).

operation(op_not, A, 1) :- \+ to_bool(A).
operation(op_not, A, 0) :- to_bool(A).
operation(op_and, A, B, 1) :- to_bool(A), to_bool(B).
operation(op_and, A, B, 0) :- \+ (to_bool(A), to_bool(B)).
operation(op_or, A, B, 1) :- (to_bool(A), !); to_bool(B).
operation(op_or, A, B, 0) :- \+ (to_bool(A); to_bool(B)).
operation(op_xor, A, B, 1) :- (\+ to_bool(A), to_bool(B)); (to_bool(A), \+ to_bool(B)).
operation(op_xor, A, B, 0) :- (to_bool(A), to_bool(B)); (\+ to_bool(A), \+ to_bool(B)).

op_p(op_not) --> ['!'].
op_p(op_and) --> { atom_chars("&&", C) }, C.
op_p(op_or) --> { atom_chars("||", C) }, C.
op_p(op_xor) --> { atom_chars("^^", C) }, C.

%# ===== EVAL =====
evaluate(const(Value), _, Value).
evaluate(variable(Name), Vars, R) :- first_letter(Name, K), lookup(K, Vars, R).
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

all_member([], _).
all_member([H | T], Values) :- member(H, Values), all_member(T, Values).

is_digit(C) :- member(C, ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9']).
is_varname_part(C) :- atom_chars("xyzXYZ", XYZ), member(C, XYZ).

first_letter(S, H) :- atom_chars(S, [H | T]).

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

varname_p([]) --> [].
varname_p([H | T]) --> { is_varname_part(H) }, [H], call_p(T, varname_p).

%# ===== INFIX =====
infix_p(const(Value)) -->
    { nonvar(Value, number_chars(Value, Chars)) },
    number_p(Chars),
    { Chars = [_ | _], number_chars(Value, Chars) }.
infix_p(variable(Name)) -->
    { nonvar(Name, atom_chars(Name, Chars)) },
    varname_p(Chars),
    { Chars = [_ | _], atom_chars (Name, Chars)}.
infix_p(operation(BinOp, A, B)) -->
    ['('], ws_p, infix_p(A), ws_p_plus, op_p(BinOp), ws_p_plus, infix_p(B), ws_p, [')'].
infix_p(operation(UnOp, A)) --> op_p(UnOp), ws_p_plus, infix_p(A).

trim_p(E) --> ws_p, infix_p(E), ws_p.

infix_str(E, A) :- ground(E), phrase(trim_p(E), C), atom_chars(A, C), !.
infix_str(E, A) :- atom(A), atom_chars(A, C), phrase(trim_p(E), C), !.
