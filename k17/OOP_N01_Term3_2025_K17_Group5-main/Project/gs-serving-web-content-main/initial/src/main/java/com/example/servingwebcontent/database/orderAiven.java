package com.example.servingwebcontent.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Date;
import com.example.servingwebcontent.model.Order;
import com.example.servingwebcontent.model.OrderPayment;


@Service
public class orderAiven {

    @Autowired
    private myConnection myConnection;
    
    public orderAiven(){}

    public ArrayList<Order> orderAivenList() {
        ArrayList<Order> items = new ArrayList<Order>(); 
        try {
            Connection conn = myConnection.getConnection();
            Statement sta = conn.createStatement();
            ResultSet setdata = sta.executeQuery("select * from Orders");
            int index =0;
            int columnCount = setdata.getMetaData().getColumnCount();
             System.out.println("column #"+columnCount);
   
            while (setdata.next()) {
                Order order = new Order();
              
                String orderID = setdata.getString("orderID");
                Date sqlDate = setdata.getDate("orderDate");
                String status = setdata.getString("status");

                System.out.println("Order AIVEN TEST:");
                System.out.println(orderID + " " + sqlDate + " " + status);

                order.setOrderId(orderID);
                order.setOrderDate(sqlDate.toLocalDate());
                order.setStatus(status);
                
                System.out.println("Get Order in order Aiven");
                System.out.println(order.getOrderId());
                System.out.println(index);
        
            items.add(order);
       }

            setdata.close();
            sta.close();
            conn.close();
           
        } 
        catch (Exception e) {
            System.out.println("Error in database connecion");
            System.out.println(e);
            e.printStackTrace();
        }

        return items;

    }
    

    public ArrayList<OrderPayment> orderListByUserId(String userId) {
        ArrayList<OrderPayment> orders = new ArrayList<>();
        try {
            Connection conn = myConnection.getConnection();

            String query = "SELECT o.orderID, o.orderDate, u.name, p.method, p.amount, p.status " + 
                           "FROM Orders o " +
                           "JOIN UserOrders uo ON o.orderID = uo.orderID " +
                           "JOIN Users u ON uo.userId = u.userId " +
                           "JOIN OrderPayments op ON o.orderID = op.orderID " +
                           "JOIN Payments p ON op.paymentID = p.paymentID " +
                           "WHERE u.userId = ? AND p.status = 'Completed'"; 

            PreparedStatement sta = conn.prepareStatement(query);
            sta.setString(1, userId);
            ResultSet setdata = sta.executeQuery();

            while (setdata.next()) {
                OrderPayment op = new OrderPayment();
                op.setOrderId(setdata.getString("orderID"));
                Date sqlDate = setdata.getDate("orderDate");
                if (sqlDate != null) {
                    op.setOrderDate(sqlDate.toLocalDate());
                }
                op.setCustomerName(setdata.getString("name"));
                op.setPaymentMethod(setdata.getString("method"));
                op.setPaymentAmount(setdata.getDouble("amount"));
                op.setPaymentStatus(setdata.getString("status"));

                op.setPaymentStatus(setdata.getString("status"));

                orders.add(op);
            }

            setdata.close();
            sta.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Error in fetching orders by userId");
            e.printStackTrace();
        }

        return orders;
    }

}

