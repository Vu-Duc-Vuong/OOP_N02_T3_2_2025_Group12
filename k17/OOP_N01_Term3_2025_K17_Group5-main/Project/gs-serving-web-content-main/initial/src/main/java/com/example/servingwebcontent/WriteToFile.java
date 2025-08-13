package com.example.servingwebcontent;

import java.io.FileWriter;
import java.io.IOException;
//import com.example.servingwebcontent.File;
import java.util.ArrayList;

import com.example.servingwebcontent.model.User;

public class WriteToFile {
    //Sau này dùng để xuất hóa đơn
    public void ToFile(ArrayList<User> u) {
        try {
            
            int i = u.size() - 1;
            FileWriter writer = new FileWriter("./initial/File/Login.txt", true);
            writer.append("\n");

            writer.write(u.get(i).getName());
            writer.write(u.get(i).getAddress());

            writer.close();
        } catch (IOException e) {
            System.out.println("Error at write to File!");
            e.printStackTrace();
        }

    }
}