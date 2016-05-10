package com.pcapcb.ui.editor;

import com.pcapcb.pcb.format.PcbParseException;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMap;
import org.fife.ui.rsyntaxtextarea.TokenTypes;

import javax.swing.text.Segment;

/**
 * Created by pca006132 on 2016/5/9.
 */
public class PcbTokenMaker  extends AbstractTokenMaker{
    private int currentTokenStart;
    private int currentTokenType;

    public PcbTokenMaker() {
        super();
    }
    @Override
    public String[] getLineCommentStartAndEnd(int languageIndex) {
        return new String[] { "//", null };
    }

    @Override
    public TokenMap getWordsToHighlight() {
        return null;
    }

    @Override
    public Token getTokenList(Segment text, int startTokenType, final int startOffset) {
        resetTokenList();
        char[] array = text.array;
        int offset = text.offset;
        int count = text.count;
        int end = offset + count;

        boolean lineStart = true;
        boolean escape = false;
        // See, when we find a token, its starting position is always of the form:
        // 'startOffset + (currentTokenStart-offset)'; but since startOffset and
        // offset are constant, tokens' starting positions become:
        // 'newStartOffset+currentTokenStart' for one less subtraction operation.
        int newStartOffset = startOffset - offset;

        currentTokenStart = offset;
        currentTokenType  = startTokenType;
        for (int i = offset; i < end; i++) {
            char c = array[i];
            switch (currentTokenType) {
                case Token.NULL:
                    currentTokenStart = i;
                    switch (c) {
                        case ' ':
                        case '\t':
                            currentTokenType = Token.WHITESPACE;
                            break;
                        case '"':
                            currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            break;
                        case '/':
                            if (lineStart)
                                currentTokenType = Token.ERROR_CHAR;
                            break;
                        case '(':
                        case ')':
                        case '{':
                        case '}':
                        case '[':
                        case ']':
                            lineStart = false;
                            addToken(text, currentTokenStart,i, Token.SEPARATOR, newStartOffset+currentTokenStart);
                            currentTokenType = Token.NULL;
                            break;
                        case ',':
                        case ':':
                            lineStart = false;
                            addToken(text, currentTokenStart,i, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                            currentTokenType = Token.NULL;
                            break;
                        case '~':
                        case '-':
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                        case '.':
                            lineStart = false;                            
                            currentTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
                            break;
                        default:
                            currentTokenType = Token.IDENTIFIER;
                            break;
                    } //end of switch for c
                    break;
                case TokenTypes.WHITESPACE:
                    switch (c) {
                        case ' ':
                        case '\t':
                            //still whitespace
                            break;
                        case '"':
                            lineStart = false;
                            addToken(text, currentTokenStart,i-1, Token.WHITESPACE, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            break;
                        case '/':
                            if (lineStart)
                                currentTokenType = Token.ERROR_CHAR;
                            break;
                        case '(':
                        case ')':
                        case '{':
                        case '}':
                        case '[':
                        case ']':
                            lineStart = false;
                            addToken(text, currentTokenStart,i-1, Token.WHITESPACE, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            addToken(text, currentTokenStart,i, Token.SEPARATOR, newStartOffset+currentTokenStart);
                            currentTokenType = Token.NULL;
                            break;
                        case ':':
                        case ',':
                            lineStart = false;
                            addToken(text, currentTokenStart,i-1, Token.WHITESPACE, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            addToken(text, currentTokenStart,i, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                            currentTokenType = Token.NULL;
                            break;
                        case '~':
                        case '-':
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                        //case '.' won't have a . at starting point
                            lineStart = false;
                            addToken(text, currentTokenStart,i-1, Token.WHITESPACE, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
                            break;
                        default:
                            addToken(text, currentTokenStart,i-1, Token.WHITESPACE, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.IDENTIFIER;
                            break;
                    } //end of switch c
                    break;
                case TokenTypes.LITERAL_STRING_DOUBLE_QUOTE:
                    if (escape) {
                        escape = false;
                        break;
                    }
                    switch (c) {
                        case '"':
                            addToken(text, currentTokenStart,i, Token.LITERAL_STRING_DOUBLE_QUOTE, newStartOffset+currentTokenStart);
                            currentTokenStart = i + 1;
                            currentTokenType = Token.NULL;
                            break;
                        case '\\':
                            escape = true;
                            break;
                    }
                    break;
                case TokenTypes.LITERAL_NUMBER_DECIMAL_INT: //numbers, decimal and relative coor are also included
                    switch (c) {
                        case '-':
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                        case '.':
                            break;
                        case ' ':
                        case '\t':
                            addToken(text, currentTokenStart,i-1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.WHITESPACE;
                            break;
                        case '"':
                            addToken(text, currentTokenStart,i-1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            break;
                        case '(':
                        case ')':
                        case '{':
                        case '}':
                        case '[':
                        case ']':
                            addToken(text, currentTokenStart,i-1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            addToken(text, currentTokenStart,i, Token.SEPARATOR, newStartOffset+currentTokenStart);
                            currentTokenType = Token.NULL;
                            break;
                        case ',':
                        case ':':
                            addToken(text, currentTokenStart,i-1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            addToken(text, currentTokenStart,i, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                            currentTokenType = Token.NULL;
                            break;
                        default:
                            //all = identifier
                            currentTokenType = Token.IDENTIFIER;
                    }
                    break;                    
                case TokenTypes.IDENTIFIER: //everything else
                    switch (c) {
                        case ' ':
                        case '\t':
                            addToken(text, currentTokenStart,i-1, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.WHITESPACE;
                            lineStart = false;
                            break;
                        case ':':
                            if (lineStart) {
                                switch (i - currentTokenStart) {
                                    case 3:
                                        switch (array[i - 3]) {
                                            case 'i':
                                            case 'r':
                                                if (array[i-2] == 'c' && array[i-1] == 'b') {
                                                    addToken(text, currentTokenStart,i-1,
                                                            Token.RESERVED_WORD, newStartOffset+currentTokenStart);
                                                } else {
                                                    addToken(text, currentTokenStart,i-1,
                                                            Token.IDENTIFIER, newStartOffset+currentTokenStart);
                                                    lineStart = false;
                                                }
                                                break;
                                            default:
                                                addToken(text, currentTokenStart,i-1,
                                                        Token.IDENTIFIER, newStartOffset+currentTokenStart);
                                                lineStart = false;
                                                break;
                                        }
                                        break;
                                    case 4:
                                        switch (array[i - 4]) {
                                            case 'c':
                                                if (array[i - 3] == 'o' && array[i - 2] == 'n' &&
                                                        array[i - 1] == 'd') {
                                                    addToken(text, currentTokenStart,i-1,
                                                            Token.RESERVED_WORD, newStartOffset+currentTokenStart);
                                                }else {
                                                    addToken(text, currentTokenStart,i-1,
                                                            Token.IDENTIFIER, newStartOffset+currentTokenStart);
                                                    lineStart = false;
                                                }
                                                break;
                                            case 'd':
                                                if (array[i - 3] == 'a' && array[i - 2] == 't' &&
                                                        array[i - 1] == 'a') {
                                                    addToken(text, currentTokenStart,i-1,
                                                            Token.RESERVED_WORD, newStartOffset+currentTokenStart);
                                                }else {
                                                    addToken(text, currentTokenStart,i-1,
                                                            Token.IDENTIFIER, newStartOffset+currentTokenStart);
                                                    lineStart = false;
                                                }
                                                break;
                                            case 'i':
                                                if (array[i - 3] == 'n' && array[i - 2] == 'i' &&
                                                        array[i - 1] == 't') {
                                                    addToken(text, currentTokenStart,i-1,
                                                            Token.RESERVED_WORD, newStartOffset+currentTokenStart);
                                                }else {
                                                    addToken(text, currentTokenStart,i-1,
                                                            Token.IDENTIFIER, newStartOffset+currentTokenStart);
                                                    lineStart = false;
                                                }
                                                break;
                                            case 'm':
                                                if (array[i - 3] == 'a' && array[i - 2] == 'r' &&
                                                        array[i - 1] == 'k') {
                                                    addToken(text, currentTokenStart,i-1,
                                                            Token.RESERVED_WORD, newStartOffset+currentTokenStart);
                                                }else {
                                                    addToken(text, currentTokenStart,i-1,
                                                            Token.IDENTIFIER, newStartOffset+currentTokenStart);
                                                    lineStart = false;
                                                }
                                                break;
                                            case 's':
                                                if (array[i - 3] == 'i' && array[i - 2] == 'g' &&
                                                        array[i - 1] == 'n') {
                                                    addToken(text, currentTokenStart,i-1,
                                                            Token.RESERVED_WORD, newStartOffset+currentTokenStart);
                                                }else {
                                                    addToken(text, currentTokenStart,i-1,
                                                            Token.IDENTIFIER, newStartOffset+currentTokenStart);
                                                    lineStart = false;
                                                }
                                                break;
                                            default:
                                                addToken(text, currentTokenStart,i-1,
                                                        Token.IDENTIFIER, newStartOffset+currentTokenStart);
                                                lineStart = false;
                                                break;
                                        }
                                        break;
                                    case 5:
                                        switch (array[i - 5]) {
                                            case 'a':
                                                if (array[i - 4] == 'f' && array[i - 3] == 't' &&
                                                        array[i - 2] == 'e' && array[i - 1] == 'r') {
                                                    addToken(text, currentTokenStart,i-1,
                                                            Token.RESERVED_WORD, newStartOffset+currentTokenStart);
                                                }else {
                                                    addToken(text, currentTokenStart,i-1,
                                                            Token.IDENTIFIER, newStartOffset+currentTokenStart);
                                                    lineStart = false;
                                                }
                                                break;
                                            case 's':
                                                if (array[i - 4] == 't' && array[i - 3] == 'a' &&
                                                        array[i - 2] == 't' && array[i - 1] == 's') {
                                                    addToken(text, currentTokenStart,i-1,
                                                            Token.RESERVED_WORD, newStartOffset+currentTokenStart);
                                                }else {
                                                    addToken(text, currentTokenStart,i-1,
                                                            Token.IDENTIFIER, newStartOffset+currentTokenStart);
                                                    lineStart = false;
                                                }
                                                break;
                                            default:
                                                lineStart = false;
                                                addToken(text, currentTokenStart,i-1,
                                                        Token.IDENTIFIER, newStartOffset+currentTokenStart);
                                                break;
                                        }
                                        break;
                                    default:
                                        addToken(text, currentTokenStart,i-1, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                                        lineStart = false;
                                        break;
                                }
                            } // finish parsing prefixes
                            else {
                                addToken(text, currentTokenStart,i-1, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                                lineStart = false;
                            }
                            currentTokenStart = i;
                            addToken(text, currentTokenStart,i, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                            currentTokenType = Token.NULL;
                            break;
                        case ',':
                            addToken(text, currentTokenStart,i-1, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            addToken(text, currentTokenStart,i, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                            currentTokenType = Token.NULL;
                            break;
                        case '"':
                            addToken(text, currentTokenStart,i-1, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            lineStart = false;
                            break;
                        case '(':
                        case ')':
                        case '{':
                        case '}':
                        case '[':
                        case ']':
                            addToken(text, currentTokenStart,i-1, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            addToken(text, currentTokenStart,i, Token.SEPARATOR, newStartOffset+currentTokenStart);
                            currentTokenType = Token.NULL;
                            lineStart = false;
                            break;
                        //else: still identifier
                    }
                    break;
                case TokenTypes.ERROR_CHAR: // '/' char
                    if (lineStart && c == '/') {
                        currentTokenType = Token.COMMENT_EOL;
                        currentTokenStart = i - 1;
                        break;
                    } else if (lineStart && c == '*') {
                        currentTokenType = Token.COMMENT_MULTILINE;
                        currentTokenStart = i - 1;
                        break;
                    } else
                    switch (c) {
                        case '-':
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                        case '.':
                            addToken(text, currentTokenStart,i-1, Token.ERROR_CHAR, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
                            break;
                        case ' ':
                        case '\t':
                            addToken(text, currentTokenStart,i-1, Token.ERROR_CHAR, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.WHITESPACE;
                            break;
                        case '"':
                            addToken(text, currentTokenStart,i-1, Token.ERROR_CHAR, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            break;
                        case '(':
                        case ')':
                        case '{':
                        case '}':
                        case '[':
                        case ']':
                            addToken(text, currentTokenStart,i-1, Token.ERROR_CHAR, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            addToken(text, currentTokenStart,i, Token.SEPARATOR, newStartOffset+currentTokenStart);
                            currentTokenType = Token.NULL;
                            break;
                        case ',':
                        case ':':
                            addToken(text, currentTokenStart,i-1, Token.ERROR_CHAR, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            addToken(text, currentTokenStart,i, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                            currentTokenType = Token.NULL;
                            break;
                        default:
                            addToken(text, currentTokenStart,i-1, Token.ERROR_CHAR, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.IDENTIFIER;
                    }
                    break;
                case TokenTypes.COMMENT_EOL:
                    i = end - 1;
                    addToken(text, currentTokenStart,i, Token.COMMENT_EOL, newStartOffset+currentTokenStart);
                    // We need to set token type to null so at the bottom we don't add one more token.
                    currentTokenType = Token.NULL;
                    break;
                case TokenTypes.COMMENT_MULTILINE:
                    while (i < end - 1 && (array[i] == ' ' || array[i] == '\t' || array[i] == '*')) {
                        i++;
                        if (array[i - 1] == '*' && i < end)
                            if (array[i] == '/') {
                                i = end - 1;
                                addToken(text, currentTokenStart,i, Token.COMMENT_MULTILINE, newStartOffset+currentTokenStart);
                                currentTokenType = Token.NULL;
                            }
                    }
                    i = end - 1;
                    break;
            }
            if (lineStart)
                switch (currentTokenType) {
                    case Token.WHITESPACE:
                    case Token.NULL:
                    case Token.IDENTIFIER:
                    case Token.ERROR_CHAR:
                    case Token.COMMENT_EOL:
                    case Token.COMMENT_MULTILINE:
                        break;
                    default:
                        lineStart = false;
                }
        }
        // Deal with the (possibly there) last token.
        switch (currentTokenType) {

            // Remember what token type to begin the next line with.
            case Token.LITERAL_STRING_DOUBLE_QUOTE:
                addToken(text, currentTokenStart,end-1, currentTokenType, newStartOffset+currentTokenStart);
                break;
            case Token.COMMENT_MULTILINE:
                addToken(text, currentTokenStart,end-1, currentTokenType, newStartOffset+currentTokenStart);
                break;
            // Do nothing if everything was okay.
            case Token.NULL:
                addNullToken();
                break;

            // All other token types don't continue to the next line...
            default:
                addToken(text, currentTokenStart,end-1, currentTokenType, newStartOffset+currentTokenStart);
                addNullToken();

        }
        return firstToken;
    }

}
