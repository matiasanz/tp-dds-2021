package Repositorios.Template;

import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

public interface Transaccional
    extends WithGlobalEntityManager
    , EntityManagerOps
    , TransactionalOps { }
