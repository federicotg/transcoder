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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class VorbisAudioCodec implements AudioCodec {

    private List<String> arguments;
    private static final List<String> QUALITIES = new ArrayList<>(
            Arrays.asList(new String[]{
                "-1",
                "-0.9", "-0.8", "-0.7", "-0.6", "-0.5", "-0.4", "-0.3", "-0.2", "-0.1",
                "0.0", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9",
                "1.0", "1.1", "1.2", "1.3", "1.4", "1.5", "1.6", "1.7", "1.8", "1.9",
                "2.0", "2.1", "2.2", "2.3", "2.4", "2.5", "2.6", "2.7", "2.8", "2.9",
                "3.0", "3.1", "3.2", "3.3", "3.4", "3.5", "3.6", "3.7", "3.8", "3.9",
                "4.0", "4.1", "4.2", "4.3", "4.4", "4.5", "4.6", "4.7", "4.8", "4.9",
                "5.0", "5.1", "5.2", "5.3", "5.4", "5.5", "5.6", "5.7", "5.8", "5.9",
                "6.0", "6.1", "6.2", "6.3", "6.4", "6.5", "6.6", "6.7", "6.8", "6.9",
                "7.0", "7.1", "7.2", "7.3", "7.4", "7.5", "7.6", "7.7", "7.8", "7.9",
                "8.0", "8.1", "8.2", "8.3", "8.4", "8.5", "8.6", "8.7", "8.8", "8.9",
                "9.0", "9.1", "9.2", "9.3", "9.4", "9.5", "9.6", "9.7", "9.8", "9.9",
                "10.0"
            }));

    public VorbisAudioCodec() {
        arguments = new ArrayList<>(7);
        arguments.add("oggenc");
        arguments.add("-Q");
        arguments.add("-q");
        arguments.add("quality");
        arguments.add("-o");
        arguments.add("dst");
        arguments.add("src");
    }

    @Override
    public void setQuality(String quality) {
        arguments.set(3, quality);
    }

    @Override
    public void setSource(String src) {
        arguments.set(6, src);
    }

    @Override
    public void setDestination(String dst) {
        arguments.set(5, dst);
    }

    @Override
    public List<String> getArguments() {
        return this.arguments;
    }

    @Override
    public String toString() {
        return "Vorbis";
    }

    @Override
    public int getMinumumQuality() {
        return -1;
    }

    @Override
    public int getMaximumQuality() {
        return 10;
    }

    @Override
    public int getDefaultQuality() {
        return 3;
    }

    @Override
    public String getExtension() {
        return ".ogg";
    }

    @Override
    public double getStepSize() {
        return 0.1d;
    }

    @Override
    public List<String> getQualities() {
        return QUALITIES;
    }

    @Override
    public String getDefaultQualityString() {
        return "3";
    }
    
    @Override
    public Optional<String> getScript() {
        return Optional.empty();
    }
}
