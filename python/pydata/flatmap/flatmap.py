

data = [["a"], ["b",["a"]], ["c",["b",["a"]]], ["d",["c",["b",["a"]]]],"e"]

print data


def flatmap(input):
    ret =[]
    for elem in input:
        ret += flatmap(elem) if type(elem) is list else elem
    return ret

print flatmap(data)