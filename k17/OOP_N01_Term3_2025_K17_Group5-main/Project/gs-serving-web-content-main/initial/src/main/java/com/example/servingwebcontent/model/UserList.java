package com.example.servingwebcontent.model;
import java.util.ArrayList;
import java.util.List;

public class UserList {

    ArrayList<User> st = new ArrayList<User>();

    public ArrayList<User> addUser(User stu) {

        st.add(stu);
        return st;

    }

    public ArrayList<User> getEditUser(String name, String userId) {

        for (int i = 0; i < st.size(); i++) {

            if (st.get(i).getUserId() == userId) {

                System.out.print("true");

                st.get(i).setName(name);
            }

        }

        return st;
    }

    public ArrayList<User> getDeleteUser(String userId) {

        for (int i = 0; i < st.size(); i++) {

            if (st.get(i).getUserId() == userId) {

                st.remove(i);

            }

        }

        return st;
    }

    public void printUserList() {
        int len = st.size();

        for (int i = 0; i < len; i++) 
        {
            System.out.println("User ID: " + st.get(i).getUserId());
            System.out.println("Fullname: " + st.get(i).getName());
            System.out.println("Gender: " + st.get(i).getGender());
            System.out.println("Birth Date: " + st.get(i).getBirthDate());
            System.out.println("Phone Number: " + st.get(i).getPhoneNumber());
            System.out.println("Email: " + st.get(i).getEmail());
            System.out.println("Address: " + st.get(i).getAddress());
            System.out.println("Password: " + st.get(i).getPassword());
            System.out.println("User Type: " + st.get(i).getUserType());
            System.out.println("-----------------------------");
        }
    }

    //Cô Thu
 public void printListUser(ArrayList<User> userList){
        for(int i =0; i<userList.size(); i++){
            System.out.println("List user:");
            System.out.println(userList.get(i).getName());
            System.out.println(userList.get(i).getAddress());
            System.out.println(userList.get(i).getUserId());


        }
    }

    public static void displayList(List<User> co) {
        for (int i = 0; i < co.size(); i++) {

            System.out.println("userName: " + co.get(i).getName());

        }
    }

    //cach viet truyen thong
    public ArrayList<User> searchUserNameTradition(String userId) {
        ArrayList<User> newList = new ArrayList<User>();
        for (User stu : st) {
            if (stu.getUserId().contains(userId)) {
                newList.add(stu);

            }
        }

        return newList;

    }

}
