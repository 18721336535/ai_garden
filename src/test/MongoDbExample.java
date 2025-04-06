//package com.example.demo;
//import com.mongodb.client.*;
//import org.bson.Document;
//
//public class MongoDbExample {
//    public static void mm(String[] args) {
//        // 创建 MongoDB 客户端
//        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
//
//        // 获取数据库
//        MongoDatabase database = mongoClient.getDatabase("testdb");
//
//        // 获取集合
//        MongoCollection<Document> collection = database.getCollection("testcollection");
//
//        // 插入文档
//        Document doc = new Document("name", "Alice")
//                .append("age", 31)
//                .append("city", "Wonderland");
//        collection.insertOne(doc);
//        System.out.println("Document inserted successfully.");
//
//        // 查询文档
//        FindIterable<Document> foundDoc = collection.find(new Document("name", "Alice"));
//        for(Document dc :foundDoc){
//            System.out.println(dc.toJson());
//        }
////        if (foundDoc != null) {
////            System.out.println("Found document: " + foundDoc.toJson());
////        } else {
////            System.out.println("Document not found.");
////        }
//
//        // 关闭客户端
//        mongoClient.close();
//    }
//}
