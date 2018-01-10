package test.db.helper;

import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.SqlSessionManager;

public class MySQLHelper {
    //mybatis的配置文件
    private static final String SQL_MAPPING_CONF_FILE = "mybatis_conf.xml";
    private static final String DATABASE_CONF_FILE = "config.properties";
    private static SqlSessionFactory sessionFactory;
    private static SqlSessionManager sqlSM;
    private static MySQLHelper _this = new MySQLHelper(); 

    public static MySQLHelper getInstance(){
    	return _this;
    }
    
    public static SqlSessionFactory getSqlSessionFactory(){
    	return sessionFactory;
    }
    
    public static SqlSessionManager getSqlSessionManager(){
    	return sqlSM;
    }
    
    /**
     * SqlSessionFactory声明周期是应用级的，不需要每次new一个
     * 而SqlSession的线程不安全，且范围太小，故把SqlSessionFactory单例化并暴露出来，在其他地方集成进去
     */
    static{
    	try {
    		//使用类加载器加载mybatis的配置文件（它也加载关联的映射文件）
    		InputStream is = MySQLHelper.class.getClassLoader().getResourceAsStream(SQL_MAPPING_CONF_FILE);
    		//使用MyBatis提供的Resources类加载mybatis的配置文件（它也加载关联的映射文件）
    		Reader reader = Resources.getResourceAsReader(DATABASE_CONF_FILE);
    		Properties properties = new Properties();
    		properties.load(reader);
    		//构建sqlSession的工厂
    		sessionFactory = new SqlSessionFactoryBuilder().build(is, properties);
    		sqlSM = SqlSessionManager.newInstance(sessionFactory);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    private MySQLHelper() {
    }
}
