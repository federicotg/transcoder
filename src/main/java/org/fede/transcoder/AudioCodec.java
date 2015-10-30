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

import java.util.List;

public interface AudioCodec {
    public void setQuality(String quality);

    public void setSource(String src);

    public void setDestination(String dst);

    public List<String> getArguments();

    public int getMinumumQuality();
    public int getMaximumQuality();
    public int getDefaultQuality();
    public double getStepSize();
    public String getExtension();
    public List<String> getQualities();
    public String getDefaultQualityString();
}