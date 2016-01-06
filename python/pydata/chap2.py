#!/usr/bin/env python

import json

def load_data():
    data_path ='ch02/usagov_bitly_data2012-03-16-1331923249.txt'
    records = [json.loads(line) for line in open(data_path)]
    return records

#print records[0]['tz']

def get_timezone():
    return [rec['tz'] for rec in records if 'tz' in rec]

#print time_zones[:10]

def get_counts(sequence):
    counts = {}
    for x in sequence:
        if x in counts:
            counts[x] += 1
        else:
            counts[x] = 1
    return counts


from collections import defaultdict
def get_count2(sequence):
    counts = defaultdict(int)
    for x in sequence:
        counts[x] += 1
    return counts

#counts = get_count2(time_zones)

#print counts['America/New_York']
#print len(time_zones)

def top_counts(count_dict,n=10):
    value_key_pairs = [ (count,tz) for tz, count in count_dict.items() ]
    value_key_pairs.sort()
    return value_key_pairs[-n:]

#print top_counts(counts)

from collections import Counter
#counts= Counter(get_timezone())
#print counts.most_common(10)

%matplotlib osx

from __future__ import  division
from numpy.random import randn
import  numpy as np
import  os
import matplotlib.pyplot as plt
import pandas as pd

from pandas import DataFrame,Series
import pandas as pd

def panda_timezone_count():
    frame = DataFrame(load_data())
    #print frame['tz'][:10]
    #print frame['tz'].value_counts()[:10]
    clean_tz= frame['tz'].fillna('Missing')
    clean_tz[clean_tz == ''] = 'Unknow'
    tz_counts = clean_tz.value_counts()
    #tz_counts[:10]
    plt.figure(figsize=(10,4))
    tz_counts[:10].plot(kind='barh',rot=0)

panda_timezone_count()