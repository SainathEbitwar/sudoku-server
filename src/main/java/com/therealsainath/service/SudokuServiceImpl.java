package com.therealsainath.service;

import com.therealsainath.utils.SudokuUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
@Slf4j
public class SudokuServiceImpl implements SudokuService {

    private final SudokuUtility sudokuUtility;
    private int attemptsLeft;

    public SudokuServiceImpl(SudokuUtility sudokuUtility) {
        this.sudokuUtility = sudokuUtility;
    }

    @Override
    public void initializeV1(String input) {

        if (input != null && input.equalsIgnoreCase("START")) {
            log.info("Initializing a new Sudoku game");
            sudokuUtility.initializeV1();
            attemptsLeft = 3;
            log.info("Initializing completed");
            return;
        }

        log.error("Error while initializing sudoku bad input : {}", input);
        throw new HttpClientErrorException(BAD_REQUEST, "Bad Input : " + input, ("Bad Input : " + input).getBytes(UTF_8), UTF_8);

    }

    @Override
    public void initializeV2(List<List<Integer>> input) {

        if (isValidInputSkeleton(input)) {
            log.info("Initializing a new Sudoku game");
            sudokuUtility.initializeV2(input);
            log.info("Initializing completed");
            attemptsLeft = 3;
            return;
        }
        log.error("Error while initializing sudoku bad input : {}", input);
        throw new HttpClientErrorException(BAD_REQUEST, "Invalid Skeleton", "Invalid Skeleton".getBytes(UTF_8), UTF_8);

    }

    @Override
    public String status() {
        return sudokuUtility.inProgressStatus();
    }

    private boolean isValidInputSkeleton(List<List<Integer>> input) {
        return input != null && input.size() == 9 && isValidContent(input);



    }

    private boolean isValidContent(List<List<Integer>> input) {
        int count =0;
        for (List<Integer> list : input) {
            if (list == null || list.size() != 9)
                return false;
            for (int n : list) {
                if ( n < 0 || n > 9)
                    return false;

                if ( n != 0)
                    count++;
            }
        }

        if (count < 25) {
            throw new HttpClientErrorException(BAD_REQUEST,
                    "Invalid Skeleton : A Skeleton should have at least 25 clues, you have given only " + count + " clues",
                    "Invalid Skeleton : A Skeleton should have at least 25 clues, you have given only \" + count + \" clues".getBytes(UTF_8), UTF_8);
        }




        return true;
    }

    @Override
    public boolean move(int row, int col, int val) {
        attemptsLeft--;

        if (row < 1 || row > 9 || col < 1 || col > 9 || val < 1 || val > 9 || !sudokuUtility.isValid(row-1, col-1, val)) {
            String msg = "Invalid input try again, attempts left : " + attemptsLeft ;
            if (attemptsLeft == 0) {
                int cv = sudokuUtility.correctValue(row-1, col-1);
                msg = "Invalid input Failed after 3 tries correct value for row : " + row + " and column : " + col + " is : " + cv;
                attemptsLeft = 3;
            }
            log.error(msg);
            throw new HttpClientErrorException(BAD_REQUEST, msg, msg.getBytes(UTF_8), UTF_8);

        }

        boolean isCompleted = sudokuUtility.setVal(row - 1, col - 1, val);
        attemptsLeft = 3;

        return isCompleted;
    }
}
