package cpp;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.util.List;

public class AntlrParser {

    public static void main(String[] args) throws IOException {
        CharStream charStream = CharStreams.fromFileName("src/cpp/HelloWorld.cc");
        CPP14Lexer cpp14Lexer = new CPP14Lexer(charStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(cpp14Lexer);
        CPP14Parser cpp14Parser = new CPP14Parser(commonTokenStream);
        CPP14Parser.TranslationunitContext translationunit = cpp14Parser.translationunit();

        // set breakpoint at here, look translationunit children.
        // you can see the parse tree.
        List<ParseTree> children = translationunit.children;
        printTree(children);
    }

    private static void printTree(List<ParseTree> children) {
        if (children == null || children.isEmpty()) {
            return;
        }
        for (ParseTree child : children) {
            printChild(child);
        }
    }

    private static void printChild(ParseTree child) {
        if (child == null) {
            return;
        }
        System.out.println(child.getText());
        int count = child.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree subChild = child.getChild(i);
            printChild(subChild);
        }
    }
}
