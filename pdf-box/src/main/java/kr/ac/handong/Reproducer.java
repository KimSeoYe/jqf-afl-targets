package kr.ac.handong;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

public class Reproducer {

    private static void fuzzWithInputStream(InputStream in) throws InvalidPasswordException
    {
        try {
            PDDocument.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: java Reproducer [input]");
            System.exit(1);
        }

        try {
            File file = new File(args[0]);
            InputStream in = new FileInputStream(file);
            fuzzWithInputStream(in);
        } catch (InvalidPasswordException e) {
            System.err.println("org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
