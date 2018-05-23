package lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Scanner {
    // return tokens as an Iterator
    public static Iterator<Token> scan(String input) throws FileNotFoundException {
        ScanContext context = new ScanContext(input);
        return new TokenIterator(context);
    }

    // return tokens as a Stream 
    public static Stream<Token> stream(String input2) throws FileNotFoundException {
        Iterator<Token> tokens = scan(input2);
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(tokens, Spliterator.ORDERED), false);
    }
}