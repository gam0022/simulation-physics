#include <stdio.h>
#include <stdlib.h>
#include <math.h>

// [0.0 1.0]の範囲で乱数を生成する
double rand_normal()
{
  return (double)rand()/RAND_MAX;
}

// n点でモンテカルロ法を用いてπを計算する
double MonteCarlo(int n)
{
  double x,y,r;
  int cnt = 0;
  int i;

  for(i=0; i<n; i++) {
    x = rand_normal();
    y = rand_normal();
    r = x*x + y*y;
    if (r<=1) cnt++;
  }
  return 4.0*cnt/n;
}

#define repeat 10

int main()
{
  printf("面積の平均値,(PI-平均値)/PI\n");
  int n[] = {100, 1000, 10000};
  int i,j;

  for(i=0; i<3; i++) {
    srand(149);
    double pi = 0;
    for(j=0; j<repeat; j++) {
      pi += MonteCarlo(n[i]);
    }
    pi /= repeat;
    double q = (pi - M_PI)/M_PI;
    printf("%f,%f\n",pi,q);
  }

  return 0;
}
