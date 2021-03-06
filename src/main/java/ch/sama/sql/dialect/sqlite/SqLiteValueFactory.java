package ch.sama.sql.dialect.sqlite;

import ch.sama.sql.query.exception.BadSqlException;
import ch.sama.sql.query.helper.Value;
import ch.sama.sql.query.standard.ValueFactory;

public class SqLiteValueFactory extends ValueFactory {
    private static final SqLiteQueryRenderer renderer = new SqLiteQueryRenderer();

    public SqLiteValueFactory() {
        super(renderer);
    }
}