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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LameMP3Codec implements AudioCodec {

    private static final List<String> QUALITIES = new ArrayList<>(
            Arrays.asList(new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"}));
    private List<String> arguments;

    public LameMP3Codec() {
        arguments = new ArrayList<>(7);
        arguments.add("/home/fede/bin/flactomp3.sh");
        arguments.add("quality");
        arguments.add("src");
        arguments.add("dstDir");
        arguments.add("dstFile");
    }

    @Override
    public void setQuality(String quality) {
        arguments.set(1, quality);
    }

    @Override
    public void setSource(String src) {
        arguments.set(2, src);
    }

    @Override
    public void setDestination(String dst) {
        int slashPos = dst.lastIndexOf(File.separatorChar);
        if (slashPos != -1) {
            arguments.set(3, dst.substring(0, slashPos));
            arguments.set(4, dst.substring(slashPos + 1));
        } else {
            throw new IllegalArgumentException("Destination must have slashes (" + File.separatorChar + ")");
        }
    }

    @Override
    public List<String> getArguments() {
        return this.arguments;
    }

    @Override
    public String toString() {
        return "MP3 (Lame)";
    }

    @Override
    public int getMinumumQuality() {
        return 0;
    }

    @Override
    public int getMaximumQuality() {
        return 9;
    }

    @Override
    public int getDefaultQuality() {
        return 4;
    }

    @Override
    public double getStepSize() {
        return 1.0d;
    }

    @Override
    public String getExtension() {
        return ".mp3";
    }

    @Override
    public List<String> getQualities() {
        return QUALITIES;
    }

    @Override
    public String getDefaultQualityString() {
        return "4";
    }
}
