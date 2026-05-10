package model.dao;

import model.dao.impl.SellerDaoJDBCImpl;
import model.entities.Seller;

public class DaoFactory {
    public static SellerDao createSellerDao() {
        return new SellerDaoJDBCImpl(db.DB.getConnection()) ;
    }
}
