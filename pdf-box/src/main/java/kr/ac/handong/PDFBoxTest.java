package kr.ac.handong;

import java.io.IOException;
import java.io.InputStream;

import com.pholser.junit.quickcheck.From;
import edu.berkeley.cs.jqf.fuzz.Fuzz;
import edu.berkeley.cs.jqf.fuzz.JQF;

import org.junit.runner.RunWith;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

@RunWith(JQF.class)
public class PDFBoxTest 
{
    @Fuzz
    public void fuzzWithInputStream(InputStream in) throws InvalidPasswordException
    {
        try {
            PDDocument.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
