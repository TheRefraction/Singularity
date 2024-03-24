package net.singularity;

import net.singularity.system.Engine;
import net.singularity.system.Window;
import net.singularity.utils.Const;

public class Main {

    private static Window window;
    private static Singularity game;

    public static void main(String[] args) {
        window = new Window(Const.TITLE, 800, 600, false);
        game = new Singularity();
        Engine engine = new Engine();
        try {
            engine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Window getWindow() {
        return window;
    }

    public static Singularity getGame() {
        return game;
    }
}
