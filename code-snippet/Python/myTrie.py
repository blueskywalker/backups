#!/usr/bin/env python

class mytrie(dict):
    def __init__(self):
        self.isDone=False

    def insert(self,input_str):
        if not input_str:
            self.isDone = True
            return

        head,tail = input_str[:1],input_str[1:]
        if head not in self:
            self[head] = mytrie()

        self[head].insert(tail)

    def travel(self,path=[]):
        ret=[]
        if self.isDone:
            ret.append(''.join(path))

        for key in self:
            ret += self[key].travel(path + [key])

        return ret


    def travel1(self,path=[]):
        if self.isDone:
           yield ''.join(path)

        for key in self:
            for item in self[key].travel1(path + [key]):
                yield item


    def retrieve(self,prefix):
        trie=self
        for key in prefix:
            if key in trie:
                trie=trie[key]
            else:
                print prefix[:prefix.find(key)]

        candidates=trie.travel()
        for candidate in candidates:
           yield prefix + candidate



ac=mytrie()

ac.insert('cat')
ac.insert('catch')
ac.insert('cold')
ac.insert('cut')
ac.insert('cry')

for item in ac.retrieve('cu'):
    print item
