package ch.sama.sql.query.standard;

import ch.sama.sql.dbo.Field;
import ch.sama.sql.dbo.IType;
import ch.sama.sql.dbo.Table;
import ch.sama.sql.query.base.IQuery;
import ch.sama.sql.query.base.IQueryRenderer;
import ch.sama.sql.query.base.IValueFactory;
import ch.sama.sql.query.exception.BadParameterException;
import ch.sama.sql.query.helper.Function;
import ch.sama.sql.query.helper.Value;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class ValueFactory implements IValueFactory {
    private IQueryRenderer renderer;

    public static Value ALL = new Value("*");
    public static Value NULL = new Value("NULL");

    public ValueFactory(IQueryRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public Value plain(String s) {
        return new Value(s);
    }

    @Override
    public Value field(String field) {
        return field(new Field(field));
    }

    @Override
    public Value field(String table, String field) {
        return field(new Field(table, field));
    }

    @Override
    public Value field(String table, Field field) {
        return field(new Field(table, field.getName()));
    }

    @Override
    public Value field(Table table, String field) {
        return field(new Field(table, field));
    }

    @Override
    public Value field(Table table, Field field) {
        return field(new Field(table, field.getName()));
    }

    @Override
    public Value field(Field field) {
        return new Value(field, renderer.render(field));
    }

    @Override
    public Value table(String table) {
        return table(new Table(table));
    }

    @Override
    public Value table(String schema, String table) {
        return table(new Table(schema, table));
    }

    @Override
    public Value table(String schema, Table table) {
        return table(new Table(schema, table.getName()));
    }

    @Override
    public Value table(Table table) {
        return new Value(table, renderer.render(table) + ".*");
    }

    @Override
    public Value string(String s) {
        return new Value(s, "'" + s.replace("'", "\\'") + "'");
    }

    @Override
    public Value numeric(Long l) {
        return new Value(l, l.toString());
    }

    @Override
    public Value numeric(Integer i) {
        return new Value(i, i.toString());
    }

    @Override
    public Value numeric(Float f) {
        return new Value(f, f.toString());
    }

    @Override
    public Value numeric(Double d) {
        return new Value(d, d.toString());
    }
    
    @Override
    public Value bool(Boolean b) {
        if (b) {
            return new Value(true, "1");
        } else {
            return new Value(false, "0");
        }
    }

    @Override
    public Value function(String fnc, Value... arguments) {
        return function(new Function(fnc, arguments));
    }

    @Override
    public Value function(Function fnc) {
        return new Value(fnc, renderer.render(fnc));
    }

    @Override
    public Value query(IQuery query) {
        return new Value(query, "(\n" + query.getSql() + "\n)");
    }
    
    @Override
    public Value combine(String operator, Value... values) {
        return new Value(
                Arrays.asList(values).stream()
                        .map(Value::getValue)
                        .collect(Collectors.joining(" " + operator + " "))
        );
    }
    
    @Override
    public Value bracket(Value value) {
        return new Value("(" + value.getValue() + ")");
    }
    
    @Override
    public Value type(IType type) {
        return new Value(type, type.getString());
    }

    @Override
    public Value object(Object object) {
        if (object == null) {
            return ValueFactory.NULL;
        }

        if (object instanceof Boolean) {
            return bool((boolean) object);
        }

        if (object instanceof Integer) {
            return numeric((int) object);
        }

        if (object instanceof Short) {
            return numeric((int) (short) object);
        }

        if (object instanceof Long) {
            return numeric((long) object);
        }

        if (object instanceof Double) {
            return numeric((double) object);
        }

        if (object instanceof Float) {
            return numeric((float) object);
        }

        if (object instanceof String) {
            String s = (String) object;

            // Bit of a gamble..
            // empty strings will be interpreted as null
            if (s.length() == 0 || s.toLowerCase().equals("null")) {
                return ValueFactory.NULL;
            }

            return string(s);
        }

        throw new BadParameterException("Cannot guess {" + object.getClass().getName() + ": " + object.toString() + "}");
    }
}
