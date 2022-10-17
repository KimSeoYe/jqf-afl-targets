package kr.ac.handong;

import java.io.InputStream;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;

public class Reproducer {

    public static void main(String[] args) throws java.io.IOException {
        if (args.length == 0) {
            System.err.println("usage: java Reproducer [input]");
            System.exit(1);
        }
        
        File file = new File(args[0]);
        InputStream in = new FileInputStream(file);

        // Attempt to read PNG
        ImageIO.read(in); // Throws java.lang.OutOfMemoryError!
    }
}