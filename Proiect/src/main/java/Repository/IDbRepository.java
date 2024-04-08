package Repository;

import Domain.Entity;

public interface IDbRepository<T extends Entity> extends IRepository<T>{
    void openConnection() ;
    void closeConnection();
    void createTable() throws RepositoryExcept;
}
