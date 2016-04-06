#!/usr/bin/env python

cons = lambda el, lst: (el, lst)
mklist = lambda *args: reduce(lambda lst, el: cons(el, lst), reversed(args), None)
car = lambda lst: lst[0] if lst else lst
cdr = lambda lst: lst[1] if lst else lst
nth = lambda n, lst: nth(n-1,cdr(lst)) if n > 0 else car(lst)
