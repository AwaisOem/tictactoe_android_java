package com.example.tic_tac_toe;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    int[] gameArr = new int[]{0,0,0,0,0,0,0,0,0};
    int turn = 1; //1 or 2
    int lastMoveIndex = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        for (int i = 0; i < 9; i++) {
            @SuppressLint("DiscouragedApi") int buttonId = getResources().getIdentifier("gameButton_" + i, "id", getPackageName());
            Button button = findViewById(buttonId);
            button.setOnClickListener(this);
        }


        findViewById(R.id.reset_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBoard();
                Toast.makeText(getApplicationContext(), "Board Reset", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.undo_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoMove();
                Toast.makeText(getApplicationContext(), "Undo Move", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            String buttonIdString = getResources().getResourceEntryName(v.getId());
            String numericPart = buttonIdString.replace("gameButton_", "");
            int buttonNumber = Integer.parseInt(numericPart);

            if(gameArr[buttonNumber]!=0) return;

            gameArr[buttonNumber] = turn;
            lastMoveIndex = buttonNumber;
            ((Button) v).setText(turn == 1 ? "/":"X");
            turn = turn==1 ? 2:1;

            int n = checkResult();
            if(n==3)return;
            String message = "";
            if(n==0){
                message = "Match is Draw";
            }else{
                message = "Hurray! Player "+(n==1 ? "/":"X")+" Won";
            }
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            resetBoard();
        }
    }

    private void undoMove(){
        if(lastMoveIndex == -1) return;
        gameArr[lastMoveIndex] = 0;
        @SuppressLint("DiscouragedApi") int buttonId = getResources().getIdentifier("gameButton_" + lastMoveIndex, "id", getPackageName());
        Button button = findViewById(buttonId);
        turn = turn==1 ? 2:1;
        button.setText("");
        lastMoveIndex = -1;
    }

    private void resetBoard(){
        Arrays.fill(gameArr, 0);
        for (int i = 0; i < 9; i++) {
            @SuppressLint("DiscouragedApi") int buttonId = getResources().getIdentifier("gameButton_" + i, "id", getPackageName());
            Button button = findViewById(buttonId);
            button.setText("");
        }
    }
    private int checkResult() {
        // Winning combinations (rows, columns, diagonals)
        int[][] winPatterns = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
                {0, 4, 8}, {2, 4, 6} // Diagonals
        };

        for (int[] pattern : winPatterns) {
            int firstCell = gameArr[pattern[0]];
            if (firstCell != 0 && gameArr[pattern[1]] == firstCell && gameArr[pattern[2]] == firstCell) {
                return firstCell;
            }
        }
        for(int j: gameArr){
            if (j==0)return 3;
        }
        // 0 for draw
        return 0;
    }

}
