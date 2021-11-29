package ru.samsung.itschool.book.cells;

import static ru.samsung.itschool.book.cells.R.layout.*;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;


import task.Stub;
import task.Task;

public class CellsActivity extends Activity implements OnClickListener {

    private Button[][] cells;
    private int schet = 0;
    final int WIDTH = 3;
    final int HEIGHT = 3;
    private int[][] clicked = new int[WIDTH][HEIGHT];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cells);
        makeCells();
        generate();
    }


    void generate() {

        for (int i = 0; i < HEIGHT; i++)
            for (int j = 0; j < WIDTH; j++) {
                cells[i][j].setBackgroundColor(Color.WHITE);
                clicked[i][j] = 0;
            }
    }

    public boolean checking(int[][] clicked) {
        boolean checkingResult = true;
        for (int i = 0; i < HEIGHT; i++)
            for (int j = 0; j < WIDTH; j++) {
                if (clicked[i][j] == 0) {
                    checkingResult = false;
                    break;
                }
            }
        return checkingResult;
    }

    public int whoWin(int HEIGHT, int WIDTH, int[][] clicked, int numb) {
        int winner = 0;

        for (int i = 0; i < HEIGHT; i++) {
            int line = 0;
            for (int j = 0; j < WIDTH; j++)
                if (clicked[i][j] == numb)
                    line++;
            if (line == WIDTH) {
                winner = numb;
                break;
            }
        }
        for (int i = 0; i < HEIGHT; i++) {
            int line = 0;
            for (int j = 0; j < WIDTH; j++)
                if (clicked[j][i] == numb)
                    line++;
            if (line == WIDTH) {
                winner = numb;
                break;
            }
        }
        int diagonal1 = WIDTH - 1, diagonal2 = 0, dia1 = 0, dia2 = 0;
        while (diagonal1 >= 0 && diagonal2 <= WIDTH - 1) {
            if (clicked[diagonal2][diagonal1] == numb)
                dia1++;
            if (clicked[diagonal2][diagonal2] == numb)
                dia2++;
            diagonal1--;
            diagonal2++;
            if (dia1 == WIDTH || dia2 == WIDTH)
                winner = numb;
        }
        return winner;
    }

//    @Override
//    public boolean onLongClick(View v) {
//        Button tappedCell = (Button) v;
//
//        //Получаем координтаты нажатой клетки
//        int tappedX = getX(tappedCell);
//       int tappedY = getY(tappedCell);
//        cells[tappedX][tappedY].setBackgroundColor(Color.RED);
//        return false;
//    }

    @Override
    public void onClick(View v) {
        Button tappedCell = (Button) v;
        int tappedX = getX(tappedCell);
        int tappedY = getY(tappedCell);
        if (schet % 2 == 0 && clicked[tappedX][tappedY] == 0) {
            cells[tappedX][tappedY].setBackgroundResource(R.drawable.krestik);
            clicked[tappedX][tappedY] = 1;
            schet++;
        }
        if (schet % 2 == 1 && clicked[tappedX][tappedY] == 0) {
            cells[tappedX][tappedY].setBackgroundResource(R.drawable.nolik);
            clicked[tappedX][tappedY] = 2;
            schet++;
        }
        if (checking(clicked)) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setContentView(game_over_screen);
                }
            }, 100);
        } else if (whoWin(HEIGHT, WIDTH, clicked, 1) == 1) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setContentView(krestik_win_screen);
                }
            }, 100);
        } else if (whoWin(HEIGHT, WIDTH, clicked, 2) == 2) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setContentView(nolik_win_screen);
                }
            }, 100);
        }
    }

    public void onClick_Restart(View v) {
        finish();
        startActivity(getIntent());
    }
    /*
     * NOT FOR THE BEGINNERS
     * ==================================================
     */

    int getY(View v) {
        return Integer.parseInt(((String) v.getTag()).split(",")[1]);
    }

    int getX(View v) {
        return Integer.parseInt(((String) v.getTag()).split(",")[0]);
    }

    void makeCells() {
        cells = new Button[HEIGHT][WIDTH];
        GridLayout cellsLayout = (GridLayout) findViewById(R.id.CellsLayout);
        cellsLayout.removeAllViews();
        cellsLayout.setColumnCount(WIDTH);
        for (int i = 0; i < HEIGHT; i++)
            for (int j = 0; j < WIDTH; j++) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                cells[i][j] = (Button) inflater.inflate(cell, cellsLayout, false);
                cells[i][j].setOnClickListener(this);
                cells[i][j].setTag(i + "," + j);
                cellsLayout.addView(cells[i][j]);
            }
    }
}