package ch.sama.sql.query.helper;

import ch.sama.sql.query.base.IQueryRenderer;
import ch.sama.sql.query.base.check.Identifier;
import ch.sama.sql.query.exception.IllegalIdentifierException;

public class Value {
    private Object source;
    private String value;
    private String alias;

    public Value(String value) {
        this.source = value;
        this.value = value;
    }
    
    public Value(Object o, String value) {
        this.source = o;
        this.value = value;
    }

	public String getValue() {
        return value;
    }

    public String getString(IQueryRenderer renderer) {
        return renderer.render(this);
    }

    public String getAlias() {
        return alias;
    }

    public Object getSource() {
        return source;
    }

    public Value as(String alias) {
        if (alias == null) {
            return this;
        }

        if (!Identifier.test(alias)) {
            throw new IllegalIdentifierException(alias);
        }
        
        Value clone = new Value(source, value);
        clone.alias = alias;

        return clone;
    }
}