#include <stdio.h>
#include <stdlib.h>
#include <memory.h>

char            *primeTable;
int		size = 0;

int isPrime0(int N)
{
    int i;

    for(i=2;i<N;i++)
        if(N%i==0)
            return 0;

    return 1;
}

int 
isPrime1(int N)
{
	int		i;
	if (N == 1)
		return 0;

    if (N%2==0)
        return 0;

	for (i = 3; i*i <= N; i+2) {
		if (N % i == 0)
			return 0;
	}
	return 1;
}

int
initPrime(int N)
{
    int i,j;
    for(i=2;i<N;i++) {
        if(primeTable[i]) {
            for(j=2;i*j<N;j++) {
                primeTable[i*j]=0;
            }
        }
    }
}

int
isPrime(int N) {
    return primeTable[N];
}

int 
getNumberOfPrimes(int N)
{
	int		i;
	int		count = 0;

	primeTable = (char*)malloc(N);
	size = 0;

    memset((void*)primeTable,1,N);
    primeTable[0]=0;
    primeTable[1]=0;
    initPrime(N);

	for (i = 1; i < N; i++) {
		if (isPrime(i))
			count++;
	}

	return count;
}

int main() {

    printf("%d\n",getNumberOfPrimes(78889));
    return 0;
}
