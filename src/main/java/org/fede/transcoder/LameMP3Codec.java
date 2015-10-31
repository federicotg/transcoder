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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class LameMP3Codec extends AudioCodec {

    private static final String SCRIPT_NAME = "/flactomp3.sh";

    private static final List<String> ARGUMENTS = Collections.unmodifiableList(Arrays.asList(new String[]{
        System.getProperty("java.io.tmpdir") + SCRIPT_NAME,
        "quality",
        "src",
        "dstDir",
        "dstFile"
    }));

    @Override
    public List<String> getArguments() {
        List<String> answer = new ArrayList<>(ARGUMENTS);
        answer.set(1, this.getQuality());
        answer.set(2, this.getSource());
        final String dst = this.getDestination();
        int slashPos = dst.lastIndexOf(File.separatorChar);
        if (slashPos != -1) {
            answer.set(3, dst.substring(0, slashPos));
            answer.set(4, dst.substring(slashPos + 1));
        } else {
            throw new IllegalArgumentException("Destination must have slashes (" + File.separatorChar + ")");
        }
        return answer;
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
    public String getDefaultQualityString() {
        return "4";
    }

    @Override
    public Optional<String> getScript() {
        return Optional.of(SCRIPT_NAME);
    }
}
