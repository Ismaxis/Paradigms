:- load_library('alice.tuprolog.lib.DCGLibrary').

lookup(K, [(K, V) | _], V).
lookup(K, [_ | T], V) :- lookup(K, T, V).

nonvar(V, _) :- var(V).
nonvar(V, T) :- nonvar(V), call(T).

is_digit(C) :- member(C, ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9']). 

operation(op_add, A, B, R) :- R is A + B.
operation(op_subtract, A, B, R) :- R is A - B.
operation(op_multiply, A, B, R) :- R is A * B.
operation(op_divide, A, B, R) :- R is A / B.
operation(op_negate, A, R) :- R is -A.

evaluate(const(Value), _, Value).
evaluate(variable(Name), Vars, R) :- lookup(Name, Vars, R).
evaluate(operation(UnOp, A), Vars, R) :- evaluate(A, Vars, AV), operation(UnOp, AV, R).
evaluate(operation(Op, A, B), Vars, R) :- 
    evaluate(A, Vars, AV), 
    evaluate(B, Vars, BV), 
    operation(Op, AV, BV, R).

expr_p(variable(Name)) --> [Name], {member(Name, [x,y,z])}.
expr_p(const(Value)) --> 
        { nonvar(Value, number_chars(Value, Chars)) },
        digits_p(Chars),
        { Chars = [_ | _], number_chars(Value, Chars) }.
expr_p(operation(Op, A, B)) -->
    ['('], ws_p, expr_p(A), ws_p_plus, op_p(Op), ws_p_plus, expr_p(B), ws_p, [')'].
expr_p(operation(UnOp, A)) --> op_p(UnOp), ws_p_plus, expr_p(A).

digits_p([]) --> [].
digits_p(L) --> negative_p(L).
digits_p(L) --> int_p(L).

negative_p(L) --> border_p(L, '-', int_p).

int_p([]) --> [].
int_p(L) --> recur_p(L, int_p).
int_p(L) --> border_p(L, '.', frac_p).

frac_p([]) --> [].
frac_p(L) --> recur_p(L, frac_p).

border_p([H | T], Symbol, Next) --> 
        { H = Symbol, T = [_ | _]}, [H], { G =.. [Next, T] }, G.
		
recur_p([H | T], Next) --> { is_digit(H) }, [H], { G =.. [Next, T] }, G. 


ws_p_plus --> [' '], ws_p.
ws_p --> [].
ws_p --> [' '], ws_p.

op_p(op_add) --> ['+'].
op_p(op_subtract) --> ['-'].
op_p(op_multiply) --> ['*'].
op_p(op_divide) --> ['/'].
op_p(op_negate) --> { atom_chars("negate", C) }, C.

wrapper_p(E) --> ws_p, expr_p(E), ws_p.

infix_str(E, A) :- ground(E), phrase(wrapper_p(E), C), atom_chars(A, C), !.
infix_str(E, A) :- atom(A), atom_chars(A, C), phrase(wrapper_p(E), C), !.
