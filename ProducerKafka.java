package Kafkaproducer;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * Created by dinuka on 7/29/17.
 */
public class ProducerKafka {

    public static void main(String[] args) throws Exception{
        ProducerKafka k = new ProducerKafka();
        k.pushKafkaData();

    }

    public void pushKafkaData() throws Exception{

        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Hitech_Smart_Factory", "root", "");
        Statement smt1 = con.createStatement();
        Statement smt2 = con.createStatement();
        ResultSet rs = smt1.executeQuery("SELECT Name FROM Tags");
        ResultSet r = smt2.executeQuery("SELECT COUNT(*) AS rowcount FROM Tags");
        r.next();
        int count = r.getInt("rowcount");
        r.close();
        String[] row = new String[count];
        for (int i=0; i < count ; i++)
            {
                rs.next();
                row[i] = rs.getString("Name");
                //System.out.println(row[i]);

            }
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<>(props);
        /*for(int i = 0; i < 10; i++) {
            Date todaysDate = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String ts = df.format(todaysDate);
            producer.send(new ProducerRecord<String, String>("company1_branch1_section1_productionline1_machine1_sensor1",ts, Integer.toString(i)));
        }
        producer.close();*/
        for(int j = 0; j < 100; j++){

            for(int i = 0; i < count; i++){
                int max = 30;
                int min = 25;
                Random rand = new Random();
                int data = rand.nextInt((max - min) + 1) + min;
                Date todaysDate = new Date();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String ts = df.format(todaysDate);
                if(j%20==0){
                    int max2 = 50;
                    int min2 = 35;
                    Random rand2 = new Random();
                    int data2 = rand2.nextInt((max2 - min2) + 1) + min2;
                    producer.send(new ProducerRecord<String, String>(row[i],ts, Integer.toString(data2)));
                }else{
                    producer.send(new ProducerRecord<String, String>(row[i],ts, Integer.toString(data)));
                }
            }
        j++;
        }
        producer.close();
    }
}


