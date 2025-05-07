package Services;

import java.sql.SQLException;

/*
@author TheMaliik
 */
public interface IUser<T> {
    public void ajouter(T t) throws  SQLException;


}
