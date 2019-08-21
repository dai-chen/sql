package com.amazon.opendistroforelasticsearch.sql.antlr.semantic.scope;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Environment<Symbol, Value> {

    private final Environment parent;

    private final Map<Symbol, Value> symbolTable = new HashMap<>();


    public Environment(Environment parent) {
        this.parent = parent;
    }

    public void define(Symbol symbol, Value value) {
        symbolTable.put(symbol, value);
    }

    public Optional<Value> resolve(Symbol symbol) {
        Value value = null;
        for (Environment cur = this; cur != null; cur = cur.parent) {
            value = symbolTable.get(symbol);
            if (value != null) {
                break;
            }
        }
        return Optional.ofNullable(value);
    }

    public Collection<Symbol> allSymbols() {
        return symbolTable.keySet();
    }
}
