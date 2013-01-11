public class Ex9 {
  public static void main(String[] args) {
    // ガウス・ザイデル法法によって偏微分方程式を解く
    Mat phi = new Mat(4);
    phi.init();
    phi.GaussSeidel(1.0, 1.0, 2000);

    System.out.println("ガウス・ザイデル法法によって求めた偏微分方程式");
    phi.disp();

    // 検算のために、解析解との差分を求める
    System.out.println("解析解との差分");
    phi.disp_diff();
  }
}

class Mat {
  public int size;
  public double[][] mat;

  Mat(int Size) {
    size = Size;
    mat = new double[size][size];
  }

  // phi を初期化する
  public void init() {
    // 行列を0で初期化 (javaの場合は明示的に行う必要がないので省略)
    // 境界条件
    mat[1][3] = 22.5;
    mat[2][3] = 36;
    mat[3][1] = -4.5;
    mat[3][2] = 9;
  }

  // 行列の全体を表示
  public void disp_all() {
    for (int y = 0; y < size; y++) {
      for (int x = 0; x < size; x++) {
        System.out.printf("%4.1f ", mat[x][y]);
      }
      System.out.println();
    }
    System.out.println();
  }

  // 行列の境界を除いた部分を表示
  public void disp() {
    for (int y = 1; y < size - 1; y++) {
      for (int x = 1; x < size - 1; x++) {
        System.out.printf("%4.1f ", mat[x][y]);
      }
      System.out.println();
    }
    System.out.println();
  }

  // ro を計算する
  private double ro(int x, int y) {
    return (6 * x - 3 * y);
  }

  // ガウス・ザイデル法で偏微分方程式を解く
  public void GaussSeidel(double G, double dx, int repeat) {
    for (int i = 0; i < repeat; i++) {
      for (int y = 1; y < size - 1; y++) {
        for (int x = 1; x < size - 1; x++) {
          double p1 = mat[x + 1][y] + mat[x - 1][y] + mat[x][y + 1]
              + mat[x][y - 1];
          double p2 = G * ro(x, y) * dx * dx;
          mat[x][y] = p1 / 4 - p2 / 4;
        }
      }
    }
  }

  // ヒントの解との差分を計算して表示する
  public void disp_diff() {
    for (int y = 1; y < size - 1; y++) {
      for (int x = 1; x < size - 1; x++) {
        System.out.printf("%4.1f ", mat[x][y]
            - (3 * x * y * y - 1.5 * x * x * y));
      }
      System.out.println();
    }
    System.out.println();
  }

}