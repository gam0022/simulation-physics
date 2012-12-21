import java.util.Random;

public class Ex5 {
  public static void main(String[] args) {
    // 初期密度
    double[] D = { 0.3, 0.5, 0.7 };
    // セルの数
    int I = 20;
    // 計算する時間
    int T = 20;
    // 規則
    int RULE = 184;

    for (double d : D) {
      System.out.printf("初期密度: %2.1f\n", d);
      Cell cell = new Cell(I, d);
      for (int t = 0; t <= T; t++) {
        System.out.printf("t=%2d : ", t);
        cell.disp();
        cell = new Cell(cell, RULE);
      }
      System.out.println();
    }
  }
}

class Cell {
  private int[] cell;
  public int size;

  // 大きさと初期密度を指定して、セルを初期化する
  Cell(int size_, double d) {
    size = size_;
    cell = new int[size];
    Random rnd = new Random();
    int count = 0;
    while (count < d * size) {
      int i = rnd.nextInt(size);
      if (cell[i] == 0) {
        cell[i] = 1;
        count++;
      }
    }
  }

  // セルから次世代のセルを作る
  Cell(Cell prev, int rule) {
    size = prev.size;
    cell = new int[size];
    for (int i = 0; i < size; i++) {
      int v = (prev.at(i - 1) << 2) | (prev.at(i) << 1) | prev.at(i + 1);
      cell[i] = (rule >> v) & 1;
    }
  }

  // 範囲チェックをしてセルの値を参照
  // 範囲外のときは、反対側のセルの値を返す
  private int at(int i) {
    return cell[i < 0 ? size - 1 : i >= size ? 0 : i];
  }

  // セル全体を出力
  public void disp() {
    for (int v : cell) {
      System.out.printf("%d ", v);
    }
    System.out.println();
  }

}