package com.example.simon.battleships;

public class GameManager {
    private static int[][] grid = new int[9][16];
    private static final int HIT = 2;
    private static final int NEAR_HIT = 1;
    private static int GRID_PIXEL_WIDTH = 120;
    private static int xPos;
    private static int yPos;

    /**
     * Sets up game to match resources
     * @param gridPixelWidth
     */
    public static void initializeGame(int gridPixelWidth) {
        GRID_PIXEL_WIDTH = gridPixelWidth;
    }

    /**
     * Places the ship on specified x and y coordinates and sets adjacent tiles to near hits
     * @param x X-coordinate for ship, between 0-8
     * @param y Y-coordinate for ship, between 0-15
     */
    public static void placeShip(int x, int y) {
        xPos = x;
        yPos = y;
        for(int i = y-1; i <= y+1; i++) {
            for(int j = x-1; j <= x+1; j++) {
                if(!(i < 0 || j < 0 || i > 15 || j > 8)) {
                    grid[j][i] = NEAR_HIT;
                }
            }
        }
        grid[x][y] = HIT;
    }

    public static void clearGrid(){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 16; j++){
                grid[i][j] = 0;
            }
        }
    }

    /**
     * Checks if the current finger position matches the ships position
     * @param x X-coordinate for current finger position
     * @param y Y-coordinate for current finger position
     * @return 2 if Hit, 1 if Near Hit and 0 if Miss
     */
    public static int isHit(int x, int y) {
        return grid[x / GRID_PIXEL_WIDTH][y / GRID_PIXEL_WIDTH];
    }

    /**
     * @return Width of one grid tile in pixels, e.g 120 pixels for 1080p resolution
     */
    public static int getGridPixelWidth() {
        return GRID_PIXEL_WIDTH;
    }
    public static int getShipX(){ return xPos; }
    public static int getShipY(){ return yPos; }
}