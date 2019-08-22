package com.amazon.opendistroforelasticsearch.sql.antlr.semantic.scope;

import com.amazon.opendistroforelasticsearch.sql.antlr.semantic.types.Type;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Symbol table for symbol definition and lookup.
 */
public class SymbolTable {

    /** 2 dimensions hash table to manage symbols with type in different namespace */
    private Map<Namespace, Map<String, Type>> tableByNamespace = new EnumMap<>(Namespace.class);


    public void put(Symbol symbol, Type type) {
        tableByNamespace.computeIfAbsent(
            symbol.getNamespace(),
            ns -> new HashMap<>()
        ).put(symbol.getName(), type);
    }

    public Optional<Type> lookup(Symbol symbol) {
        Map<String, Type> table = tableByNamespace.get(symbol.getNamespace());
        Type type = null;
        if (table != null) {
            type = table.get(symbol.getName());
        }
        return Optional.ofNullable(type);
    }

    public Collection<String> lookupAll(Namespace namespace) {
        return tableByNamespace.getOrDefault(namespace, Collections.emptyMap()).keySet();
    }
}
