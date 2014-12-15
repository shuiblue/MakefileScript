
bar =
foo = $(bar)


A:B C D
	echo a
B: C D
	echo b
C: D
	echo c

ifdef foo
D: E
	echo d1
else
D: F
	echo d2
endif

E: 
	echo e
F:
	echo f

