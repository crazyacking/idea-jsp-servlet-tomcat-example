package net.therap.mealplanner.dao;

import net.therap.mealplanner.connection_manager.MysqlConnector;
import net.therap.mealplanner.entity.Dish;
import net.therap.mealplanner.entity.Meal;
import net.therap.mealplanner.utils.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author rumman
 * @since 10/16/16
 */
public class MealDaoImpl implements MealDao {
    @Override
    public List<Meal> findAll() {
        SessionFactory sessionFactory = HibernateUtil.getSessionAnnotationFactory();
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        List<Meal> mealList = session.createCriteria(Meal.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
        tx.commit();
        session.close();
        return mealList;
    }

    @Override
    public List<Meal> findById(int mealId) {
        return null;
    }

    @Override
    public List<Meal> findByName(String mealName) {
        return null;
    }

    @Override
    public boolean insertMeal(Meal meal) {
        List<Meal> mealList = findAll();
        if (!mealList.contains(meal)) {
            SessionFactory sessionFactory = HibernateUtil.getSessionAnnotationFactory();
            Session session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            session.save(meal);
            tx.commit();
            session.close();
            return true;
        }
        return false;
    }

    @Override
    public boolean updateMeal(Meal meal) {
        List<Meal> mealList = findAll();
        if (!mealList.contains(meal)) {
            SessionFactory sessionFactory = HibernateUtil.getSessionAnnotationFactory();
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.update(meal);
            transaction.commit();
            session.close();
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteMeal(Meal meal) {
        List<Meal> mealList = findAll();
        if (mealList.contains(meal)) {
            SessionFactory sessionFactory = HibernateUtil.getSessionAnnotationFactory();
            Session session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            session.delete(meal);
            tx.commit();
            session.close();
            return true;
        }
        return false;
    }

    public boolean insertMealDishMap(int mealId, Set<Dish> dishSet) {
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO meal_dish_map (meal_id, dish_id) VALUES "
                + " (?,?)";

        try {
            dbConnection = MysqlConnector.getMysqlConnection();
            preparedStatement = dbConnection.prepareStatement(insertTableSQL);
            for (Dish dish : dishSet) {
                preparedStatement.setString(1, String.valueOf(mealId));
                preparedStatement.setString(2, String.valueOf(dish.getId()));
                preparedStatement.executeUpdate();
            }
            return true;

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return false;
    }

    public Set<Dish> getDishSetByMealId(int mealId) {
        Connection connection = MysqlConnector.getMysqlConnection();
        Statement stmt = null;
        DishDaoImpl dishDao = new DishDaoImpl();
        List<Dish> dishList = dishDao.findAll();
        Set<Dish> dishSet = new HashSet<Dish>();

        try {
            stmt = connection.createStatement();
            String sql;
            sql = "SELECT id, dish_id FROM meal_dish_map WHERE meal_id = " + mealId;
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int dish_id = rs.getInt("dish_id");
                for (Dish dish : dishList) {
                    if (dish.getId() == dish_id) {
                        dishSet.add(dish);
                    }
                }
            }
            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return dishSet;
    }

    public boolean deleteMealDishMap(int mealId) {

        Statement stmt = null;
        Connection dbConnection = MysqlConnector.getMysqlConnection();
        PreparedStatement preparedStatement = null;

        String deleteSQL = "DELETE FROM meal_dish_map WHERE meal_id = ?";

        try {
            preparedStatement = dbConnection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, mealId);
            preparedStatement.executeUpdate();

            System.out.println("Meal Dish Maps deleted!");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }

        return false;
    }

    public boolean updateMealDishMap(int mealId, Set<Dish> dishSet) {
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        String insertTableSQL = "UPDATE meal_dish_map SET dish_id = ? WHERE meal_id = ?; ";

        try {
            dbConnection = MysqlConnector.getMysqlConnection();
            preparedStatement = dbConnection.prepareStatement(insertTableSQL);
            for (Dish dish : dishSet) {
                preparedStatement.setString(1, String.valueOf(dish.getId()));
                preparedStatement.setString(2, String.valueOf(mealId));
                preparedStatement.executeUpdate();
            }
            return true;

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return false;
    }
}
