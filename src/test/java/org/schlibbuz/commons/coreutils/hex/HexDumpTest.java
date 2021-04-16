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

import static org.testng.Assert.*;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test for class HexDump.
 * @author Stefan Frei <email>stefan.a.frei@gmail.com</email>
 */
public class HexDumpTest {

    @DataProvider(name = "getEffectiveBufferLength")
    public static Object[][] buildJsonMap() {
        return new Object[][] {
            {new char[]{'λ'}, 2},
            {new char[]{'a'}, 1},
            {new char[]{'€'}, 2},
        };
    }
    @Test(dataProvider = "getEffectiveBufferLength")
    public void getEffectiveBufferLength(char[] cbuf, int expected) {
        var i = new HexDump(cbuf);
        assertEquals(i.getEffectiveBufferLength(cbuf), expected);
    }

    @DataProvider(name = "bufToString")
    public static Object[][] bufToString() {
        return new Object[][] {
            {new char[]{'λ'}, 2},
            {new char[]{'a'}, 1},
            {new char[]{'€'}, 2},
            {new char[300], 2},
            {new char[]{'€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€','€',}, 2},
        };
    }
    @Test(dataProvider = "bufToString")
    public void bufToString(char[] cbuf, int blaa) {
        System.out.println(HexDump.of(cbuf));
    }
}
