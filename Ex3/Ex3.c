#include <stdio.h>
#include <stdlib.h>
#include <math.h>

// [0.0 1.0]の範囲で乱数を生成する
double rand_normal()
{
  return (double)rand()/RAND_MAX;
}

// [a b]の範囲の乱数を実数で得る
double rand_range(double a, double b)
{
  return a + (b - a) * rand_normal();
}

int main()
{
  FILE *fo;
  char *fname = "data.csv";
  if ((fo=fopen(fname,"w")) == NULL ) {
    printf("File[%s] dose not open!! ¥n",fname);
    exit(0);
  }

  int i, n=200;
  double x,y,r;
  srand(149);

  for(i=0; i<n; i++) {
    do {
      x = rand_range(-1,1);
      y = rand_range(-1,1);
      r = x*x + y*y;
    } while(r > 1);
    fprintf(fo, "%f,%f\n", x,y);
  }

  return 0;
}
