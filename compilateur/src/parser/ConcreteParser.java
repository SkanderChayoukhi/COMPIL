package parser;

import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class ConcreteParser {
    public static ParseTree analyze(InputStream is) throws IOException {
        ANTLRInputStream input = new ANTLRInputStream(is);
        /* Basic connection to ANTLR (does not capture syntactic errors)
        CalcLexer lexer = new CalcLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CalcParser parser = new CalcParser(tokens);
         */
        PCFLexer lexer = new ReportingPCFLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PCFParser parser = new PCFParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new ErrorListener());

        // generating parse tree
        ParseTree tree = parser.term();
        System.out.println("ANTLR Syntax Tree: " + tree.toStringTree(parser));
        if (ErrorFlag.getFlag()) // exit in case of an error
            throw new SyntaxError(ErrorFlag.getMsg());
        else
            return tree;
    }// else visit parse tree to generate AST   
}
