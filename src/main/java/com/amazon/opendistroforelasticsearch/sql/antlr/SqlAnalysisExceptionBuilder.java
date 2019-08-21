package com.amazon.opendistroforelasticsearch.sql.antlr;

import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.SemanticAnalysisException;
import com.amazon.opendistroforelasticsearch.sql.antlr.syntax.SyntaxAnalysisException;
import com.amazon.opendistroforelasticsearch.sql.utils.StringUtils;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

/**
 * Builder for creating sql analysis exception by enforcing error message format for consistency.
 */
public class SqlAnalysisExceptionBuilder {

    /** Internal enum for which exception subclass to build */
    private final ExceptionType type;

    /** What caused the error is required */
    private final String cause;

    /** Suggest correction or usage */
    private String suggestion;

    /** Where the error incurred */
    private String location;


    public static SqlAnalysisExceptionBuilder syntaxException(String cause, Object... args) {
        return new SqlAnalysisExceptionBuilder(ExceptionType.SYNTAX, cause, args);
    }

    public static SqlAnalysisExceptionBuilder semanticException(String cause, Object... args) {
        return new SqlAnalysisExceptionBuilder(ExceptionType.SEMANTIC, cause, args);
    }

    private SqlAnalysisExceptionBuilder(ExceptionType type, String cause, Object[] args) {
        this.type = type;
        this.cause = StringUtils.format(cause, args);
    }

    public SqlAnalysisExceptionBuilder at(String sql, Token token) {
        this.location = StringUtils.format("Offending token is [%s] at '%s...'.",
            token.getText(), sql.substring(0, token.getStopIndex() + 1));
        return this;
    }

    public SqlAnalysisExceptionBuilder at(String sql, ParserRuleContext ctx) {
        return at(sql, ctx.getStop());
    }

    public SqlAnalysisExceptionBuilder suggestion(String suggestion, Object... args) {
        this.suggestion = StringUtils.format(suggestion, args);
        return this;
    }

    public SqlAnalysisException build() {
        switch (type) {
            case SYNTAX: return new SyntaxAnalysisException(fullMessage());
            case SEMANTIC: return new SemanticAnalysisException(fullMessage());
            default: throw new IllegalStateException("Unknown exception type [" + type + "].");
        }
    }

    private String fullMessage() {
        StringBuilder message = new StringBuilder();
        for (String str : new String[] {cause, location, suggestion}) {
            if (str != null) {
                message.append(str).append(" ");
            }
        }
        return message.toString();
    }

    private enum ExceptionType {
        SYNTAX, SEMANTIC
    }
}
