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
package yamp.msg.util;

import java.util.Arrays;
import java.util.function.Function;

public class Utils {
    @FunctionalInterface
    public interface ByteArrayFunction<R> {
        R apply(byte[] param);
    }

    public static byte[] copyBytes(byte[] original, int from, int to) {
        return Arrays.copyOfRange(original, from, to);
    }

    public static <R> R ifNullBytes(byte[] param, ByteArrayFunction<R> func) {
        return param == null ? null : func.apply(param);
    }

    public static <T, R> R ifNull(T param, Function<T, R> func) {
        return param == null ? null : func.apply(param);
    }

    public static <T> T nvl(T param1, T param2) {
        return (param1 == null) ? param2 : param1;
    }
}
