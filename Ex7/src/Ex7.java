import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Ex7 {
  static int n = 500;
  static double H = 3.5;
  static int org = 50;
  static double dt = 0.1;
  static int step = 20;

  public static void main(String[] args) {
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
      points[i] = new Point(org, H);
    }

    csv_out(points, "before.csv");

    for (int i = 0; i < step; i++) {
      for (int j = 0; j < n; j++) {
        points[j].move(dt);
      }
    }

    csv_out(points, "after.csv");

  }

  // CSVに出力する
  static void csv_out(Point[] points, String filename) {
    File csv = new File(filename);
    try {
      FileOutputStream fos = new FileOutputStream(csv);
      OutputStreamWriter osw = new OutputStreamWriter(fos);
      PrintWriter pw = new PrintWriter(osw);

      for (int i = 0; i < n; i++) {
        pw.printf("%f,%f\n", points[i].x, points[i].y);
      }

      pw.close();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}

class Point {
  public double x, y;
  public double vx, vy;
  private static Random rand = new Random(149);

  double rand_range(Random rand, double min, double max) {
    return rand.nextDouble() * (max - min) + min;
  }

  Point(int org, double H) {
    // x^2 + y^2 <= 1を満たす(x,y)を生成する
    do {
      x = rand_range(rand, -1, 1);
      y = rand_range(rand, -1, 1);
    } while (x * x + y * y > 1);
    // 初期速度を決定する
    vx = H * x;
    vy = H * y;
    // 中心座標分だけ平行移動する
    x += org;
    y += org;
  }

  // 移動する
  void move(double dt) {
    x += dt * vx;
    y += dt * vy;
  }
}