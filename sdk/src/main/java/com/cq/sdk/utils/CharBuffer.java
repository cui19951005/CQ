package com.cq.sdk.utils;


import java.io.IOException;
import java.util.Arrays;

/**
 * Created by admin on 2016/8/25.
 */
public final class CharBuffer  implements Appendable {

    int defaultLength =28;

    char[] value;


    int count;


    CharBuffer() {
        this.value=new char[this.defaultLength];
    }


    CharBuffer(int capacity) {
        value = new char[capacity];
    }

    public int length() {
        return count;
    }

    private void ensureCapacityInternal(int minimumCapacity) {
        // overflow-conscious code
        if (minimumCapacity - value.length > 0) {
            value = Arrays.copyOf(value,minimumCapacity+this.defaultLength);
        }
    }


    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    public void setLength(int newLength) {
        if (count < newLength) {
            Arrays.fill(value, count, newLength, '\0');
        }

        count = newLength;
    }

    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin)
    {
        if (srcBegin < 0)
            throw new StringIndexOutOfBoundsException(srcBegin);
        if ((srcEnd < 0) || (srcEnd > count))
            throw new StringIndexOutOfBoundsException(srcEnd);
        if (srcBegin > srcEnd)
            throw new StringIndexOutOfBoundsException("srcBegin > srcEnd");
        System.arraycopy(value, srcBegin, dst, dstBegin, srcEnd - srcBegin);
    }



    public CharBuffer append(Object obj) {
        return append(String.valueOf(obj));
    }


    public CharBuffer append(String str) {
        if (str == null)
            return appendNull();
        int len = str.length();
        ensureCapacityInternal(count + len);
        str.getChars(0, len, value, count);
        count += len;

        return this;
    }

    // Documentation in subclasses because of synchro difference
    public CharBuffer append(StringBuffer sb) {
        if (sb == null)
            return appendNull();
        int len = sb.length();
        ensureCapacityInternal(count + len);
        sb.getChars(0, len, value, count);
        count += len;
        return this;
    }
    private CharBuffer appendNull() {
        int c = count;
        ensureCapacityInternal(c + 4);
        final char[] value = this.value;
        value[c++] = 'n';
        value[c++] = 'u';
        value[c++] = 'l';
        value[c++] = 'l';
        count = c;
        return this;
    }


    public Appendable append(CharSequence csq) throws IOException {
        return this.append(csq,0,csq.length());
    }

    public CharBuffer append(CharSequence s, int start, int end) {
        if (s == null)
            s = "null";
        if ((start < 0) || (start > end) || (end > s.length()))
            throw new IndexOutOfBoundsException(
                    "start " + start + ", end " + end + ", s.length() "
                            + s.length());
        int len = end - start;
        ensureCapacityInternal(count + len);
        for (int i = start, j = count; i < end; i++, j++)
            value[j] = s.charAt(i);
        count += len;
        return this;
    }


    public CharBuffer append(char[] str) {
        ensureCapacityInternal(count + str.length);
        System.arraycopy(str, 0, value, count, str.length);
        count += str.length;
        return this;
    }
    public CharBuffer replace(char[] chars,int start,int end){
        System.arraycopy(chars,0,this.value,start,end-start);
        return this;
    }

    public CharBuffer append(boolean b) {
        if (b) {
            ensureCapacityInternal(count + 4);
            value[count++] = 't';
            value[count++] = 'r';
            value[count++] = 'u';
            value[count++] = 'e';
        } else {
            ensureCapacityInternal(count + 5);
            value[count++] = 'f';
            value[count++] = 'a';
            value[count++] = 'l';
            value[count++] = 's';
            value[count++] = 'e';
        }
        return this;
    }

    public CharBuffer append(char c) {
        ensureCapacityInternal(count + 1);
        value[count++] = c;
        return this;
    }


    public CharBuffer append(int i) {
        this.append(CharUtils.valueOf(i));
        return this;
    }


    public CharBuffer append(long l) {
        this.append(CharUtils.valueOf(l));
        return this;
    }

    public char[] substring(int start) {
        return substring(start, count);
    }



    public char[] substring(int start, int end) {
        return CharUtils.subChars(value, start, end);
    }

    public int indexOf(char[] str) {
        return indexOf(str, 0);
    }


    public int indexOf(char[] str, int fromIndex) {
        return CharUtils.indexOf(this.value,str,fromIndex);
    }


    public String toString(){
        return new String(this.value);
    }


    public final char[] getValue() {
        return Arrays.copyOfRange(this.value,0,this.count);
    }

}
