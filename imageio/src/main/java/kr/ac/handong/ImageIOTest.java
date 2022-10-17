package kr.ac.handong;

import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.junit.runner.RunWith;

import edu.berkeley.cs.jqf.fuzz.Fuzz;
import edu.berkeley.cs.jqf.fuzz.JQF;

@RunWith(JQF.class)
public class ImageIOTest {

    @Fuzz
    public void testWithInputStream(InputStream input) throws java.lang.IllegalArgumentException {
        try {
            ImageIO.read(input); // Throws java.lang.OutOfMemoryError
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}