package net.singularity.utils;

import java.awt.Component;
import javax.swing.JOptionPane;
public class SException {
    private static Component mainComponent = null;

    public static void raiseException(Exception e) throws Exception {
        String msg = "An exception has occurred.\n\n" + e.toString();

        JOptionPane.showMessageDialog(mainComponent, msg, "Exception", JOptionPane.ERROR_MESSAGE);
        throw e;
    }

    public static void raiseException(RuntimeException e) {
        String msg = "An exception has occurred.\n\n" + e.toString();

        JOptionPane.showMessageDialog(mainComponent, msg, "Exception", JOptionPane.ERROR_MESSAGE);
        throw e;
    }

    public static void raiseException(IllegalStateException e) {
        String msg = "An exception has occurred.\n\n" + e.toString();

        JOptionPane.showMessageDialog(mainComponent, msg, "Exception", JOptionPane.ERROR_MESSAGE);
        throw e;
    }
}
