package com.amazon.opendistroforelasticsearch.sql.antlr;

import com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlLexer;
import com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser;
import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.OpenDistroSemanticAnalyzer;
import com.amazon.opendistroforelasticsearch.sql.antlr.syntax.CaseChangingCharStream;
import com.amazon.opendistroforelasticsearch.sql.antlr.syntax.SyntaxAnalysisException;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Facade for ANTLR generated parser to avoid boilerplate code.
 */
public class OpenDistroSqlAnalyzer {

    /** Original sql query */
    private final String sql;

    public OpenDistroSqlAnalyzer(String sql) {
        this.sql = sql;
    }

    /**
     * Generate parse tree for the query to perform syntax and semantic analysis.
     * Runtime exception with clear message is thrown for any verification error.
     */
    public void analyze() {
        analyzeSemantic(
            analyzeSyntax(
                createParser(
                    createLexer())));
    }

    private OpenDistroSqlParser createParser(Lexer lexer) {
        return new OpenDistroSqlParser(
                   new CommonTokenStream(lexer));
    }

    private OpenDistroSqlLexer createLexer() {
         return new OpenDistroSqlLexer(
                    new CaseChangingCharStream(
                        CharStreams.fromString(sql), true));
    }

    private ParseTree analyzeSyntax(OpenDistroSqlParser parser) {
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                    int line, int charPositionInLine, String msg, RecognitionException e) {
                Token offendingToken = (Token) offendingSymbol;
                throw new SyntaxAnalysisException(
                    "Failed to parse query due to syntax error by offending symbol [%s] at: %s...",
                        offendingToken.getText(), sql.substring(0, offendingToken.getStopIndex() + 1));
            }
        });
        return parser.root();
    }

    private void analyzeSemantic(ParseTree tree) {
        tree.accept(new OpenDistroSemanticAnalyzer(sql));
    }

}
