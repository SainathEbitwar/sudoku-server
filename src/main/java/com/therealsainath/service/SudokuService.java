package com.therealsainath.service;

import java.util.List;

public interface SudokuService {
    void initializeV1(String input);

    boolean move(int row, int col, int val);

    void initializeV2(List<List<Integer>> input);

    String status();
}
