import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

void main() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    Department obj = new Department(1, "Books");
    Seller seller = new Seller(21 ,"Bob","BobCharlton@gmail.com",new Date(), 3000.0, obj);
    SellerDao sellerDao = DaoFactory.createSellerDao();

    System.out.println(seller.toString());
}