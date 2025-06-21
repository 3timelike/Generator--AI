import com.zsy.config.DatabaseConfig;
import com.zsy.connection.DatabaseConnection;

public class Test {

    @org.junit.Test
    public void t1(){
        DatabaseConfig databaseConfig = new DatabaseConfig("localhost", "3307", "root", "123");
        String database = DatabaseConnection.database(databaseConfig);
        System.out.println(database);
        //jdbc:mysql://localhost:3307
    }

}
