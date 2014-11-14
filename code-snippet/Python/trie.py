#!/usr/bin/env python

class Trie:
    """ Trie Implementation """

    def __init__(self):
        self.__final = False
        self.__nodes = {}


    def __repr__(self):
       return 'Trie<len={},final={}>'.format(len(self),self.__final)

    def __getstate__(self):
        return self.__final,self.__nodes

    def __setstate__(self,state):
        self.__final,self.__nodes = state

    def __len__(self):
       return len(self.__nodes)

    def __bool__(self):
        return self.__final

    def __contains__(self,array):
        try:
            return self[array]
        except KeyError:
            return False

    def __iter__(self):
       yield self
       for node in self.__nodes.values():
           for ch in node:
               yield ch

    def __get(self,array,create):
        if array:
            head,tail = array[0],array[1:]
            #            return self.__nodes.setdefault(head,Trie()).__get(tail,create)
            if create and head not in self.__nodes:
                self.__nodes[head] = Trie()
            return self.__nodes[head].__get(tail,create)
        return self

    def __getitem__(self,array):
        return self.__get(array,False)

    def create(self,array):
        self.__get(array,True).__final = True

    def __read(self,name):
        if self.__final:
            yield name

        for key,value in self.__nodes.items():
            for out in value.__read(name + [key]):
                yield out

    def read(self):
        for out in self.__read([]):
            yield out

    def update(self,array):
        self[array].__final = True

    def delete(self,array):
        self[array].__final = False

    def prune(self):
        for key,value in self.__nodes.items():
            if not value.prune():
                del self.__nodes[key]

        if not len(self):
            self.delete([])

        return self


def main():
    test_data = [ "dog","cat","donkey","cow","giraffe","bull" ]

    trie=Trie()

    for word in test_data:
        trie.create(word)

    for out in trie.read():
        print ''.join(out)

if __name__ == "__main__":
    main()
