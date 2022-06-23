-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: wenshin
-- ------------------------------------------------------
-- Server version	8.0.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Temporary view structure for view `all_users_and_passwords`
--

DROP TABLE IF EXISTS `all_users_and_passwords`;
/*!50001 DROP VIEW IF EXISTS `all_users_and_passwords`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `all_users_and_passwords` AS SELECT 
 1 AS `id`,
 1 AS `email`,
 1 AS `password`,
 1 AS `role`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback` (
  `id` int NOT NULL AUTO_INCREMENT,
  `rate` float NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `id_user_order` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_user_order` (`id_user_order`),
  CONSTRAINT `feedback_ibfk_1` FOREIGN KEY (`id_user_order`) REFERENCES `user_order` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback`
--

LOCK TABLES `feedback` WRITE;
/*!40000 ALTER TABLE `feedback` DISABLE KEYS */;
INSERT INTO `feedback` VALUES (9,5,'asdasd',38);
/*!40000 ALTER TABLE `feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `master`
--

DROP TABLE IF EXISTS `master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `master` (
  `id` int NOT NULL AUTO_INCREMENT,
  `lastname` varchar(255) NOT NULL,
  `firstname` varchar(255) NOT NULL,
  `middlename` varchar(255) NOT NULL,
  `birthdate` date DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `avatar` varchar(255) NOT NULL,
  `price` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `master`
--

LOCK TABLES `master` WRITE;
/*!40000 ALTER TABLE `master` DISABLE KEYS */;
INSERT INTO `master` VALUES (1,'Крюков','Юрий','Тихонович','1999-03-21','nodeji1@mail.ru','password','+79503374175','avatars/avatar1000.jpg',1000),(2,'Яковлев','Денис','Сергеевич','1995-09-19','denis@example.com','password','+79507744624','avatars/avatar1200.jpg',1200),(3,'Абрамов','Даниил','Александрович','2000-05-30','daniil@example.com','password','+79507623054','avatars/avatar800.jpg',800),(4,'Павлов','Алексей','Альбертович','2001-04-13','alexey@example.com','password','+79503384178','avatars/avatar0.png',0);
/*!40000 ALTER TABLE `master` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `messages` (
  `id` int NOT NULL AUTO_INCREMENT,
  `datetime` datetime NOT NULL,
  `message` varchar(255) NOT NULL,
  `author` varchar(255) NOT NULL,
  `order_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `user_order` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
INSERT INTO `messages` VALUES (26,'2022-06-17 08:12:16','fsf','user',50);
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `salon`
--

DROP TABLE IF EXISTS `salon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `salon` (
  `id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(255) NOT NULL,
  `lat` float(9,7) NOT NULL,
  `lon` float(10,7) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `salon`
--

LOCK TABLES `salon` WRITE;
/*!40000 ALTER TABLE `salon` DISABLE KEYS */;
INSERT INTO `salon` VALUES (1,'ул. Госпитальная, 19, Омск',54.9954567,73.3823166),(2,'ул. Конева, 28 корпус 1, Омск',54.9776955,73.3113937),(3,'Карла Маркса просп., 75, Омск',54.9466057,73.3859100);
/*!40000 ALTER TABLE `salon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sessions`
--

DROP TABLE IF EXISTS `sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sessions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `hours` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  `order_id` int NOT NULL,
  `isPaid` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  CONSTRAINT `sessions_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `user_order` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sessions`
--

LOCK TABLES `sessions` WRITE;
/*!40000 ALTER TABLE `sessions` DISABLE KEYS */;
INSERT INTO `sessions` VALUES (35,'2022-06-14','8,9,10','completed',50,1),(36,'2022-06-17','9,10,11,12','cancelled',50,0),(37,'2022-06-20','13,14,15','inProgress',50,0),(38,'2022-06-14','13,14,15','cancelled',51,1),(39,'2022-06-17','13,14','cancelled',51,0),(40,'2022-06-16','10','inProgress',52,1),(41,'2022-06-22','9,10','inProgress',53,1),(42,'2022-06-25','14,15,16','inProgress',53,0),(43,'2022-06-22','12,13,14','inProgress',54,1),(44,'2022-06-25','12,13','inProgress',54,0),(45,'2022-06-23','8,9,10','inProgress',50,0),(46,'2022-06-26','11','inProgress',50,0),(47,'2022-06-20','9,10,11','inProgress',51,0),(48,'2022-06-23','11,12','inProgress',51,0);
/*!40000 ALTER TABLE `sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sketch`
--

DROP TABLE IF EXISTS `sketch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sketch` (
  `id` int NOT NULL AUTO_INCREMENT,
  `image` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `width` float NOT NULL,
  `height` float NOT NULL,
  `working_hours` int NOT NULL,
  `is_public` tinyint(1) NOT NULL,
  `author` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `author` (`author`),
  CONSTRAINT `sketch_ibfk_1` FOREIGN KEY (`author`) REFERENCES `master` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sketch`
--

LOCK TABLES `sketch` WRITE;
/*!40000 ALTER TABLE `sketch` DISABLE KEYS */;
INSERT INTO `sketch` VALUES (1,'sketches/animals-abstraction-sample.jpg','Рысь','Эскиз рыси в стиле абстракция',10,15,5,1,1),(2,'sketches/books-sample.jpg','Книги','Эскиз стопки книг',10,15,5,1,1),(3,'sketches/music-sample.jpg','Пластинка','Эскиз волны переходящей в пластинку',20,8,3,1,1),(4,'sketches/dragon-sample.jpg','Дракон','Эскиз дракона в цветах',15,30,10,1,1);
/*!40000 ALTER TABLE `sketch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sketch_tags`
--

DROP TABLE IF EXISTS `sketch_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sketch_tags` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_sketch` int NOT NULL,
  `id_tag` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_sketch` (`id_sketch`),
  KEY `id_tag` (`id_tag`),
  CONSTRAINT `sketch_tags_ibfk_1` FOREIGN KEY (`id_sketch`) REFERENCES `sketch` (`id`),
  CONSTRAINT `sketch_tags_ibfk_2` FOREIGN KEY (`id_tag`) REFERENCES `tag` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sketch_tags`
--

LOCK TABLES `sketch_tags` WRITE;
/*!40000 ALTER TABLE `sketch_tags` DISABLE KEYS */;
INSERT INTO `sketch_tags` VALUES (1,1,3),(2,1,5),(3,1,8),(4,2,2),(5,2,5),(6,3,1),(7,3,5),(8,3,7),(9,4,4),(10,4,6),(11,4,9);
/*!40000 ALTER TABLE `sketch_tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
INSERT INTO `tag` VALUES (1,'музыка'),(2,'книги'),(3,'животные'),(4,'японская тематика'),(5,'чёрно-белый'),(6,'драконы'),(7,'маленькие'),(8,'абстракция'),(9,'цветные'),(10,'минимализм');
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tokens`
--

DROP TABLE IF EXISTS `tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tokens` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `token` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tokens`
--

LOCK TABLES `tokens` WRITE;
/*!40000 ALTER TABLE `tokens` DISABLE KEYS */;
INSERT INTO `tokens` VALUES (14,'asd@asd.zxc','asdqwe123','master',0),(38,'vladimir@example.com','gY+E5tpV6Oyz5WZafr1Jg1NlWduI1sDBjFbGL4WyHkIfJ36LIEnFfXMHR6Y5A1JbOazXzMW2wsBV+2F8v8MuFA==','user',2),(40,'q@q.q','toAPJsmcHu5kptpyCW7PngUzGnFnnp92ALtRct3KP+WhiTSwAu52K9V+sNHJe7BqARo5wi/wKqbuQvOMl4O77w==','user',3),(69,'alexey@example.com','wy+HhotAHgNOSBoFV7xJcPg/r1MRSpISyP6Et+dNf6E+A279QmSZ1rHjGQcHqI74flXXfGE07D75qv4w16wOKA==','master',4),(73,'nodeji1@mail.ru','/DoSxKreR+9IFTUQet0MbQ/EsaNQpOFl/hH3+Me7a5t5QbuyGm0R7TNUmOvVHLCDpRY5iylWvzdctStkVJnmjw==','master',1),(74,'denis@example.com','vlqktI9d0ok7Mu051v5d3zUGUQ9qNAc6P733Zbh4hcKR+94V/DruMtD4wUqnroxkU6bEuxMF7j/I2eTmfDd+Rg==','master',2),(76,'nodeji2@mail.ru','Psq/YAJMolUhcT63q0NsrtF7JeWRLo+z4Ti7y0rBvX76k+00NuQXCH877gsXNZaPU0o/fvrV0DibLD+xUbEH3Q==','user',1),(77,'nodeji2@mail.ru','zvqWyISlWw9bcZdoakSEIrnpx9EL6qF26Kqqq2AO9AkGSeVyW0bRpQ5PieOdSpQrLDiJ2O7Z3juZz1dJbdxycQ==','user',1),(78,'nodeji1@mail.ru','F5dF1/xr8Nh9bzhkbYyLUgw0gom8sGn/brc1xh4CqAqErPBn5CZK01ED24S+F+9H1Y8xt1fIHeTh7PZQaHgvhw==','master',1),(79,'nodeji1@mail.ru','HBM9cwwdGDYq4ua7I+pHzhdq2h9oRpHF0zjcdDQIyBYQVcOiyFs01Th6aUdTE3rZ8J+4Ap/z1/EM2Skv2ZhP/g==','master',1),(80,'nodeji2@mail.ru','kwtrMdFOx7c8G3x16+GAPg4svt50+9Oc2Yr2FU7FBjDseF9qK2TlVtuHiCAOeUIzHqhQjXD2i67X8MYTz2H8jQ==','user',1),(81,'nodeji1@mail.ru','pa5q8ySV0Da6MwMd069UiVVzHVazlqStjVCSQltZXh7ht2Vyq3ZFIKQFQi4QCw1p0YGLVV/Xs4tOjBp6QxI3VA==','master',1),(82,'nodeji2@mail.ru','uaY3fi5KkfHBm5smXVDoXhdZDiuLUxQnFwCxjpopBApuVWttq6D449uXGppGet49+PTCx7iB2N9DIX/D6HWZqA==','user',1),(83,'nodeji1@mail.ru','V6ri5K0WWnZbiAx1qCn4e/mszl5Qy+ScjQbMcG4Kt48vjYavS4p6HgwsqPS44wiEYF2HmnxUxJSlNHPJh0C/oQ==','master',1),(84,'nodeji2@mail.ru','MKdcSvUYozjvVzTi+Pu6nKdftxj/esPBAlMqsP8g9p6hUIjLHOsBSvDcWxRLbSNzRoM5sDqWtMt48GXmlM2qXg==','user',1);
/*!40000 ALTER TABLE `tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `lastname` varchar(255) NOT NULL,
  `firstname` varchar(255) NOT NULL,
  `middlename` varchar(255) NOT NULL,
  `registerdate` date DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Ерохин','Алексей','Вадимович','1997-04-23','nodeji2@mail.ru','password','+79503374176'),(2,'Лебедев','Владимир','Вольфович','1999-02-23','vladimir@example.com','password','+79505123426'),(3,'rty','qwe','uio','2022-06-08','q@q.q','qq','+712332112342');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_fav_sketches`
--

DROP TABLE IF EXISTS `user_fav_sketches`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_fav_sketches` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sketch_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sketch_id` (`sketch_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `user_fav_sketches_ibfk_1` FOREIGN KEY (`sketch_id`) REFERENCES `sketch` (`id`),
  CONSTRAINT `user_fav_sketches_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_fav_sketches`
--

LOCK TABLES `user_fav_sketches` WRITE;
/*!40000 ALTER TABLE `user_fav_sketches` DISABLE KEYS */;
INSERT INTO `user_fav_sketches` VALUES (30,1,3),(31,2,3),(32,4,3),(40,1,1);
/*!40000 ALTER TABLE `user_fav_sketches` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_order`
--

DROP TABLE IF EXISTS `user_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_order` (
  `id` int NOT NULL AUTO_INCREMENT,
  `status` varchar(255) NOT NULL,
  `sketch_id` int DEFAULT NULL,
  `client_id` int NOT NULL,
  `master_id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_order`
--

LOCK TABLES `user_order` WRITE;
/*!40000 ALTER TABLE `user_order` DISABLE KEYS */;
INSERT INTO `user_order` VALUES (38,'completed',1,1,1),(39,'completed',2,2,2),(50,'orderInProgress',4,1,1),(51,'orderInProgress',2,1,1),(52,'orderInProgress',1,1,4),(53,'orderInProgress',2,1,2),(54,'orderInProgress',1,1,2);
/*!40000 ALTER TABLE `user_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Final view structure for view `all_users_and_passwords`
--

/*!50001 DROP VIEW IF EXISTS `all_users_and_passwords`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_unicode_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`server`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `all_users_and_passwords` AS select `master`.`id` AS `id`,`master`.`email` AS `email`,`master`.`password` AS `password`,'master' AS `role` from `master` union select `user`.`id` AS `id`,`user`.`email` AS `email`,`user`.`password` AS `password`,'user' AS `role` from `user` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-06-17 12:23:30
