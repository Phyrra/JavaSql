package ch.sama.sql.dialect.tsql.grammar.visitor;

import ch.sama.sql.dialect.tsql.TSqlSourceFactory;
import ch.sama.sql.dialect.tsql.grammar.antlr.SqlBaseVisitor;
import ch.sama.sql.dialect.tsql.grammar.antlr.SqlParser;
import ch.sama.sql.query.base.IQuery;
import ch.sama.sql.query.helper.Source;

import java.util.List;

class SourceVisitor extends SqlBaseVisitor<Source> {
    private QueryVisitor queryVisitor;
    private TSqlSourceFactory sourceVisitor;

    public SourceVisitor(QueryVisitor query, TSqlSourceFactory source) {
        this.queryVisitor = query;
        this.sourceVisitor = source;
    }

    @Override
    public Source visitSource(SqlParser.SourceContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Source visitAliasedSource(SqlParser.AliasedSourceContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Source visitAliasedTable(SqlParser.AliasedTableContext ctx) {
        Source table = visit(ctx.tableSource());
        String alias = ctx.sqlIdentifier().Identifier().getText();
        
        return table.as(alias);
    }

    @Override
    public Source visitTableSource(SqlParser.TableSourceContext ctx) {
        List<SqlParser.SqlIdentifierContext> identifiers = ctx.table().sqlIdentifier();
        
        if (identifiers.size() > 1) {
            return sourceVisitor.table(identifiers.get(0).Identifier().getText(), identifiers.get(1).Identifier().getText());
        } else {
            return sourceVisitor.table(identifiers.get(0).Identifier().getText());
        }
    }

    @Override
    public Source visitAliasedStatement(SqlParser.AliasedStatementContext ctx) {
        IQuery query = queryVisitor.visit(ctx.statement());
        String alias = ctx.sqlIdentifier().Identifier().getText();

        return sourceVisitor.query(query).as(alias);
    }
}
