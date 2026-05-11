package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
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
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("INSERT INTO seller\n" +
                    "(Name, Email, BirthDate, BaseSalary, DepartmentId)\n" +
                    "VALUES\n" +
                    "(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);


            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3, new java.sql.Date(seller.getDateOfBirth().getTime()));
            st.setDouble(4, seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    seller.setId(id);
                }
                DB.CloseResultSet(rs);
            } else {
                throw new DbException("Failed to insert seller");
            }
        } catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.CloseStatement(st);
        }
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
        PreparedStatement st = null;
        ResultSet rs = null;

        try{
            st = conn.prepareStatement("SELECT seller.*,department.Name as DepName\n" +
                    "FROM seller INNER JOIN department\n" +
                    "ON seller.DepartmentId = department.Id\n" +
                    "ORDER BY Name");


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
