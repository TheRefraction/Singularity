package net.singularity;

import net.singularity.system.Window;
import org.lwjgl.Version;

public class Main {
    public static void main(String[] args) {
        System.out.println(Version.getVersion());
        Window window = new Window("Singularity", 800, 600, false);
        window.init();

        while(!window.windowShouldClose()) {
            window.update();
        }

        window.cleanup();
    }
}
