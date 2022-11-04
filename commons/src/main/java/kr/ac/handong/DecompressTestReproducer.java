/*
 * Copyright (c) 2017-2018 The Regents of the University of California
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package kr.ac.handong;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DeflaterInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import java.io.File;
import java.io.FileInputStream;

public class DecompressTestReproducer {

    private static void bzip2(InputStream in){
        byte[] destBuffer = new byte[1024];
        try {
            new BZip2CompressorInputStream(in)
                .read(destBuffer, 0, destBuffer.length);
        } catch (IOException e){
            // Ignore
        }

    }

    private static void deflate(InputStream in){
        byte[] destBuffer = new byte[1024];
        try {
            new DeflaterInputStream(in)
                .read(destBuffer, 0, destBuffer.length);
        } catch (IOException e){
            // Ignore
        }

    }
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("usage: java Reproducer [option] [input]");
            System.err.println("option");
            System.err.println("\t-b: bzip2()");
            System.err.println("\t-d: deflate()");
            System.exit(1);
        }

        File file = new File(args[1]);
        try {
            InputStream is = new FileInputStream(file);
            if (args[0].equals("-b")) bzip2(is) ;
            else if (args[0].equals("-d")) deflate(is) ;
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}