package service;

import DAO.DailyReportDao;
import model.DailyReport;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.DBHelper;
import java.util.List;

public class DailyReportService {

    private static DailyReportService dailyReportService;
    private static long currentDayEarnings = 0L;
    private static long currentDaySellings = 0L;
    private static DailyReport lastDayReport = new DailyReport(0L, 0L);

    private SessionFactory sessionFactory;

    private DailyReportService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public static DailyReportService getInstance() {
        if (dailyReportService == null) {
            dailyReportService = new DailyReportService(DBHelper.getSessionFactory());
        }
        return dailyReportService;
    }

    public static void newSelling(long price) {
        currentDayEarnings += price;
        currentDaySellings ++;
    }

    public void saveNewReport(DailyReport newReport) {
        newReport.setSoldCars(currentDaySellings);
        newReport.setEarnings(currentDayEarnings);
        Session session = sessionFactory.openSession();
        DailyReportDao dao = new DailyReportDao(session);
        dao.save(newReport);
        session.close();
        currentDayEarnings = 0L;
        currentDaySellings = 0L;
        lastDayReport = newReport;
    }

    public List<DailyReport> getAllDailyReports() {
        return new DailyReportDao(sessionFactory.openSession()).getAllDailyReport();
    }

    public DailyReport getLastReport() {
        return lastDayReport;
    }

    public void deleteAllReports() {
        Session session = sessionFactory.openSession();
        String hql = "delete DailyReport";
        session.createQuery(hql).executeUpdate();
        session.close();
    }
}
