package ch.sama.sql.dialect.tsql.helper;

import ch.sama.sql.dialect.tsql.TSqlValueFactory;
import ch.sama.sql.query.exception.BadParameterException;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class ValueGuessTest {
    private static final TSqlValueFactory value = new TSqlValueFactory();

    @Test
    public void guessNull() {
        assertEquals("NULL", value.object(null).getValue());
    }

    @Test
    public void guessBoolean() {
        assertEquals("1", value.object(true).getValue());
    }

    @Test
    public void guessShort() {
        assertEquals("123", value.object((short) 123).getValue());
    }

    @Test
    public void guessInt() {
        assertEquals("123", value.object((int) 123).getValue());
    }

    @Test
    public void guessLong() {
        assertEquals("123", value.object((long) 123).getValue());
    }

    @Test
    public void guessFloat() {
        assertEquals("1.23", value.object(1.23f).getValue());
    }

    @Test
    public void guessDouble() {
        assertEquals("1.23", value.object(1.23).getValue());
    }

    @Test
    public void guessDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        assertEquals("CONVERT(datetime, '2016-03-22 00:00:00', 21)", value.object(sdf.parse("2016-03-22")).getValue());
    }

    @Test
    public void guessEmptyString() {
        assertEquals("NULL", value.object("").getValue());
    }

    @Test
    public void guessNullString() {
        assertEquals("NULL", value.object("nUlL").getValue());
    }

    @Test
    public void guessString() {
        assertEquals("'Hello'", value.object("Hello").getValue());
    }

    @Test
    public void useCorrectEscaping() {
        assertEquals("'Hello ''World'''", value.object("Hello 'World'").getValue());
    }

    @Test (expected = BadParameterException.class)
    public void wrongGuess() {
        value.object(new Object());
    }

    @Test
    public void guessUuid() {
        String uuid = "4e20ff97-c06a-4805-aeab-e96797a062d6";

        assertEquals("'" + uuid + "'", value.object(UUID.fromString(uuid)).getValue());
    }
}
