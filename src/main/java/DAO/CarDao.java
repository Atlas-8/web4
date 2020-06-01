package DAO;

import model.Car;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class CarDao {

    private Session session;

    public CarDao(Session session) {
        this.session = session;
    }

    public Car getCar(String brand, String model, String licensePlate) {
        String hql = "FROM Car WHERE brand = :brand AND model = :model AND licensePlate = :licensePlate";
        Query query = session.createQuery(hql);
        query.setParameter("brand", brand);
        query.setParameter("model", model);
        query.setParameter("licensePlate", licensePlate);
        session.clear();
        return (Car) query.uniqueResult();


        /*решить без критерии, критерию не использовать (на досуге выяснить почему) */

        /*Criteria criteria = session.createCriteria(Car.class);
        return (Car) criteria
                .add(Restrictions.eq("brand", brand))
                .add(Restrictions.eq("model", model))
                .add(Restrictions.eq("licensePlate", licensePlate))
                .uniqueResult();*/
    }

    public void deleteCar(Car car) {
        String hql = "DELETE Car WHERE id = :id";
        Query query = session.createQuery(hql);
        query.setParameter("id", car.getId());
        query.executeUpdate();
        session.clear();
    }

    public void save(Car car) {
        session.save(car);
        session.close();
    }

}
