#!/bin/sh

#
# Copyright (C) 2015 Federico Tello Gentile <federicotg@gmail.com>
#
# This program is free software; you can redistribute it and/or
# modify it under the terms of the GNU General Public License
# as published by the Free Software Foundation; either version 2
# of the License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
#


ARTIST=`metaflac "$2" --show-tag=ARTIST | sed s/.*=//g`
TITLE=`metaflac "$2" --show-tag=TITLE | sed s/.*=//g`
ALBUM=`metaflac "$2" --show-tag=ALBUM | sed s/.*=//g`
GENRE=`metaflac "$2" --show-tag=GENRE | sed s/.*=//g`
TRACKNUMBER=`metaflac "$2" --show-tag=TRACKNUMBER | sed s/.*=//g`
DATE=`metaflac "$2" --show-tag=DATE | sed s/.*=//g`
mkdir -p "$3"
flac -s -c -d "$2" | lame -S -V$1 --add-id3v2 --pad-id3v2 --ignore-tag-errors --tt "$TITLE" --tn "${TRACKNUMBER:-0}" --ta "$ARTIST" --tl "$ALBUM" --ty "$DATE" --tg "${GENRE:-12}" - "$3/$4"

