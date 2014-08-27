package ch.sama.sql.query.base.checker;

import ch.sama.sql.dbo.Field;
import ch.sama.sql.dbo.Table;
import ch.sama.sql.query.base.FromQuery;
import ch.sama.sql.query.base.IQuery;
import ch.sama.sql.query.base.JoinQuery;
import ch.sama.sql.query.base.SelectQuery;
import ch.sama.sql.query.helper.Value;

import java.util.ArrayList;
import java.util.List;

public class QueryFinder {
    public QueryFinder() { }

    public<T extends IQuery> T get(IQuery src, Class<T> dst) {
        if (src == null) {
            return null;
        }

        if (dst.isAssignableFrom(src.getClass())) {
            return (T)src;
        }

        return get(src.getParent(), dst);
    }

    public<T extends IQuery> List<T> getAll(IQuery src, Class<T> dst) {
        List<T> list = new ArrayList<T>();

        IQuery iter = src;
        T query;


        while ((query = get(iter, dst)) != null) {
            list.add(query);
            iter = query.getParent();
        }

        return list;
    }

    // TODO: This will not work with "SELECT *"
    // TODO: This will not work with "SELECT TABLE.*"
    public<T> List<T> getSelected(IQuery src, Class<T> dst) {
        SelectQuery query = get(src, SelectQuery.class);
        List<Value> values = query.getValues();

        List<T> fields = new ArrayList<T>();
        for (Value v : values) {
            Object o = v.getSource();
            if (dst.isAssignableFrom(o.getClass())) {
                fields.add((T)o);
            }
        }

        return fields;
    }

    public List<Table> getSources(IQuery src) {
        List<Table> tables = new ArrayList<Table>();

        FromQuery from = get(src, FromQuery.class); // There can be only one
        List<JoinQuery> joins = getAll(src, JoinQuery.class);

        List<Table> tmp = from.getTables();
        for (Table t : tmp) {
            tables.add(t);
        }

        for (JoinQuery j : joins) {
            tables.add(j.getTable());
        }

        return tables;
    }
}
