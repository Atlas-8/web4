package service;

import DAO.CarDao;
import model.Car;
import org.hibernate.*;
import util.DBHelper;
import javax.servlet.ServletException;
import java.util.List;

public class CarService {

    private static CarService carService;
    private SessionFactory sessionFactory;

    private CarService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public static CarService getInstance() {
        if (carService == null) {
            carService = new CarService(DBHelper.getSessionFactory());
        }
        return carService;
    }

    public boolean saveCar(Car car) throws ServletException {
        Session session = sessionFactory.openSession();
        int limit = session.createSQLQuery("SELECT (id) FROM cars WHERE brand='" +
                car.getBrand() + "'").list().size();
        session.clear();
        if (limit >= 10) {
            session.close();
            return false;
        }
        CarDao dao = new CarDao(session);
        dao.save(car);
        return true;
    }

    public Car buyCar(String brand, String model, String licensePlate) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        CarDao carDao = new CarDao(session);
        Car buyingCar = carDao.getCar(brand, model, licensePlate);
        carDao.deleteCar(buyingCar);
        transaction.commit();
        session.close();
        return buyingCar;
    }

    public List<Car> getAllCars() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<Car> cars = session.createQuery("FROM Car").list();
        transaction.commit();
        session.close();
        return cars;
    }

    public void deleteAllCars() {
        Session session = sessionFactory.openSession();
        String hql = "delete Car";
        session.createQuery(hql).executeUpdate();
        session.close();
    }
}













