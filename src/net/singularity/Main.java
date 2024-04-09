package net.singularity;

import org.lwjgl.Version;

public class Main {

    public static void main(String[] args) {
        System.out.println("Launching game");
        System.out.println("LWJGL " + Version.getVersion());
        new Singularity().start();
    }
}
