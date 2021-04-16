/* MIT License
 *
 * Copyright (c) 2021 Stefan Frei
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.schlibbuz.commons.coreutils.hex;

/**
 * Hex-Dump.
 * @author Stefan Frei <p>stefan.a.frei@gmail.com</p>
 */
public class HexDump {

    private static final byte ADDR_LENGTH = 8;
    private static final byte AREA_SEP_WIDTH = 5;
    private static final byte BODY_COLS = 16;
    private static final byte BODY_COL_WIDTH = 2;
    private static final byte TBL_HDR_HEIGHT = 2;
    private static final byte TBL_WIDTH = getTableWidth();
    private static final char BODY_COL_SEP = ' ';
    private static final char HDR_SEP_HORIZ = '-';
    private static final char HDR_SEP_SIDE = ' ';
    private static final char HDR_SEP_VERT = '|';
    private static final char NEW_LINE = '\n';
    private static final String AREA_SEP = genAreaSep();
    private static final String ADDR_BASE = "00000000";
    private static final String TBL_HDR = genTableHeader();
    private static final String TBL_LEFT_GAP = "     ";

    private final char[] buf;
    private final int bufHeight;
    private final int effBufLen;
    private final int tableSize;
    private final StringBuilder table;


    HexDump(char[] buf) {
        this.buf = buf;
        bufHeight = getBufHeight();
        effBufLen = getEffectiveBufferLength(buf);
        tableSize = getTableSize();
        table = new StringBuilder(tableSize);
    }

    public static HexDump of(char[] buf) {
        return new HexDump(buf);
    }

    private static byte getTableWidth() {
        return (byte)(
                ADDR_LENGTH
                + TBL_LEFT_GAP.length()
                // last separator abused as \n
                + (BODY_COLS * (BODY_COL_WIDTH + 1))
            );
    }

    private static String genAreaSep() {
        byte sideLen = AREA_SEP_WIDTH / 2;
        boolean even = AREA_SEP_WIDTH % 2 == 0;
        if (even) sideLen -= 1;
        StringBuilder s = new StringBuilder(AREA_SEP_WIDTH);
        for (int index = 0; index < sideLen; index++) s.append(HDR_SEP_SIDE);
        for (int index = 0; index < AREA_SEP_WIDTH - 2 * sideLen; index++)
            s.append(HDR_SEP_VERT);
        for (int index = 0; index < sideLen; index++) s.append(HDR_SEP_SIDE);
        return s.toString();
    }

    private static String genTableHeader() {
        StringBuilder tableHeader = new StringBuilder(TBL_WIDTH * TBL_HDR_HEIGHT);
        appendTableHeaderLeft(tableHeader);
        appendTableHeaderGap(tableHeader);
        appendTableHeaderCenter(tableHeader);
        appendTableHeaderSepHorizontal(tableHeader);
        return tableHeader.toString();
    }

    private static StringBuilder appendTableHeaderLeft(StringBuilder tableHeader) {
        return tableHeader.append("  ADDR  ");
    }

    private static StringBuilder appendTableHeaderGap(StringBuilder tableHeader) {
        return tableHeader.append(AREA_SEP);
    }

    private static StringBuilder appendTableHeaderCenter(StringBuilder tableHeader) {
        for (int col = 0; col < BODY_COLS; col++) {
            tableHeader
                    .append('0')
                    .append(Integer.toHexString(col).toUpperCase())
                    .append(BODY_COL_SEP);
        }
        return tableHeader.replace(
                tableHeader.length() - 1,
                tableHeader.length(),
                String.valueOf(NEW_LINE)
        );
    }

    private static StringBuilder appendTableHeaderSepHorizontal(StringBuilder tableHeader) {
        for (int col = 0; col < TBL_WIDTH; col++) {
            tableHeader.append(HDR_SEP_HORIZ);
        }
        return tableHeader.replace(
                tableHeader.length() - 1,
                tableHeader.length(),
                String.valueOf(NEW_LINE)
        );
    }

    final int getEffectiveBufferLength(char[] buf) {
        int length = 0;
        int intVal;
        for (int index = 0; index < buf.length; index++) {
            intVal = (int)buf[index];
            if (intVal <= Byte.MAX_VALUE) {
                length++;
            } else {
                length += 2; // as of java 11 'FF FF' is theoretical max.
            }
        }
        return length;
    }

    private int getBufHeight() {
        return effBufLen % BODY_COLS == 0
                ? effBufLen / BODY_COLS : effBufLen / BODY_COLS + 1;
    }

    final int getTableSize() {
        return (TBL_HDR_HEIGHT + bufHeight) * TBL_WIDTH
                - (effBufLen % BODY_COLS == 0
                ? 0 : BODY_COLS - effBufLen % BODY_COLS);
    }

    private String createTable() {
        table.append(TBL_HDR);
        hex();
        return table.toString();
    }

    private String makeAddr(int addr) {
        StringBuilder hexAddr = new StringBuilder(8);
        hexAddr.append(Integer.toHexString(addr).toUpperCase());
        return hexAddr.insert(0, ADDR_BASE.substring(hexAddr.length())).toString();
    }

    private byte appendHexVal(char val) {
        StringBuilder s = new StringBuilder(4);
        s.append(Integer.toHexString((int)val).toUpperCase());

        if (s.length() % 2 == 1) s.insert(0, '0');

        for (int index = 0; index < s.length(); index+=2) {

        }
        return (byte)(s.length()/2);
    }

    void hex() {
        int bufIndex = 0;
        int addr = 0;
        while(bufIndex < buf.length) {
            StringBuilder line = new StringBuilder(TBL_WIDTH);
            line.append(makeAddr(addr)).append(AREA_SEP);
            addr += BODY_COLS;
            for (byte bytesPut = 0; bytesPut < BODY_COLS && bufIndex < buf.length;) {
                StringBuilder hexVal = new StringBuilder(4);
                hexVal.append(Integer.toHexString((int)buf[bufIndex]).toUpperCase());
                if (hexVal.length() % 2 == 1) hexVal.insert(0, '0');
                for (int index = 0; index < hexVal.length(); index+=2) {
                    line.append(hexVal.substring(index, index + 2)).append(BODY_COL_SEP);
                    bytesPut++;
                }
                bufIndex++;
            }
            table.append(line.deleteCharAt(line.length() - 1).append(NEW_LINE));
        }
    }

    @Override
    public String toString() {
        return createTable();
    }
}
