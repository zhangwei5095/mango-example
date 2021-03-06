package org.jfaster.mango.example.sharding;

import com.google.common.collect.Lists;
import org.jfaster.mango.datasource.DataSourceFactory;
import org.jfaster.mango.datasource.DriverManagerDataSource;
import org.jfaster.mango.datasource.MultipleDatabaseDataSourceFactory;
import org.jfaster.mango.datasource.SimpleDataSourceFactory;
import org.jfaster.mango.example.util.RandomUtils;
import org.jfaster.mango.operator.Mango;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ash
 */
public class DatabaseShardingOrderDaoMain {

    public static void main(String[] args) {
        String driverClassName = "com.mysql.jdbc.Driver";
        String username = "root"; // 这里请使用您自己的用户名
        String password = "root"; // 这里请使用您自己的密码

        String url1 = "jdbc:mysql://localhost:3306/mango_example_db1";
        DataSource ds1 = new DriverManagerDataSource(driverClassName, url1, username, password);
        DataSourceFactory db1DataSourceFactory = new SimpleDataSourceFactory(ds1);

        String url2 = "jdbc:mysql://localhost:3306/mango_example_db2";
        DataSource ds2 = new DriverManagerDataSource(driverClassName, url2, username, password);
        DataSourceFactory db2DataSourceFactory = new SimpleDataSourceFactory(ds2);

        Map<String, DataSourceFactory> factories = new HashMap<String, DataSourceFactory>();
        factories.put("db1", db1DataSourceFactory);
        factories.put("db2", db2DataSourceFactory);
        DataSourceFactory dsf = new MultipleDatabaseDataSourceFactory(factories);
        Mango mango = Mango.newInstance(dsf);

        DatabaseShardingOrderDao orderDao = mango.create(DatabaseShardingOrderDao.class);

        List<Integer> uids = Lists.newArrayList(66, 9527);
        for (Integer uid : uids) {
            String id = RandomUtils.randomStringId(10); // 随机生成10位字符串ID
            Order order = new Order();
            order.setId(id);
            order.setUid(uid);
            order.setPrice(100);
            order.setStatus(1);

            orderDao.addOrder(order);
            System.out.println(orderDao.getOrdersByUid(uid));
        }
    }

}
