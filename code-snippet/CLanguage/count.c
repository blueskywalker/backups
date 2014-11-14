#include <stdio.h>


int count(unsigned value) {
  int v=value;
  int i;

  for(i=0;v>0;i++) {
    v &= v-1;
  }

  return i;
}

int main(int argc,char *argv[])
{

  unsigned int test [] = { 1,2,5,13,100,200,1500 };
  int size = sizeof(test)/sizeof(unsigned);
  int i;

    for(i=0;i<size;i++) {
      printf("%5d - %4d\n",test[i],count(test[i]));
    }
 
  return 0;
}
