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

public class AoTuVAudioCodec implements AudioCodec {

    private static final List<String> QUALITIES = new ArrayList<>(
            Arrays.asList(new String[]{"-2","-1", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"}));
    
    private List<String> arguments;

    public AoTuVAudioCodec() {
        arguments = new ArrayList<>(7);
        arguments.add("/tmp/aotuv.sh");
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
        return "Vorbis (AoTuV)";
    }

    @Override
    public int getMinumumQuality() {
        return -2;
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
        return Optional.of("/aotuv.sh");
    }
}
