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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.LogManager;

import org.apache.commons.io.IOUtils;
import org.junit.Assume;

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Result;
import com.google.javascript.jscomp.SourceFile;

import java.io.File;
import java.io.FileInputStream;

public class Reproducer {

    static {
        // Disable all logging by Closure passes
        LogManager.getLogManager().reset();
    }
    
    private static void doCompile(SourceFile input, Compiler compiler, CompilerOptions options, SourceFile externs) {
        System.out.println("### doCompile");
        Result result = compiler.compile(externs, input, options);
        Assume.assumeTrue(result.success);
    }

    private static void testWithInputStream(InputStream in, Compiler compiler, CompilerOptions options, SourceFile externs) {
        try {
            SourceFile input = SourceFile.fromInputStream("input", in, StandardCharsets.UTF_8);
            doCompile(input, compiler, options, externs);
        } catch (IOException e) {
            e.printStackTrace();
        }      
    }

    public static void main (String[] args) {
        if (args.length == 0) {
            System.err.println("usage: java Reproducer [input]");
            System.exit(1);
        }

        Compiler compiler = new Compiler(new PrintStream(new ByteArrayOutputStream(), false));
        CompilerOptions options = new CompilerOptions();
        SourceFile externs = SourceFile.fromCode("externs", "");

        compiler.disableThreads();
        options.setPrintConfig(false);
        CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);
        
        try {
            File file = new File(args[0]);
            InputStream in = new FileInputStream(file);

            // System.out.println(IOUtils.toString(in, StandardCharsets.UTF_8.name())) ;
            
            testWithInputStream(in, compiler, options, externs);
        } catch (IOException e) {
            e.printStackTrace();
        }     
    }
}