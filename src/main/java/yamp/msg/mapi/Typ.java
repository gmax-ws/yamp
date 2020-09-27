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
package yamp.msg.mapi;

/**
 * Property Type constants
 */
public class Typ {
    public static final String PtypInteger16 = "0002";
    public static final String PtypInteger32 = "0003";
    public static final String PtypFloating32 = "0004";
    public static final String PtypFloating64 = "0005";
    public static final String PtypCurrency = "0006";
    public static final String PtypFloatingTime = "0007";
    public static final String PtypErrorCode = "000A";
    public static final String PtypBoolean = "000B";
    public static final String PtypInteger64 = "0014";
    public static final String PtypString = "001F";
    public static final String PtypString8 = "001E";
    public static final String PtypTime = "0040";
    public static final String PtypGuid = "0048";
    public static final String PtypServerId = "00FB";
    public static final String PtypRestriction = "00FD";
    public static final String PtypRuleAction = "00FE";
    public static final String PtypBinary = "0102";
    public static final String PtypMultipleInteger16 = "1002";
    public static final String PtypMultipleInteger32 = "1003";
    public static final String PtypMultipleFloating32 = "1004";
    public static final String PtypMultipleFloating64 = "1005";
    public static final String PtypMultipleCurrency = "1006";
    public static final String PtypMultipleFloatingTime = "1007";
    public static final String PtypMultipleInteger64 = "1014";
    public static final String PtypMultipleString = "101F";
    public static final String PtypMultipleString8 = "101E";
    public static final String PtypMultipleTime = "1040";
    public static final String PtypMultipleGuid = "1048";
    public static final String PtypMultipleBinary = "1102";
    public static final String PtypUnspecified = "0000";
    public static final String PtypNull = "0001";
    public static final String PtypObject = "000D";
}
