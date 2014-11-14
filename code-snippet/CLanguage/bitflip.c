#include <stdio.h>
#include <math.h>

char *player[] = { "First Player", "Second Player"};

int powerof2(int n) 
{
    if(n>0) {
        int base = 1;
        base <<= n ;
        return base;
    }

    return 1;
}
 
int beauty(int i)
{
        i = i - ((i >> 1) & 0x55555555);
            i = (i & 0x33333333) + ((i >> 2) & 0x33333333);
                return (((i + (i >> 4)) & 0x0F0F0F0F) * 0x01010101) >> 24;
}

int tryPower(int n) {
    int k;
    int pow;
    int beautiful = beauty(n);

    for(k=sqrtl(n);k>=0;k--) {
        pow = powerof2(k);
        if(pow>n) 
            continue;
        if(beautiful == beauty(n-pow))
            return k;
    }
    return -1;
}

int game(int n,int play)
{
    int k =tryPower(n);

    if (k>-1) {
        return game(n-powerof2(k),play==0?1:0);
    }  else {
        return play==0?1:0;
    }
}

int main(int argc,char *argv[])
{
    int total;
    int n;
    int i;
    int play;

    scanf("%d",&total);

    for(i=0;i<total && scanf("%d",&n)>0;i++) {
        play=game(n,0);
        printf("%s\n",player[play]);
    }

    return 0;
}
