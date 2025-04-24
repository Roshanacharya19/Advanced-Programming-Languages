% Basic family relationships
% parent(Parent, Child) - Parent is a parent of Child
% male(Person) - Person is male
% female(Person) - Person is female

% Define family members by gender
male(roshan).
male(hari).
male(shyam).
male(krishna).
male(ram).
male(gopal).
male(mohan).

female(sita).
female(geeta).
female(radha).
female(saraswati).
female(parvati).
female(laxmi).
female(sarita).

% Define parent relationships
parent(roshan, hari).
parent(roshan, radha).
parent(sita, hari).
parent(sita, radha).

parent(hari, shyam).
parent(hari, parvati).
parent(geeta, shyam).
parent(geeta, parvati).

parent(radha, krishna).
parent(radha, sarita).
parent(ram, krishna).
parent(ram, sarita).

parent(shyam, mohan).
parent(saraswati, mohan).

parent(parvati, laxmi).
parent(parvati, gopal).
parent(krishna, laxmi).
parent(krishna, gopal).

% Derived relationships using rules

% father(Father, Child) - Father is a father of Child
father(Father, Child) :- parent(Father, Child), male(Father).

% mother(Mother, Child) - Mother is a mother of Child
mother(Mother, Child) :- parent(Mother, Child), female(Mother).

% grandparent(Grandparent, Grandchild) - Grandparent is a grandparent of Grandchild
grandparent(Grandparent, Grandchild) :- parent(Grandparent, Parent), parent(Parent, Grandchild).

% sibling(Person1, Person2) - Person1 and Person2 are siblings
sibling(Person1, Person2) :- parent(Parent, Person1), parent(Parent, Person2), Person1 \= Person2.

% Full siblings have the same father and mother
full_sibling(Person1, Person2) :- 
    father(Father, Person1), father(Father, Person2),
    mother(Mother, Person1), mother(Mother, Person2),
    Person1 \= Person2.

% cousin(Person1, Person2) - Person1 and Person2 are cousins
cousin(Person1, Person2) :- 
    parent(Parent1, Person1), 
    parent(Parent2, Person2), 
    sibling(Parent1, Parent2),
    Person1 \= Person2.

% ancestor(Ancestor, Person) - Ancestor is an ancestor of Person (recursive)
ancestor(Ancestor, Person) :- parent(Ancestor, Person).
ancestor(Ancestor, Person) :- parent(Ancestor, Intermediate), ancestor(Intermediate, Person).

% descendant(Descendant, Person) - Descendant is a descendant of Person (recursive)
descendant(Descendant, Person) :- parent(Person, Descendant).
descendant(Descendant, Person) :- parent(Person, Intermediate), descendant(Descendant, Intermediate).