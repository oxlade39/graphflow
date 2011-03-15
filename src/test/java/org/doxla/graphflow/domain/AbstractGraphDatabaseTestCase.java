package org.doxla.graphflow.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;

@Ignore("test infrastructure")
public class AbstractGraphDatabaseTestCase {
    protected EmbeddedGraphDatabase db;
    private Transaction transaction;

    @Before
    public void setUp() throws Exception {
        db = new EmbeddedGraphDatabase("var/example3");
        startTransaction();
    }

    private void startTransaction() {
        transaction = db.beginTx();
    }

    @After
    public void tearDown() throws Exception {
        endTransaction();
        db.shutdown();
    }

    protected void endTransaction() {
        transaction.success();
        transaction.finish();
    }

    public abstract class TransactionTemplate {
        public void execute() {
            Transaction transaction1 = db.beginTx();
            try {
                doExecute();
                transaction1.success();
            } catch (Throwable t) {
                transaction1.failure();
            } finally {
                transaction1.finish();
            }
        }

        protected abstract void doExecute();
    }
}
