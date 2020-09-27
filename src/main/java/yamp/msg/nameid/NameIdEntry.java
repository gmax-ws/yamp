/*
 * Outlook .MSG file parser
 * Copyright (c) 2020 Scalable Solutions
 *
 * author: Marius Gligor <marius.gligor@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111, USA.
 */
package yamp.msg.nameid;

public class NameIdEntry {
    private final int nameId;
    private final int guidIndexOrig;
    private final int guidIndex;
    private final int propIndex;
    private final int kind;

    public NameIdEntry(int nameId, int guidIndex, int propIndex) {
        this.nameId = nameId;
        this.guidIndexOrig = guidIndex;
        this.kind = guidIndex & 1;
        this.guidIndex = guidIndex >> 1;
        this.propIndex = propIndex;
    }

    public int getNameId() {
        return nameId;
    }

    public int getGuidIndex() {
        return guidIndex;
    }

    public int getPropIndex() {
        return propIndex;
    }

    public int getKind() {
        return kind;
    }

    public int getGuidIndexOrig() {
        return guidIndexOrig;
    }
}
