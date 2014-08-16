package ch.sama.sql.query.helper;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.sama.sql.dbo.Field;
import ch.sama.sql.dialect.tsql.TSqlOrderParser;

public class OrderTest {
	private static final OrderParser parser = new TSqlOrderParser();
	
	@Test
	public void ascSingle() {
		Order o = Order.asc(new Field("A"));
		assertEquals("ORDER BY A ASC", parser.parse(o));
	}
	
	@Test
	public void ascMultiple() {
		Order o = Order.asc(new Field("A"), new Field("B"));
		assertEquals("ORDER BY A, B ASC", parser.parse(o));
	}
	
	@Test
	public void descSingle() {
		Order o = Order.desc(new Field("A"));
		assertEquals("ORDER BY A DESC", parser.parse(o));
	}
	
	@Test
	public void descMultiple() {
		Order o = Order.desc(new Field("A"), new Field("B"));
		assertEquals("ORDER BY A, B DESC", parser.parse(o));
	}
}
