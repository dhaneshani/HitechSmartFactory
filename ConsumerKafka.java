package Kafkaproducer;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
//import java.util.Arrays;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

/**
 * Created by dinuka on 8/24/17.
 */
public class ConsumerKafka {
    public static void main(String[] args)throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Hitech_Smart_Factory", "root", "");
        Statement smt = con.createStatement();
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

        }
        for (int i=0; i < count ; i++)
        {
        ConsumerKafka c = new ConsumerKafka();
        c.readKafkaData(row[i]);
        }


    }

    public void readKafkaData(String str) throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Hitech_Smart_Factory", "root", "");
        Statement smt = con.createStatement();

        Properties props = new Properties();

        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer <String, String>(props);

        //Kafka Consumer subscribes list of topics here.
        consumer.subscribe(Arrays.asList(str));

        //print the topic name
        System.out.println("Subscribed to topic " + str);

        int i = 0;

        for (boolean exit = false;!exit;) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                // print the offset,key and value for the consumer records.
                String time_Stamp = record.key();
                int sen_Val = Integer.parseInt(record.value());
                if (sen_Val > 35) {
                    System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
                    smt.executeUpdate("INSERT INTO " + str + " (Time_Stamp, Sen_Data) VALUES ('" + time_Stamp + "', '" + sen_Val + "');");
                }
                i++;
                if(i>4){exit=true;}
            }
        }
        consumer.close();

    }

}

