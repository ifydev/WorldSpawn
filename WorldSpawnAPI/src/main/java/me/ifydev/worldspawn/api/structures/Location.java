package me.ifydev.worldspawn.api.structures;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Innectic
 * @since 08/06/2018
 */
@AllArgsConstructor
@Getter
public class Location {

    private String world;
    private int x;
    private int y;
    private int z;
}
