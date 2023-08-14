package com.therealsainath.controller;

import com.therealsainath.model.Entry;
import com.therealsainath.service.SudokuService;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SudokuController {

    private final SudokuService sudokuService;

    public SudokuController(SudokuService sudokuService) {
        this.sudokuService = sudokuService;
    }


    @PostMapping("/v1/START")
    public String startv1(@RequestBody String input) {
        sudokuService.initializeV1(input);
        return "READY";
    }

    @PostMapping("/v2/START")
    public String startv2(@RequestBody List<List<Integer>> input) {
        sudokuService.initializeV2(input);
        return "READY";
    }

    @GetMapping("/move/{row}/{col}/{val}")
    public String move(@PathVariable(value = "row") int row,
                       @PathVariable(value = "col") int col,
                       @PathVariable(value = "val") int val) {

        boolean isCompleted = sudokuService.move(row, col, val);
        if (isCompleted)
            return "Valid : This Sudoku game is completed pls initialize a new one";
        return "Valid";

    }

    @PostMapping("/move")
    public String move1(@NonNull @RequestBody Entry entry) {

        boolean isCompleted = sudokuService.move(entry.getRow(), entry.getCol(), entry.getVal());
        if (isCompleted)
            return "Valid : This Sudoku game is completed pls initialize a new one";
        return "Valid";

    }

    @GetMapping("/status")
    public String status() {
        return sudokuService.status();
    }

}
