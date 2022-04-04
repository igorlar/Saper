package com.example.saper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static int time = 999000;
    public static int bomb = 20;
    public static int size = 8;
    private TextView smiley, timer, flag, flagsLeft;
    private MineSweeperGame mineSweeperGame;
    private CountDownTimer countDownTimer;
    private int secondsElapsed;
    private boolean timerStarted;
    private boolean firstpick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        createGrid();

        timer = findViewById(R.id.activity_main_timer);
        timerStarted = false;
        countDownTimer = new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
                secondsElapsed += 1;
                timer.setText(String.format("%03d", secondsElapsed));
            }

            public void onFinish() {
                mineSweeperGame.outOfTime();
                Toast.makeText(getApplicationContext(), "Время вышло! Поражение", Toast.LENGTH_SHORT).show();
                revealAllBombs();
            }
        };
        flagsLeft = findViewById(R.id.activity_main_flagsleft);
        if (firstpick){
            flagsLeft.setText(String.format("%03d", mineSweeperGame.getNumberBombs() - mineSweeperGame.getFlagCount()));
        }
        else{
            flagsLeft.setText(String.format("%03d", bomb));
        }



        smiley = findViewById(R.id.activity_main_smiley);
        smiley.setOnClickListener(view -> {
            GradientDrawable border = new GradientDrawable();
            flag.setBackground(border);
            firstpick = false;
            timerStarted = false;
            countDownTimer.cancel();
            secondsElapsed = 0;
            timer.setText(R.string.default_count);
            flagsLeft.setText(String.format("%03d", bomb));
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    findViewById(x * size + y).setBackgroundTintList(getResources().getColorStateList(R.color.grey, null));
                    ((Button) findViewById(x * size + y)).setText("");
                }
            }
        });

        flag = findViewById(R.id.activity_main_flag);
        flag.setOnClickListener(view -> {
            if (firstpick){
                mineSweeperGame.toggleMode();
                if (mineSweeperGame.isFlagMode()) {
                    GradientDrawable border = new GradientDrawable();
                    border.setStroke(6, Color.rgb(255,0,0));
                    flag.setBackground(border);
                } else {
                    GradientDrawable border = new GradientDrawable();
                    flag.setBackground(border);
                }
            }
        });
    }

    private void openemptyCell(View view) {
        int x = getidX(view);
        int y = getidY(view);
        if (mineSweeperGame.getMineGrid().cellAt(x - 1, y - 1) != null) {
            openCell(findViewById((y - 1) * size + (x - 1)));
        }
        if (mineSweeperGame.getMineGrid().cellAt(x, y - 1) != null) {
            openCell(findViewById((y - 1) * size + x));
        }
        if (mineSweeperGame.getMineGrid().cellAt(x + 1, y - 1) != null) {
            openCell(findViewById((y - 1) * size + (x + 1)));
        }
        if (mineSweeperGame.getMineGrid().cellAt(x - 1, y) != null) {
            openCell(findViewById(y * size + (x - 1)));
        }
        if (mineSweeperGame.getMineGrid().cellAt(x + 1, y) != null) {
            openCell(findViewById(y * size + (x + 1)));
        }
        if (mineSweeperGame.getMineGrid().cellAt(x - 1, y + 1) != null) {
            openCell(findViewById((y + 1) * size + (x - 1)));
        }
        if (mineSweeperGame.getMineGrid().cellAt(x, y + 1) != null) {
            openCell(findViewById((y + 1) * size + x));
        }
        if (mineSweeperGame.getMineGrid().cellAt(x + 1, y + 1) != null) {
            openCell(findViewById((y + 1) * size + (x + 1)));
        }
    }

    private void openCell(View view) {
        Cell cell;
        cell = mineSweeperGame.getMineGrid().cellAt(getidX(view), getidY(view));
        if (!cell.isRevealed() && !cell.isFlagged() && mineSweeperGame.isClearMode()) {
            if (cell.getValue() == -1) {
                ((Button) findViewById(view.getId())).setText(R.string.bomb);
                view.setBackgroundTintList(getResources().getColorStateList(R.color.red, null));
                countDownTimer.cancel();
                Toast.makeText(getApplicationContext(), "Поражение", Toast.LENGTH_SHORT).show();
                revealAllBombs();
                mineSweeperGame.GameOver();
            } else if (cell.getValue() == 0) {
                cell.setRevealed(true);
                openemptyCell(view);
                view.setBackgroundTintList(getResources().getColorStateList(R.color.blackgrey, null));
            } else {
                String s = String.valueOf(cell.getValue());
                ((Button) findViewById(view.getId())).setText(s);
                view.setBackgroundTintList(getResources().getColorStateList(R.color.blackgrey, null));
                if (cell.getValue() == 1) {
                    ((Button) findViewById(view.getId())).setTextColor(Color.rgb(0, 0, 255));
                }
                if (cell.getValue() == 2) {
                    ((Button) findViewById(view.getId())).setTextColor(Color.rgb(0, 255, 0));
                }
                if (cell.getValue() == 3) {
                    ((Button) findViewById(view.getId())).setTextColor(Color.rgb(255, 0, 0));
                }
                if (cell.getValue() == 4) {
                    ((Button) findViewById(view.getId())).setTextColor(Color.rgb(25, 25, 112));
                }
                if (cell.getValue() == 5) {
                    ((Button) findViewById(view.getId())).setTextColor(Color.rgb(128, 0, 0));
                }
                if (cell.getValue() == 6) {
                    ((Button) findViewById(view.getId())).setTextColor(Color.rgb(0, 255, 255));
                }
                if (cell.getValue() == 7) {
                    ((Button) findViewById(view.getId())).setTextColor(Color.rgb(255, 165, 0));
                }
                if (cell.getValue() == 8) {
                    ((Button) findViewById(view.getId())).setTextColor(Color.rgb(255, 215, 0));
                }
            }
            cell.setRevealed(true);
        }
    }


    private void clickCell(Button button) {
        button.setOnClickListener(view -> {
            if (!firstpick){
                mineSweeperGame = new MineSweeperGame(size, bomb, getidX(view), getidY(view));
                firstpick = true;
            }
            if (!mineSweeperGame.getOutOfTime() && !mineSweeperGame.isGameWon()) {
                if (mineSweeperGame.isGameOver()) {
                    Toast.makeText(getApplicationContext(), "Поражение", Toast.LENGTH_SHORT).show();
                } else {
                    if (!timerStarted) {
                        countDownTimer.start();
                        timerStarted = true;
                    }
                    Cell cell;
                    cell = mineSweeperGame.getMineGrid().cellAt(getidX(view), getidY(view));
                    openCell(view);
                    if (mineSweeperGame.isFlagMode() && !cell.isRevealed()) {
                        if (mineSweeperGame.getFlagCount() >= mineSweeperGame.getNumberBombs() && !cell.isFlagged()) {
                            Toast.makeText(getApplicationContext(), "Флаги кончились", Toast.LENGTH_SHORT).show();
                        } else {
                            if (cell.isFlagged()) {
                                mineSweeperGame.setFlagCount(-1);
                                ((Button) findViewById(view.getId())).setText("");
                            } else {
                                ((Button) findViewById(view.getId())).setText(R.string.flag);
                                mineSweeperGame.setFlagCount(1);
                            }
                            cell.setFlagged(!cell.isFlagged());
                        }

                        flagsLeft.setText(String.format("%03d", mineSweeperGame.getNumberBombs() - mineSweeperGame.getFlagCount()));
                    }
                }
            }
            if (mineSweeperGame.isGameWon()) {
                Toast.makeText(getApplicationContext(), "Победа", Toast.LENGTH_SHORT).show();
                revealAllBombs();
                countDownTimer.cancel();
            }
        });
    }

    private void revealAllBombs() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (mineSweeperGame.getMineGrid().cellAt(x, y).getValue() == -1) {
                    ((Button) findViewById(y * size + x)).setText(R.string.bomb);
                    mineSweeperGame.getMineGrid().cellAt(x, y).setRevealed(true);
                }
            }
        }
    }

    public void createGrid() {
        TableLayout tableLayout = findViewById(R.id.table_layout);
        tableLayout.removeAllViews();
        for (int x = 0; x < size; x++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            tableRow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            for (int y = 0; y < size; y++) {
                Button button = new Button(this);
                button.setId(x * size + y);
                button.setLayoutParams(new TableRow.LayoutParams((int) 850 / size, (int) 850 / size));
                clickCell(button);
                tableRow.addView(button, y);
            }
            tableLayout.addView(tableRow, x);
        }
    }

    private int getidX(View view) {
        int id = view.getId();
        int a = id - id / size * size;
        return a;
    }

    private int getidY(View view) {
        int id = view.getId();
        int a = id / size;
        return a;
    }
}