package de.geoinfoffm.registry.core;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlTest {

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(
                "xxx",
                "xxx",
                "xxx"
        );
    }

    private List<Object> convertToObject(byte[] byteArr) throws IOException, ClassNotFoundException {
        List<Object> list = new ArrayList<>();
       // Object obj = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(byteArr);
        ObjectInputStream ois = new ObjectInputStream(bis);
        while(bis.available() > 0){
            Object obj = ois.readObject();
            System.out.println("obj type: " + obj.getClass());
            list.add(obj);

            //list.add((Map<String, Object>)ois.readObject());
        }
        return  list;
    }

    @Test
    public void testLogValueOfOperationparametervalue_parametervalue() throws SQLException, IOException, ClassNotFoundException {
        Connection conn = connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM operationparametervalue_parametervalue;");
        while(rs.next()){
            //Display values
            System.out.println("ID: " + rs.getString(1));
            System.out.println("UOMID: " + rs.getString(2));

            byte[] bytea = rs.getBytes(3);
            List<Object> ls = convertToObject(bytea);
            System.out.println("Value: " + StringUtils.join(ls, ","));
            System.out.println("===");
        }
        rs.close();
        stmt.close();
        conn.close();
    }
}
