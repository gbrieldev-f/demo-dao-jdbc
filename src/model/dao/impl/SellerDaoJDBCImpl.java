package model.dao.impl;

import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBCImpl implements SellerDao {

    private Connection conn;

    public SellerDaoJDBCImpl(Connection conn) {
        this.conn = conn;
    }


    @Override
    public void insert(Seller seller) {

    }

    @Override
    public void update(Seller seller) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try{
            st = conn.prepareStatement("SELECT seller.*,department.Name as DepName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "WHERE seller.Id = ?");

            st.setInt(1, id);
            rs = st.executeQuery();

            if(rs.next()){
                Department dep = instatiateDepartment(rs);
                Seller seller = instatiateSeller(rs, dep);
                return seller;
            }
            return null;
        }
        catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            db.DB.CloseStatement(st);
            db.DB.CloseResultSet(rs);

        }


    }

    private Seller instatiateSeller(ResultSet rs, Department dep)  throws SQLException{
        Seller seller = new Seller();
        seller.setName(rs.getString("Name"));
        seller.setDepartment(dep);
        seller.setId(rs.getInt("Id"));
        seller.setEmail(rs.getString("Email"));
        seller.setDateOfBirth(rs.getDate("BirthDate"));
        seller.setBaseSalary(rs.getDouble("BaseSalary"));
        return seller;
    }

    private Department instatiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    @Override
    public List<Seller> findAll() {
        return List.of();
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try{
            st = conn.prepareStatement("SELECT seller.*,department.Name as DepName\n" +
                    "FROM seller INNER JOIN department\n" +
                    "ON seller.DepartmentId = department.Id\n" +
                    "WHERE DepartmentId = ?\n" +
                    "ORDER BY Name");

            st.setInt(1, department.getId());
            rs = st.executeQuery();

            List <Seller> sellers = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while(rs.next()){

                Department dep = map.get(rs.getInt("DepartmentId"));
                if(dep == null){
                    dep = instatiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }
                Seller seller = instatiateSeller(rs, dep);
                sellers.add(seller);
            }

            return sellers;
        }

        catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            db.DB.CloseStatement(st);
            db.DB.CloseResultSet(rs);

        }

    }
}
