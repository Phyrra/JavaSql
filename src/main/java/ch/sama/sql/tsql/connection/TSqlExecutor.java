package ch.sama.sql.tsql.connection;

import ch.sama.sql.dbo.connection.DBConnection;
import ch.sama.sql.dbo.connection.IQueryExecutor;
import ch.sama.sql.dbo.connection.IResultSetTransformer;
import ch.sama.sql.query.exception.BadSqlException;
import ch.sama.sql.query.exception.ConnectionException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class TSqlExecutor implements IQueryExecutor {
    private DBConnection connection;
    private IResultSetTransformer transformer;

    public TSqlExecutor(DBConnection connection, IResultSetTransformer transformer) {
        this.connection = connection;
        this.transformer = transformer;
    }

    private void closeStatement(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) { }
    }

    @Override
    public void execute(String query) {
        Statement statement;
        try {
            Connection con = connection.open();
            statement = con.createStatement();
        } catch (Exception e) {
            connection.close();

            throw new ConnectionException(e);
        }

        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            closeStatement(statement);
            connection.close();

            throw new BadSqlException(e);
        }

        closeStatement(statement);
        connection.close();
    }

    @Override
    public List<Map<String, Object>> query(String query) {
        List<Map<String, Object>> list;

        Statement statement;
        try {
            Connection con = connection.open();
            statement = con.createStatement();
        } catch (Exception e) {
            connection.close();

            throw new ConnectionException(e);
        }

        try {
            ResultSet resultSet = statement.executeQuery(query);
            list = transformer.transform(resultSet);
        } catch (SQLException e) {
            closeStatement(statement);
            connection.close();

            throw new BadSqlException(e);
        }

        closeStatement(statement);
        connection.close();

        return list;
    }

    @Override
    public Map<String, Object> get(String query) {
        List<Map<String, Object>> list;

        Statement statement;
        try {
            Connection con = connection.open();
            statement = con.createStatement();
        } catch (Exception e) {
            connection.close();

            throw new ConnectionException(e);
        }

        try {
            ResultSet resultSet = statement.executeQuery(query);
            list = transformer.transform(resultSet);
        } catch (SQLException e) {
            closeStatement(statement);
            connection.close();

            throw new BadSqlException(e);
        }

        closeStatement(statement);
        connection.close();

        if (list.size() != 1) {
            throw new BadSqlException("Expected 1 Result, got " + list.size());
        }

        return list.get(0);
    }
}
