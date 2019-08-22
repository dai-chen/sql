package com.amazon.opendistroforelasticsearch.sql.antlr.semantic.scope;

import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.Type;

import java.util.Collection;
import java.util.Optional;

public class Environment {

    private final Environment parent;

    private final SymbolTable symbolTable;

    public Environment(Environment parent) {
        this.parent = parent;
        this.symbolTable = new SymbolTable();
    }

    public void define(Symbol symbol, Type type) {
        symbolTable.put(symbol, type);
    }

    public Optional<Type> resolve(Symbol symbol) {
        Optional<Type> type = Optional.empty();
        for (Environment cur = this; cur != null; cur = cur.parent) {
            type = cur.symbolTable.lookup(symbol);
            if (type.isPresent()) {
                break;
            }
        }
        return type;
    }

    public Collection<String> allSymbolsIn(Namespace namespace) {
        return symbolTable.lookupAll(namespace);
    }

    public Environment getParent() {
        return parent;
    }
}
