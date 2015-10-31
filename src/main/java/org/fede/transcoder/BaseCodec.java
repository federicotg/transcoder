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

/**
 *
 * @author Federico Tello Gentile <federicotg@gmail.com>
 */
public abstract class BaseCodec implements Codec{
    
    private String source;
    private String destination;

    @Override
    public final void setSource(String source) {
        this.source = source;
    }

    @Override
    public final void setDestination(String destination) {
        this.destination = destination;
    }

    protected String getSource() {
        return source;
    }

    protected String getDestination() {
        return destination;
    }
    
    
    
}
