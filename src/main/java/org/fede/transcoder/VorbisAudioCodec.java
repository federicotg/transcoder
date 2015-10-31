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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class VorbisAudioCodec extends AudioCodec {

    private static final List<String> ARGUMENTS = Collections.unmodifiableList(Arrays.asList(new String[]{
        "oggenc",
        "-Q",
        "-q",
        "quality",
        "-o",
        "dst",
        "src"}));


    @Override
    public List<String> getArguments() {

        List<String> answer = new ArrayList<>(ARGUMENTS);
        answer.set(3, this.getQuality());
        answer.set(5, this.getDestination());
        answer.set(6, this.getSource());
        return answer;
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
    public String getDefaultQualityString() {
        return "3";
    }

    @Override
    public Optional<String> getScript() {
        return Optional.empty();
    }
}
