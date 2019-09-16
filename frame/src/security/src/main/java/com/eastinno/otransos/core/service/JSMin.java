package com.eastinno.otransos.core.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;

public class JSMin {
    private static final int EOF = -1;
    private PushbackInputStream in;
    private OutputStream out;
    private int theA;
    private int theB;

    public JSMin(InputStream in, OutputStream out) {
        this.in = new PushbackInputStream(in);
        this.out = out;
    }

    static boolean isAlphanum(int c) {
        return ((c >= 97) && (c <= 122)) || ((c >= 48) && (c <= 57)) || ((c >= 65) && (c <= 90)) || (c == 95) || (c == 36) || (c == 92)
                || (c > 126);
    }

    int get() throws IOException {
        int c = this.in.read();

        if ((c >= 32) || (c == 10) || (c == -1)) {
            return c;
        }

        if (c == 13) {
            return 10;
        }

        return 32;
    }

    int peek() throws IOException {
        int lookaheadChar = this.in.read();
        this.in.unread(lookaheadChar);
        return lookaheadChar;
    }

    int next() throws IOException, JSMin.UnterminatedCommentException {
        int c = get();
        if (c == 47) {
            switch (peek()) {
            case 47:
                do
                    c = get();
                while (c > 10);
                return c;
            case 42:
                get();
                while (true) {
                    switch (get()) {
                    case 42:
                        if (peek() == 47) {
                            get();
                            return 32;
                        }
                        break;
                    case -1:
                        throw new UnterminatedCommentException();
                    }
                }
            }

            return c;
        }

        return c;
    }

    void action(int d) throws IOException, JSMin.UnterminatedRegExpLiteralException, JSMin.UnterminatedCommentException,
            JSMin.UnterminatedStringLiteralException {
        switch (d) {
        case 1:
            this.out.write(this.theA);
        case 2:
            this.theA = this.theB;

            if ((this.theA == 39) || (this.theA == 34)) {
                while (true) {
                    this.out.write(this.theA);
                    this.theA = get();
                    if (this.theA == this.theB) {
                        break;
                    }
                    if (this.theA <= 10) {
                        throw new UnterminatedStringLiteralException();
                    }
                    if (this.theA == 92) {
                        this.out.write(this.theA);
                        this.theA = get();
                    }
                }
            }

        case 3:
            this.theB = next();
            if ((this.theB == 47)
                    && ((this.theA == 40) || (this.theA == 44) || (this.theA == 61) || (this.theA == 58) || (this.theA == 91)
                            || (this.theA == 33) || (this.theA == 38) || (this.theA == 124) || (this.theA == 63) || (this.theA == 123)
                            || (this.theA == 125) || (this.theA == 59) || (this.theA == 10))) {
                this.out.write(this.theA);
                this.out.write(this.theB);
                while (true) {
                    this.theA = get();
                    if (this.theA == 47)
                        break;
                    if (this.theA == 92) {
                        this.out.write(this.theA);
                        this.theA = get();
                    } else if (this.theA <= 10) {
                        throw new UnterminatedRegExpLiteralException();
                    }
                    this.out.write(this.theA);
                }
                this.theB = next();
            }
            break;
        }
    }

    public void jsmin() throws IOException, JSMin.UnterminatedRegExpLiteralException, JSMin.UnterminatedCommentException,
            JSMin.UnterminatedStringLiteralException {
        this.theA = 10;
        action(3);
        while (this.theA != -1) {
            switch (this.theA) {
            case 32:
                if (isAlphanum(this.theB))
                    action(1);
                else {
                    action(2);
                }
                break;
            case 10:
                switch (this.theB) {
                case 40:
                case 43:
                case 45:
                case 91:
                case 123:
                    action(1);
                    break;
                case 32:
                    action(3);
                    break;
                default:
                    if (isAlphanum(this.theB))
                        action(1);
                    else
                        action(2);
                    break;
                }
                break;
            default:
                switch (this.theB) {
                case 32:
                    if (isAlphanum(this.theA)) {
                        action(1);
                    } else
                        action(3);
                    break;
                case 10:
                    switch (this.theA) {
                    case 34:
                    case 39:
                    case 41:
                    case 43:
                    case 45:
                    case 93:
                    case 125:
                        action(1);
                        break;
                    default:
                        if (isAlphanum(this.theA))
                            action(1);
                        else
                            action(3);
                        break;
                    }
                    break;
                default:
                    action(1);
                }
                break;
            }
        }
        this.out.flush();
    }

    public static void main(String[] arg) {
        try {
            JSMin jsmin = new JSMin(new FileInputStream(arg[0]), System.out);
            jsmin.jsmin();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnterminatedRegExpLiteralException e) {
            e.printStackTrace();
        } catch (UnterminatedCommentException e) {
            e.printStackTrace();
        } catch (UnterminatedStringLiteralException e) {
            e.printStackTrace();
        }
    }

    class UnterminatedCommentException extends Exception {
        UnterminatedCommentException() {
        }
    }

    class UnterminatedRegExpLiteralException extends Exception {
        UnterminatedRegExpLiteralException() {
        }
    }

    class UnterminatedStringLiteralException extends Exception {
        UnterminatedStringLiteralException() {
        }
    }
}
