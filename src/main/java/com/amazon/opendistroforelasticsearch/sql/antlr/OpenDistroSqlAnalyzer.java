package com.amazon.opendistroforelasticsearch.sql.antlr;

import com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlLexer;
import com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParser;
import com.amazon.opendistroforelasticsearch.sql.antlr.parser.OpenDistroSqlParserBaseListener;
import com.amazon.opendistroforelasticsearch.sql.rewriter.matchtoterm.VerificationException;
import com.amazon.opendistroforelasticsearch.sql.utils.StringUtils;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.lucene.search.spell.LevenshteinDistance;
import org.apache.lucene.search.spell.StringDistance;

import java.util.HashMap;
import java.util.Map;

import static com.amazon.opendistroforelasticsearch.sql.esdomain.LocalClusterState.FieldMappings;
import static com.amazon.opendistroforelasticsearch.sql.esdomain.LocalClusterState.IndexMappings;
import static com.amazon.opendistroforelasticsearch.sql.esdomain.LocalClusterState.state;

/**
 * Facade for ANTLR generated parser to avoid boilerplate code.
 */
public class OpenDistroSqlAnalyzer {

    /**
     * Generate parse tree for the query to perform syntax and semantic analysis.
     * Runtime exception with clear message is thrown for any verification error.
     *
     * @param sql   original query
     */
    public void analyze(String sql) {
        analyzeSemantic(
            analyzeSyntax(
                createParser(
                    createLexer(sql)
                )
            )
        );
    }

    private OpenDistroSqlParser createParser(Lexer lexer) {
        return new OpenDistroSqlParser(new CommonTokenStream(lexer));
    }

    private OpenDistroSqlLexer createLexer(String sql) {
         return new OpenDistroSqlLexer(
                    new CaseChangingCharStream(
                        CharStreams.fromString(sql), true));
    }

    private ParseTree analyzeSyntax(OpenDistroSqlParser parser) {
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                throw new VerificationException(StringUtils.format(
                    "ANTLR parser failed due to syntax error by offending symbol [%s] at position [%d]: ", offendingSymbol, charPositionInLine));
            }
        });
        return parser.root();
    }

    private void analyzeSemantic(ParseTree tree) {
        final Map<String, FieldMappings> mappingByAlias = new HashMap<>();

        ParseTreeListener listener1 = new OpenDistroSqlParserBaseListener() {

            @Override
            public void enterTableName(OpenDistroSqlParser.TableNameContext ctx) {
                OpenDistroSqlParser.FullIdContext fullId = ctx.fullId();
                String indexName = fullId.uid(0).simpleId().ID().getSymbol().getText();

                IndexMappings indexMappings = state().getFieldMappings(new String[]{indexName});
                if (indexMappings == null) {
                    throw new VerificationException(StringUtils.format("Index name or pattern [%s] doesn't match any existing index", indexName));
                }

                FieldMappings mappings = indexMappings.firstMapping().firstMapping();
                mappingByAlias.put("", mappings);
            }
        };

        ParseTreeListener listener2 = new OpenDistroSqlParserBaseListener() {

            @Override
            public void enterFullColumnName(OpenDistroSqlParser.FullColumnNameContext ctx) {
                final String fieldName = ctx.uid().simpleId().ID().getText();
                FieldMappings fieldMappings = mappingByAlias.get("");
                Map<String, Object> mappings = fieldMappings.mapping(fieldName);
                if (mappings == null) {
                    StringDistance distanceAlg = new LevenshteinDistance();
                    String candiate = fieldName;
                    float min = Float.MAX_VALUE;
                    for (String name : fieldMappings.allNames()) {
                        float dist = distanceAlg.getDistance(fieldName, name);
                        if (dist < min) {
                            candiate = name;
                            min = dist;
                        }
                    }
                    throw new VerificationException(StringUtils.format("Field [%s] cannot be found. Did you mean [%s]?", fieldName, candiate));
                }
            }
        };

        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener1, tree);
        walker.walk(listener2, tree);
    }

    /**
     * Convert to upper/lower case before sending to lexer.
     * https://github.com/antlr/antlr4/blob/master/doc/case-insensitive-lexing.md#custom-character-streams-approach
     * https://github.com/parrt/antlr4/blob/case-insensitivity-doc/doc/resources/CaseChangingCharStream.java
     */
    private static class CaseChangingCharStream implements CharStream {

        final CharStream stream;
        final boolean upper;

        /**
         * Constructs a new CaseChangingCharStream wrapping the given {@link CharStream} forcing
         * all characters to upper case or lower case.
         * @param stream The stream to wrap.
         * @param upper If true force each symbol to upper case, otherwise force to lower.
         */
        public CaseChangingCharStream(CharStream stream, boolean upper) {
            this.stream = stream;
            this.upper = upper;
        }

        @Override
        public String getText(Interval interval) {
            return stream.getText(interval);
        }

        @Override
        public void consume() {
            stream.consume();
        }

        @Override
        public int LA(int i) {
            int c = stream.LA(i);
            if (c <= 0) {
                return c;
            }
            if (upper) {
                return Character.toUpperCase(c);
            }
            return Character.toLowerCase(c);
        }

        @Override
        public int mark() {
            return stream.mark();
        }

        @Override
        public void release(int marker) {
            stream.release(marker);
        }

        @Override
        public int index() {
            return stream.index();
        }

        @Override
        public void seek(int index) {
            stream.seek(index);
        }

        @Override
        public int size() {
            return stream.size();
        }

        @Override
        public String getSourceName() {
            return stream.getSourceName();
        }
    }
}