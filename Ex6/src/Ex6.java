import java.util.Random;

public class Ex6 {
  public static void main(String[] args) {
    System.out.println("初期のメッシュ");
    Mat a = new Mat(4, 4, 200);
    a.disp();

    System.out.println("初期のメッシュの値の総和: " + a.sum() + "\n");
  }
}

class Mat {
  public int xsize;
  public int ysize;
  private int[][] mat;
  private static Random rand = new Random(149);

  // pear組の点を行列に与えてメッシュを初期化
  Mat(int xmax, int ymax, int point_num) {
    // 初期化
    xsize = xmax + 1;
    ysize = ymax + 1;
    mat = new int[xsize][ysize];

    // メッシュに質量を与える
    for (int i = 0; i < point_num; i++) {
      // [0 max] の乱数のペアを生成する
      float x = rand_range(0, xmax);
      float y = rand_range(0, ymax);
      // 一番近いメッシュに質量を与える(整数に丸める)
      int xi = Math.round(x);
      int yi = Math.round(y);
      // 個数をインクリメントする
      mat[xi][yi]++;
    }
  }

  // 行列の内容を出力する
  public void disp() {
    for (int y = 0; y < ysize; y++) {
      for (int x = 0; x < xsize; x++) {
        System.out.printf("%3d ", mat[x][y]);
      }
      System.out.println();
    }
    System.out.println();
  }

  public int sum() {
    int s = 0;
    for (int x = 0; x < xsize; x++) {
      for (int y = 0; y < xsize; y++) {
        s += mat[x][y];
      }
    }
    return s;
  }

  // [min max]の範囲の乱数を生成する
  private float rand_range(int min, int max) {
    return rand.nextFloat() * (max - min) + min;
  }
}