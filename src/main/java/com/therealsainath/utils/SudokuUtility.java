package com.therealsainath.utils;

import de.sfuhrm.sudoku.Creator;
import de.sfuhrm.sudoku.GameMatrix;
import de.sfuhrm.sudoku.Riddle;
import de.sfuhrm.sudoku.Solver;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static de.sfuhrm.sudoku.GameSchemas.SCHEMA_9X9;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class SudokuUtility {

    private int[][] solvedSudoku;
    private int[][] inProgressSudoku;

    public void initializeV1() {
        GameMatrix matrix = Creator.createFull(SCHEMA_9X9);
        Riddle riddle = Creator.createRiddle(matrix);

        initializeData(riddle, matrix);
    }

    private void initializeData(GameMatrix riddle, GameMatrix matrix) {
        solvedSudoku = new int[9][9];
        inProgressSudoku = new int[9][9];

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                inProgressSudoku[r][c] = riddle.get(r, c);
                solvedSudoku[r][c] = matrix.get(r, c);
            }
        }
    }

    public void initializeV2(List<List<Integer>> input) {

        GameMatrix matrix = Creator.createFull(SCHEMA_9X9);
        matrix.clear();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                matrix.set(i, j, (byte)(int)input.get(i).get(j));
            }
        }

        Solver solver = new Solver(matrix);
        solver.setLimit(1);
        List<GameMatrix> solutions = solver.solve();

        if ( solutions.size() == 0)
            throw new HttpClientErrorException(BAD_REQUEST,
                "No solution exists for this Sudoku skeleton pls try with valid skeleton",
                "No solution exists for this Sudoku skeleton pls try with valid skeleton".getBytes(UTF_8), UTF_8);

        initializeData(matrix, solutions.get(0));

    }
    public boolean isValid(int row, int col, int val) {
        return solvedSudoku[row][col] == val;
    }

    public int correctValue(int row, int col) {
        return solvedSudoku[row][col];
    }

    public boolean setVal(int row, int col, int val) {
        inProgressSudoku[row][col] = val;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (inProgressSudoku[i][j] == 0)
                    return false;
            }
        }
        return true;

    }

    public String inProgressStatus() {

        if (inProgressSudoku == null )
            throw new HttpClientErrorException(BAD_REQUEST,
                    "Sudoku not initialize pls initialize Sudoku using any of */START Api",
                    "Sudoku not initialize pls initialize Sudoku using any of */START Api".getBytes(UTF_8), UTF_8);

        StringBuilder status = new StringBuilder();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (inProgressSudoku[i][j] != 0)
                    status.append(inProgressSudoku[i][j]);
                else
                    status.append("*");
                status.append("  ");
            }
            status.append("</br>");
            status.append("\n");
        }

        return new String(status);
    }
}
