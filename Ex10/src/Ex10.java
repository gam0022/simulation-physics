import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Random;

public class Ex10 {
  static int pr = 5;// 点の範囲
  static int np = 500;
  static int nm = 99;
  static double G = 1.0;
  static double M = 1.0;
  static double H = 3.5;
  static int ni = 50;
  static double dt = 0.1;
  static double d = 1.0;
  static int org = 50;
  static int[] nks = { 0, 20, 40 };

  public static void main(String[] args) {

    // 1.2.3
    Point[] points = new Point[np];
    for (int i = 0; i < np; i++) {
      points[i] = new Point(pr, org, H);
    }
    // 初期状態を出力
    csv_out(points, "before.csv");

    // 4. ポテンシャル phi(初期値は0)
    Mat phi = new Mat(nm + 2);

    // 現在出力すべきステップの添字
    int nki = 0;
    int nk = 0;

    // 5. 6〜9を時間ステップ回数ループ
    for (int step = 0; step <= nks[2]; step++) {
      // 6. 質量密度 ro
      Mat ro = new Mat(points, nm);
      // 7. ポテンシャル phi をガウス・ザイデル法で計算
      phi.GaussSeidel(ro, G, d, ni);
      // 8. 重力場 F
      Mat Fx = new Mat(phi.size - 1);
      Mat Fy = new Mat(phi.size - 1);
      phi.getF(Fx, Fy, d);
      // 9. 天体の運動
      for (Point p : points) {
        // (1) 天体が受ける力 Fp = M * F
        Mat Fpx = Fx.mul(M);
        Mat Fpy = Fy.mul(M);
        // (2) 天体の新しい速度を計算
        p.update_v(Fpx, Fpy, M, dt);
        // (3) 天体の新しい位置を計算
        p.move(dt);
        // シュミレーション結果をCSVに出力する
        if (step == nk) {
          String filename = String.format("after_nk%d.csv", nk);
          csv_out(points, filename);
          if(nki<2) nk = nks[++nki];
        }
      }
    }
  }

  // CSVに出力する
  static void csv_out(Point[] points, String filename) {
    File csv = new File(filename);
    try {
      FileOutputStream fos = new FileOutputStream(csv);
      OutputStreamWriter osw = new OutputStreamWriter(fos);
      PrintWriter pw = new PrintWriter(osw);

      for (int i = 0; i < np; i++) {
        pw.printf("%f,%f\n", points[i].x, points[i].y);
      }

      pw.close();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}

class Mat {
  public int size;
  private double[][] mat;

  // 大きさを指定して行列を初期化
  Mat(int size) {
    this.size = size;
    mat = new double[this.size][this.size];
  }

  // 質点を与えてメッシュを初期化
  Mat(Point[] ps, int nm) {
    // 初期化
    size = nm + 2;
    mat = new double[size][size];

    // メッシュに質量を与える
    for (Point p : ps) {
      // 一番近いメッシュに質量を与える(整数に丸める)
      int xi = nearest(p.x);
      int yi = nearest(p.y);
      // 個数をインクリメントする
      mat[xi][yi]++;
    }
  }

  // ポテンシャル phi から重力場 Fx,Fy を計算する
  void getF(Mat Fx, Mat Fy, double d) {
    for (int x = 1; x < size - 1; x++) {
      for (int y = 1; y < size - 1; y++) {
        Fx.mat[x][y] = -(at(x + 1, y) - at(x, y)) / d;
        Fy.mat[x][y] = -(at(x, y + 1) - at(x, y)) / d;
      }
    }
  }

  // 添字が範囲外なら範囲内に抑える
  int limit(int a) {
    return (a < 0) ? 0 : (a >= size) ? size - 1 : a;
  }

  // 最も近い添字を求める
  int nearest(double a) {
    return limit((int) Math.round(a));
  }

  // 最も近い点の値を返す
  public double at(double x, double y) {
    int xi = nearest(x);
    int yi = nearest(y);
    return mat[xi][yi];
  }

  // ガウス・ザイデル法で偏微分方程式を解く
  public void GaussSeidel(Mat ro, double G, double dx, int repeat) {
    for (int i = 0; i < repeat; i++) {
      for (int y = 1; y < size - 1; y++) {
        for (int x = 1; x < size - 1; x++) {
          double p1 = mat[x + 1][y] + mat[x - 1][y] + mat[x][y + 1]
              + mat[x][y - 1];
          double p2 = G * ro.mat[x][y] * dx * dx;
          mat[x][y] = p1 / 4 - p2 / 4;
        }
      }
    }
  }

  // 全ての値を定数倍する
  public Mat mul(double c) {
    Mat m = new Mat(size);
    for (int x = 1; x < size - 1; x++) {
      for (int y = 1; y < size - 1; y++) {
        m.mat[x][y] = c * mat[x][y];
      }
    }
    return m;
  }
}

class Point {
  public double x, y;
  public double vx, vy;
  private static Random rand = new Random(149);

  double rand_range(Random rand, double min, double max) {
    return rand.nextDouble() * (max - min) + min;
  }

  Point(int pr, int org, double H) {
    // x^2 + y^2 <= pr^2 を満たす(x,y)を生成する
    do {
      x = rand_range(rand, -pr, pr);
      y = rand_range(rand, -pr, pr);
    } while (x * x + y * y > pr * pr);
    // 初期速度を決定する
    vx = H * x;
    vy = H * y;
    // 中心座標分だけ平行移動する
    x += org;
    y += org;
  }

  // 速度を計算する
  void update_v(Mat Fpx, Mat Fpy, double M, double dt) {
    vx += Fpx.at(x, y) / M * dt;
    vy += Fpy.at(x, y) / M * dt;
  }

  // 移動する
  void move(double dt) {
    x += dt * vx;
    y += dt * vy;
  }
}