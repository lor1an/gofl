/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epam.gameoflife;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author lor1an
 */
public class GameOfLifeGrid {

    public int cellRows;
    public int cellCols;
    public int generations;
    //private static Shape[] shapes;
    /**
     * Contains the current, living shape. It's implemented as a hashtable.
     * Tests showed this is 70% faster than Vector.
     */
    private HashMap currentShape;
    private HashMap nextShape;

    public Cell[][] grid;

    public GameOfLifeGrid(int cellCols, int cellRows) {
        this.cellCols = cellCols;
        this.cellRows = cellRows;
        currentShape = new HashMap();
        nextShape = new HashMap();
        grid = new Cell[cellCols][cellRows];

    }

    public void clear() {
        generations = 0;
        currentShape.clear();
        nextShape.clear();
    }

    public void next() {
        Cell cell;
        int x, y;
        int neighbours;
        Iterator iter;

        generations++;
        nextShape.clear();

        // Reset cells
        iter = currentShape.keySet().iterator();
        while (iter.hasNext()) {
            cell = (Cell) iter.next();
            cell.neighbour = 0;
        }
        // Add neighbours
        // You can't walk through an hashtable and also add elements. Took me a couple of ours to figure out. Argh!
        // That's why we have a hashNew hashtable.
        iter = currentShape.keySet().iterator();
        while (iter.hasNext()) {
            cell = (Cell) iter.next();
            x = cell.getX();
            y = cell.getY();
            addNeighbour(x - 1, y - 1);
            addNeighbour(x, y - 1);
            addNeighbour(x + 1, y - 1);
            addNeighbour(x - 1, y);
            addNeighbour(x + 1, y);
            addNeighbour(x - 1, y + 1);
            addNeighbour(x, y + 1);
            addNeighbour(x + 1, y + 1);
        }

        // Bury the dead
        // We are walking through an enum from we are also removing elements. Can be tricky.
        iter = currentShape.keySet().iterator();
        while (iter.hasNext()) {
            cell = (Cell) iter.next();
            // Here is the Game Of Life rule (1):
            if (cell.neighbour != 3 && cell.neighbour != 2) {
                currentShape.remove(cell);
            }
        }
        // Bring out the new borns
        iter = nextShape.keySet().iterator();
        while (iter.hasNext()) {
            cell = (Cell) iter.next();
            // Here is the Game Of Life rule (2):
            if (cell.neighbour == 3) {
                setCell(cell.getX(), cell.getY(), true);
            }
        }
    }

    private void addNeighbour(int x, int y) {
        if (x < 0 || x > cellCols || y < 0 || y > cellRows) {
            return;
        } else {
            try {
                Cell cell = (Cell) nextShape.get(grid[x][y]);
                if (cell == null) {
                    // Cell is not in hashtable, then add it
                    Cell c = grid[x][y];
                    c.neighbour = 1;
                    nextShape.put(c, c);
                } else {
                    // Else, increments neighbour count
                    cell.neighbour++;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                // ignore
            }
        }
    }

    private void setCell(int x, int y, boolean b) {
        try {
            Cell cell = grid[x][y];
            if (b) {
                currentShape.put(cell, cell);
            } else {
                currentShape.remove(cell);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // ignore
        }
    }

}
