package com.therealsainath.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Entry {

    private int row;
    private int col;
    private int val;

}
