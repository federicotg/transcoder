/*
 * Copyright (C) 2015 Federico Tello Gentile <federicotg@gmail.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.fede.transcoder;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

public class Transcoder extends Observable {

    private static final FilenameFilter SOURCE_FILENAME_FILTER = new FileExtensionFilter(".flac");
    private static final FileFilter DIRECTORY_FILTER = new DirectoryFilter();
    private ExecutorService executor;
    private final AudioCodec codec;
    private final File src;
    private final File dst;

    public Transcoder(AudioCodec codec,
            File src, File dst) {
        this.codec = codec;
        this.src = src;
        this.dst = dst;
    }

    public void cancelTranscode() {
        this.executor.shutdownNow();
    }

    public void transcodeInParallel(boolean overwrite, int cores) throws IOException, InterruptedException {
        this.executor = Executors.newFixedThreadPool(cores);
        this.transcode(src, overwrite);
        this.executor.shutdown();
        while (!this.executor.isTerminated()) {
            this.executor.awaitTermination(10, TimeUnit.SECONDS);
        }
    }

    private void transcode(final File currentSrc,
            boolean overwrite) throws IOException, InterruptedException {

        final ProcessBuilder pb = new ProcessBuilder(this.codec.getArguments());
        for (File srcFile : currentSrc.listFiles(SOURCE_FILENAME_FILTER)) {
            final String srcName = srcFile.getAbsolutePath();
            final String dstName = srcName.replace(this.src.getAbsolutePath(), this.dst.getAbsolutePath()).replace(".flac", this.codec.getExtension());
            if (overwrite || !new File(dstName).exists()) {
                Callable<Integer> callable = () -> {
                    Process process = null;
                    synchronized (Transcoder.this) {
                        codec.setDestination(dstName);
                        codec.setSource(srcName);
                        process = pb.start();
                    }
                    int val = process.waitFor();
                    if (val != 0) {
                        System.err.println("ERROR: " + dstName + ": " + val);
                    }
                    synchronized (Transcoder.this) {
                        setChanged();
                        notifyObservers(val);
                    }
                    return val;
                };
                this.executor.submit(callable);
            } else {
                synchronized (Transcoder.this) {
                    setChanged();
                    notifyObservers(0);
                }
            }
        }

        for (File directory : currentSrc.listFiles(DIRECTORY_FILTER)) {
            this.transcode(directory, overwrite);
        }
    }

    public static void main(String[] args) {
        try {
            SwingUtilities.invokeAndWait(() -> new TranscoderGUI().setVisible(true));
            
        } catch (InterruptedException | InvocationTargetException ex) {
            Logger.getLogger(Transcoder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
