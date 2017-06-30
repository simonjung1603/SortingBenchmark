package de.dhbw.sort.visualize;

import de.dhbw.sort.SortingBenchmark;
import de.dhbw.sort.util.AbstractAlgorithmHelper;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Created by jbi on 13.06.2017.
 */
public class Visualizer extends PApplet {
    int fullScreen = -1;

    private int width = 200;
    private int height = 600;
    private int rowCount = 3;
    private int columnCount = 3;
    private int fWidth;
    private int fHeight;
    private boolean[] screenValid;
    private AbstractGraphics[] screens;
    private PImage display;
    private PImage displayFullscreen;

    public Visualizer(int width, int height, int rowCount, int columnCount) {
        this.width = width;
        this.height = height;
        this.rowCount = rowCount;
        this.columnCount = columnCount;

        this.fWidth = width / rowCount;
        this.fHeight = height / columnCount;


        screens = new AbstractGraphics[rowCount * columnCount];
        screenValid = new boolean[screens.length];



    }

    public void initScreens() {


        for (int i = 0; i < screens.length; i++) {
            screens[i] = new DummyGraphics(this.createGraphics(fWidth, fHeight), this.createGraphics(width, height));
        }
    }

    private void initScreen(int theIndex) {


        screens[theIndex] = new Graphics(this.createGraphics(fWidth, fHeight), this.createGraphics(width, height));
    }

    public void setup() {
        //Momentan w�rde ich das vergr��ern des Hauptfensters verbieten
        //surface.setResizable(true);
        frameRate(30);
        initScreens();

        display = createImage(fWidth, fHeight, PApplet.RGB);
        displayFullscreen = createImage(width, height, PApplet.RGB);
        try {

            Thread.sleep(1000);
            System.out.println(screens[1].frames.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void settings() {
        size(width, height);
    }

    public void mousePressed() {
        // Ordne dem Mausclick ein Fenster zu und setzte es auf fullscreen.
        if (fullScreen == -1) {
            fullScreen = mouseX / (fWidth);
            System.out.println("X index is: " + fullScreen);
            fullScreen += (mouseY / (fHeight)) * rowCount;
            System.out.println("Y index is: " + (mouseY / fWidth));
            System.out.println("Array index is: " + fullScreen);

            screenValid[fullScreen] = false;

        } else {
            // War schon eins Vollbild setze die ansicht wieder zurueck

            screenValid[fullScreen] = false;
            fullScreen = -1;
            background(0);
        }
    }

    public void draw() {
//        System.out.println(this.frameRate);
        // Wenn keines im fullScreen Modus ist zeichne einfach alle normal
        if (fullScreen == -1) {
            for (int i = 0; i < screens.length; i++)

            {

                try {
                    if (screens[i].peek(false) != null) {
                        display.loadPixels();
                        display.pixels =  screens[i].getNextFrame(false);
                        display.updatePixels();
                        image(display, (i % rowCount) * (fWidth),
                                (i / rowCount) * (fHeight));
                        screens[i].getNextFrame(true);
                    }

                } catch (Exception e) {

                }
            }
        } else {
            try {
                if (screens[fullScreen].peek(true) != null) {

                    displayFullscreen.loadPixels();
                    displayFullscreen.pixels = screens[fullScreen].getNextFrame(true);
                    displayFullscreen.updatePixels();
                    image(displayFullscreen, 0, 0);
//                    screens[fullScreen].framesFullscreen.poll();

                }

                image(displayFullscreen, 0, 0);
                for (int i = 0; i < screens.length; i++) {
                    screens[i].getNextFrame(false);
                    screens[i].getNextFrame(true);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //                        screens[fullScreen].setRenderd(true);
            //                    }
        }
    }

    public int getGridNumber() {
        return screens.length;
    }


    public Graphics getScreen(int index) {
        initScreen(index);
        return (Graphics) screens[index];
    }
}
