package ija.robots.load;

import java.util.Scanner;

/**
 * Class for tokenizing saved room file
 */
public class LoaderLex {
    private String res = "";
    private char cur;
    private Scanner scan;

    /**
     * Creates new loader lexer
     * @param scan scanner to use for loading
     */
    public LoaderLex(Scanner scan) {
        scan.useDelimiter("");
        cur = ' ';
        if (scan.hasNext())
            cur = scan.next().charAt(0);

        this.scan = scan;
    }

    /**
     * Gets next token in the file
     * @return next token
     * @throws Exception when unexpected character in file
     */
    public Token next() throws Exception {
        res = "";
        if (skipWhitespace() == '\0')
            return Token.Eof;

        res += cur;
        var token = Token.Eof;
        switch (cur) {
            case '{':
                token = Token.OptStart;
                break;
            case '}':
                token = Token.OptEnd;
                break;
            case '[':
                token = Token.PosStart;
                break;
            case ']':
                token = Token.PosEnd;
                break;
            case 'x':
                token = Token.X;
                break;
            case ',':
                token = Token.Colon;
                break;
            default:
                if (Character.isDigit(cur)) {
                    return readNum();
                } else if (cur == '-') {
                    return readNegNum();
                } else if (Character.isAlphabetic(cur)) {
                    token = readIdent();
                } else {
                    throw new Exception(
                        "Unexpected character '" + cur + "' in file"
                    );
                }
        }
        cur = ' ';
        if (scan.hasNext())
            nextChar();
        return token;
    }

    /**
     * Returns last token value
     * @return last token value
     */
    public String getString() {
        return res;
    }

    /**
     * Returns double representation of last token
     * @return last token double representation
     */
    public double getNum() {
        return Double.parseDouble(res);
    }

    private Token readNum() {
        boolean f = false;
        while (scan.hasNext()) {
            nextChar();
            if (cur == '.') {
                if (f)
                    break;
                f = true;
            } else if (!Character.isDigit(cur)) {
                break;
            }
            res += cur;
        }
        return Token.Number;
    }

    private Token readNegNum() throws Exception {
        if (!Character.isDigit(nextChar()))
            throw new Exception("Unexpected character '-' in file");

        res += cur;
        return readNum();
    }

    private Token readIdent() throws Exception {
        while (scan.hasNext()) {
            nextChar();
            if (!Character.isAlphabetic(cur) && cur != '_')
                break;
            res += cur;
        }

        if (cur != ':' && skipWhitespace() != ':')
            throw new Exception("Identifier must be followed by ':'");

        return Token.Ident;
    }

    private char nextChar() {
        return cur = scan.next().charAt(0);
    }

    private char skipWhitespace() {
        while (Character.isWhitespace(cur)) {
            if (!scan.hasNext())
                return '\0';
            nextChar();
        }
        return cur;
    }
}
