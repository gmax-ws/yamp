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
package yamp.msg.common;

import org.apache.poi.poifs.filesystem.DirectoryEntry;

import java.util.Iterator;
import java.util.Map;

public abstract class AbstractContainer<T> implements Iterator<T>, Iterable<T> {

    private int index = 0;

    private final Map<String, DirectoryEntry> data;

    public AbstractContainer(Map<String, DirectoryEntry> data) {
        this.data = data;
    }

    @Override
    public boolean hasNext() {
        return index < size();
    }

    @Override
    public T next() {
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }

    protected String folderName(String prefix, int index) {
        return String.format("%s%08d", prefix, index);
    }

    protected DirectoryEntry get(String folderName) {
        return this.data.get(folderName);
    }

    protected int size() {
        return this.data.size();
    }

    protected int nextIndex() {
        return index++;
    }
}
