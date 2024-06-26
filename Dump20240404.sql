-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: hrms
-- ------------------------------------------------------
-- Server version	8.0.35

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
-- Table structure for table `answer_result`
--

DROP TABLE IF EXISTS `answer_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `answer_result` (
  `answer_result` float DEFAULT NULL,
  `category_question_id` int DEFAULT NULL,
  `employee_id` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `is_final` bit(1) DEFAULT NULL,
  `performance_cycle_id` int DEFAULT NULL,
  `saved_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6ml4akuskfj68qr4eyx27uvkp` (`category_question_id`),
  KEY `FKc6kvsnfpgsva00stseeub2xae` (`performance_cycle_id`),
  KEY `FKh7g7awcswc6gnva2xvc4oiect` (`employee_id`),
  CONSTRAINT `FK6ml4akuskfj68qr4eyx27uvkp` FOREIGN KEY (`category_question_id`) REFERENCES `category_question` (`category_question_id`),
  CONSTRAINT `FKc6kvsnfpgsva00stseeub2xae` FOREIGN KEY (`performance_cycle_id`) REFERENCES `performance_cycle` (`performance_cycle_id`),
  CONSTRAINT `FKh7g7awcswc6gnva2xvc4oiect` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answer_result`
--

LOCK TABLES `answer_result` WRITE;
/*!40000 ALTER TABLE `answer_result` DISABLE KEYS */;
/*!40000 ALTER TABLE `answer_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `category_weight` float DEFAULT NULL,
  `category_description` varchar(255) DEFAULT NULL,
  `category_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,NULL,NULL,'Category 1');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category_question`
--

DROP TABLE IF EXISTS `category_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category_question` (
  `category_id` int DEFAULT NULL,
  `category_question_id` int NOT NULL AUTO_INCREMENT,
  `question_id` int DEFAULT NULL,
  PRIMARY KEY (`category_question_id`),
  KEY `FK3u7rj7rndipodxx0s1prrxip5` (`category_id`),
  KEY `FKqycqj8lxn33xrl0abyw0cv3lb` (`question_id`),
  CONSTRAINT `FK3u7rj7rndipodxx0s1prrxip5` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`),
  CONSTRAINT `FKqycqj8lxn33xrl0abyw0cv3lb` FOREIGN KEY (`question_id`) REFERENCES `question` (`question_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category_question`
--

LOCK TABLES `category_question` WRITE;
/*!40000 ALTER TABLE `category_question` DISABLE KEYS */;
INSERT INTO `category_question` VALUES (1,1,1),(1,2,1),(1,3,1),(1,4,1),(1,5,1),(1,6,1);
/*!40000 ALTER TABLE `category_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `competency`
--

DROP TABLE IF EXISTS `competency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `competency` (
  `competency_group_id` int DEFAULT NULL,
  `competency_id` int NOT NULL AUTO_INCREMENT,
  `ordered` int DEFAULT NULL,
  `insertion_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modification_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `competency_name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`competency_id`),
  KEY `FKnyvofi6teu79gpqg8w2s7bhre` (`competency_group_id`),
  CONSTRAINT `FKnyvofi6teu79gpqg8w2s7bhre` FOREIGN KEY (`competency_group_id`) REFERENCES `competency_group` (`competency_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `competency`
--

LOCK TABLES `competency` WRITE;
/*!40000 ALTER TABLE `competency` DISABLE KEYS */;
/*!40000 ALTER TABLE `competency` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `competency_cycle`
--

DROP TABLE IF EXISTS `competency_cycle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `competency_cycle` (
  `competency_cycle_id` int NOT NULL AUTO_INCREMENT,
  `year` int DEFAULT NULL,
  `due_date` datetime(6) DEFAULT NULL,
  `initial_date` datetime(6) DEFAULT NULL,
  `insertion_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modification_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `start_date` datetime(6) DEFAULT NULL,
  `competency_cycle_name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `evaluator_type` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`competency_cycle_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `competency_cycle`
--

LOCK TABLES `competency_cycle` WRITE;
/*!40000 ALTER TABLE `competency_cycle` DISABLE KEYS */;
INSERT INTO `competency_cycle` VALUES (1,NULL,NULL,NULL,'2023-12-18 04:25:44','2023-12-18 04:25:44',NULL,'Cycle 1','Description 1',NULL,NULL);
/*!40000 ALTER TABLE `competency_cycle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `competency_evaluation`
--

DROP TABLE IF EXISTS `competency_evaluation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `competency_evaluation` (
  `competency_cycle_id` int DEFAULT NULL,
  `competency_evaluation_id` int NOT NULL AUTO_INCREMENT,
  `competency_id` int DEFAULT NULL,
  `employee_id` int DEFAULT NULL,
  `final_evaluation` float DEFAULT NULL,
  `self_evaluation` float DEFAULT NULL,
  `supervisor_evaluation` float DEFAULT NULL,
  PRIMARY KEY (`competency_evaluation_id`),
  KEY `FK6r8u1lr3sqxeje83ku2s8txcy` (`competency_id`),
  KEY `FK6by4kmgxvuexpuw79vtsai994` (`competency_cycle_id`),
  KEY `FKi2fb06f6x1v86j83mg3nylku9` (`employee_id`),
  CONSTRAINT `FK6by4kmgxvuexpuw79vtsai994` FOREIGN KEY (`competency_cycle_id`) REFERENCES `competency_cycle` (`competency_cycle_id`),
  CONSTRAINT `FK6r8u1lr3sqxeje83ku2s8txcy` FOREIGN KEY (`competency_id`) REFERENCES `competency` (`competency_id`),
  CONSTRAINT `FKi2fb06f6x1v86j83mg3nylku9` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `competency_evaluation`
--

LOCK TABLES `competency_evaluation` WRITE;
/*!40000 ALTER TABLE `competency_evaluation` DISABLE KEYS */;
/*!40000 ALTER TABLE `competency_evaluation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `competency_evaluation_overall`
--

DROP TABLE IF EXISTS `competency_evaluation_overall`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `competency_evaluation_overall` (
  `competency_cycle_id` int DEFAULT NULL,
  `employee_id` int DEFAULT NULL,
  `evaluation_overall_id` int NOT NULL AUTO_INCREMENT,
  `is_evaluator_submitted` bit(1) DEFAULT NULL,
  `is_final_submitted` bit(1) DEFAULT NULL,
  `is_self_submitted` bit(1) DEFAULT NULL,
  `score` float DEFAULT NULL,
  `completed_date` datetime(6) DEFAULT NULL,
  `last_updated` datetime(6) DEFAULT NULL,
  `employee_status` varchar(255) DEFAULT NULL,
  `evaluator_status` varchar(255) DEFAULT NULL,
  `final_status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`evaluation_overall_id`),
  KEY `FKgi2936v2h5xws93llommyg13j` (`competency_cycle_id`),
  KEY `FK7uh7b6dlovfwr6nvkdga7hi6k` (`employee_id`),
  CONSTRAINT `FK7uh7b6dlovfwr6nvkdga7hi6k` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`),
  CONSTRAINT `FKgi2936v2h5xws93llommyg13j` FOREIGN KEY (`competency_cycle_id`) REFERENCES `competency_cycle` (`competency_cycle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `competency_evaluation_overall`
--

LOCK TABLES `competency_evaluation_overall` WRITE;
/*!40000 ALTER TABLE `competency_evaluation_overall` DISABLE KEYS */;
/*!40000 ALTER TABLE `competency_evaluation_overall` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `competency_group`
--

DROP TABLE IF EXISTS `competency_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `competency_group` (
  `competency_group_id` int NOT NULL AUTO_INCREMENT,
  `weight` float DEFAULT NULL,
  `competency_group_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`competency_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `competency_group`
--

LOCK TABLES `competency_group` WRITE;
/*!40000 ALTER TABLE `competency_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `competency_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `competency_time_line`
--

DROP TABLE IF EXISTS `competency_time_line`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `competency_time_line` (
  `competency_cycle_id` int DEFAULT NULL,
  `competency_time_line_id` int NOT NULL AUTO_INCREMENT,
  `is_done` bit(1) DEFAULT NULL,
  `due_date` datetime(6) DEFAULT NULL,
  `start_date` datetime(6) DEFAULT NULL,
  `competency_time_line_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`competency_time_line_id`),
  KEY `FKtfc5syd69acn66u4gd8ur64me` (`competency_cycle_id`),
  CONSTRAINT `FKtfc5syd69acn66u4gd8ur64me` FOREIGN KEY (`competency_cycle_id`) REFERENCES `competency_cycle` (`competency_cycle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `competency_time_line`
--

LOCK TABLES `competency_time_line` WRITE;
/*!40000 ALTER TABLE `competency_time_line` DISABLE KEYS */;
/*!40000 ALTER TABLE `competency_time_line` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dam_extension`
--

DROP TABLE IF EXISTS `dam_extension`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dam_extension` (
  `dam_extension_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(10) DEFAULT NULL,
  `icon_uri` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`dam_extension_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dam_extension`
--

LOCK TABLES `dam_extension` WRITE;
/*!40000 ALTER TABLE `dam_extension` DISABLE KEYS */;
/*!40000 ALTER TABLE `dam_extension` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dam_info_detail`
--

DROP TABLE IF EXISTS `dam_info_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dam_info_detail` (
  `created_at` date DEFAULT NULL,
  `extension` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `FKin0jm3mb9bx4ik92nqacx802c` (`extension`),
  CONSTRAINT `FKin0jm3mb9bx4ik92nqacx802c` FOREIGN KEY (`extension`) REFERENCES `dam_extension` (`dam_extension_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dam_info_detail`
--

LOCK TABLES `dam_info_detail` WRITE;
/*!40000 ALTER TABLE `dam_info_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `dam_info_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department` (
  `department_id` int NOT NULL AUTO_INCREMENT,
  `sum_id` int DEFAULT NULL,
  `department_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`department_id`),
  KEY `FKiibjcy8ieeughr1jp94yw70vi` (`sum_id`),
  CONSTRAINT `FKiibjcy8ieeughr1jp94yw70vi` FOREIGN KEY (`sum_id`) REFERENCES `employee` (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES (1,NULL,'Backend');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `emergency_contact`
--

DROP TABLE IF EXISTS `emergency_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `emergency_contact` (
  `emergency_contact_id` int NOT NULL AUTO_INCREMENT,
  `employee_id` int DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`emergency_contact_id`),
  KEY `FKtoyey9u8x1mc48k4labhfhew7` (`employee_id`),
  CONSTRAINT `FKtoyey9u8x1mc48k4labhfhew7` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `emergency_contact`
--

LOCK TABLES `emergency_contact` WRITE;
/*!40000 ALTER TABLE `emergency_contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `emergency_contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `current_contract` int DEFAULT NULL,
  `department_id` int DEFAULT NULL,
  `employee_id` int NOT NULL AUTO_INCREMENT,
  `job_level_id` int DEFAULT NULL,
  `position_id` int DEFAULT NULL,
  `status` int DEFAULT NULL,
  `date_of_birth` datetime(6) DEFAULT NULL,
  `insertion_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `joined_date` datetime(6) DEFAULT NULL,
  `left_date` datetime(6) DEFAULT NULL,
  `modification_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `facebook_link` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `instagram_link` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `linkedin_link` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `profile_bio` varchar(255) DEFAULT NULL,
  `twitter_link` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`employee_id`),
  KEY `FKbejtwvg9bxus2mffsm3swj3u9` (`department_id`),
  KEY `FKor1u9v6xi7l1pocx10h0hqier` (`job_level_id`),
  KEY `FKbc8rdko9o9n1ri9bpdyxv3x7i` (`position_id`),
  CONSTRAINT `FKbc8rdko9o9n1ri9bpdyxv3x7i` FOREIGN KEY (`position_id`) REFERENCES `position` (`position_id`),
  CONSTRAINT `FKbejtwvg9bxus2mffsm3swj3u9` FOREIGN KEY (`department_id`) REFERENCES `department` (`department_id`),
  CONSTRAINT `FKor1u9v6xi7l1pocx10h0hqier` FOREIGN KEY (`job_level_id`) REFERENCES `job_level` (`job_level_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (NULL,NULL,1,NULL,NULL,NULL,NULL,'2023-12-18 04:41:45',NULL,NULL,'2023-12-18 04:41:45',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(NULL,NULL,2,NULL,NULL,NULL,NULL,'2023-12-18 04:41:45',NULL,NULL,'2023-12-18 04:41:45',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(NULL,NULL,3,NULL,NULL,NULL,NULL,'2023-12-18 04:41:45',NULL,NULL,'2023-12-18 04:41:45',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_career_path`
--

DROP TABLE IF EXISTS `employee_career_path`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_career_path` (
  `employee_id` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `match_percentage` float DEFAULT NULL,
  `ordered` int DEFAULT NULL,
  `position_level_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKg7oso5qudsdjn82i8k5319uub` (`employee_id`),
  KEY `FK54ica8qxu88cj67ulg19i180q` (`position_level_id`),
  CONSTRAINT `FK54ica8qxu88cj67ulg19i180q` FOREIGN KEY (`position_level_id`) REFERENCES `position_level` (`position_level_id`),
  CONSTRAINT `FKg7oso5qudsdjn82i8k5319uub` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_career_path`
--

LOCK TABLES `employee_career_path` WRITE;
/*!40000 ALTER TABLE `employee_career_path` DISABLE KEYS */;
/*!40000 ALTER TABLE `employee_career_path` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_dam_info`
--

DROP TABLE IF EXISTS `employee_dam_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_dam_info` (
  `dam_extension_id` int DEFAULT NULL,
  `employee_dam_id` int NOT NULL AUTO_INCREMENT,
  `employee_id` int DEFAULT NULL,
  `uploaded_at` datetime(6) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`employee_dam_id`),
  KEY `FKk0t3rmqkwmuy8siq7b94d2aj` (`employee_id`),
  KEY `FK2eef7j59knpge6p1jtweqwgft` (`dam_extension_id`),
  CONSTRAINT `FK2eef7j59knpge6p1jtweqwgft` FOREIGN KEY (`dam_extension_id`) REFERENCES `dam_extension` (`dam_extension_id`),
  CONSTRAINT `FKk0t3rmqkwmuy8siq7b94d2aj` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_dam_info`
--

LOCK TABLES `employee_dam_info` WRITE;
/*!40000 ALTER TABLE `employee_dam_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `employee_dam_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_position_level_target`
--

DROP TABLE IF EXISTS `employee_position_level_target`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_position_level_target` (
  `employee_id` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `position_level_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7j1585mkltx2t3qq6kbl8f94m` (`employee_id`),
  KEY `FKcuncpaebhhmcjfa9etwxc0t47` (`position_level_id`),
  CONSTRAINT `FK7j1585mkltx2t3qq6kbl8f94m` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`),
  CONSTRAINT `FKcuncpaebhhmcjfa9etwxc0t47` FOREIGN KEY (`position_level_id`) REFERENCES `position_level` (`position_level_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_position_level_target`
--

LOCK TABLES `employee_position_level_target` WRITE;
/*!40000 ALTER TABLE `employee_position_level_target` DISABLE KEYS */;
/*!40000 ALTER TABLE `employee_position_level_target` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_skill`
--

DROP TABLE IF EXISTS `employee_skill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_skill` (
  `employee_id` int DEFAULT NULL,
  `employee_skill_id` int NOT NULL AUTO_INCREMENT,
  `proficiency_level_id` int DEFAULT NULL,
  `skill_id` int DEFAULT NULL,
  PRIMARY KEY (`employee_skill_id`),
  KEY `FKkd8xx37dlmjryoas0d91hri6c` (`employee_id`),
  KEY `FK7y1tk9aja4bn4j7wwacowy8bp` (`proficiency_level_id`),
  KEY `FKam2psf41jwoy33ge3uvxep8tl` (`skill_id`),
  CONSTRAINT `FK7y1tk9aja4bn4j7wwacowy8bp` FOREIGN KEY (`proficiency_level_id`) REFERENCES `proficiency_level` (`proficiency_level_id`),
  CONSTRAINT `FKam2psf41jwoy33ge3uvxep8tl` FOREIGN KEY (`skill_id`) REFERENCES `skill` (`skill_id`),
  CONSTRAINT `FKkd8xx37dlmjryoas0d91hri6c` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_skill`
--

LOCK TABLES `employee_skill` WRITE;
/*!40000 ALTER TABLE `employee_skill` DISABLE KEYS */;
/*!40000 ALTER TABLE `employee_skill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback` (
  `feedback_request_id` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `feedback_content` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKab6eumknffvaups9t3igmkj0x` (`feedback_request_id`),
  CONSTRAINT `FKab6eumknffvaups9t3igmkj0x` FOREIGN KEY (`feedback_request_id`) REFERENCES `feedback_request` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback`
--

LOCK TABLES `feedback` WRITE;
/*!40000 ALTER TABLE `feedback` DISABLE KEYS */;
/*!40000 ALTER TABLE `feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback_request`
--

DROP TABLE IF EXISTS `feedback_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback_request` (
  `cycle_id` int DEFAULT NULL,
  `feedback_receiver_id` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `request_receiver_id` int DEFAULT NULL,
  `requestor_id` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKekdi3ikugbxp44gt9u7b5uojw` (`cycle_id`),
  KEY `FKhsa5qary5os3daj065vb6a4xr` (`feedback_receiver_id`),
  KEY `FKmsphyf3liu1sh6bifk4p85qgy` (`request_receiver_id`),
  KEY `FK6ixbpxli9odiunlirgtie3ufj` (`requestor_id`),
  CONSTRAINT `FK6ixbpxli9odiunlirgtie3ufj` FOREIGN KEY (`requestor_id`) REFERENCES `employee` (`employee_id`),
  CONSTRAINT `FKekdi3ikugbxp44gt9u7b5uojw` FOREIGN KEY (`cycle_id`) REFERENCES `performance_cycle` (`performance_cycle_id`),
  CONSTRAINT `FKhsa5qary5os3daj065vb6a4xr` FOREIGN KEY (`feedback_receiver_id`) REFERENCES `employee` (`employee_id`),
  CONSTRAINT `FKmsphyf3liu1sh6bifk4p85qgy` FOREIGN KEY (`request_receiver_id`) REFERENCES `employee` (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback_request`
--

LOCK TABLES `feedback_request` WRITE;
/*!40000 ALTER TABLE `feedback_request` DISABLE KEYS */;
INSERT INTO `feedback_request` VALUES (1,3,2,2,1,'2023-12-14 10:49:19.975000','Test'),(1,3,3,2,1,'2023-12-15 17:25:27.053000','Test'),(1,3,4,2,1,'2023-12-18 09:26:04.442000','Test'),(1,3,5,2,1,'2023-12-18 09:43:06.230000','Test'),(1,3,6,2,1,'2023-12-18 09:43:51.690000','Test'),(1,3,7,2,1,'2023-12-18 09:49:04.075000','Test'),(1,3,8,2,1,'2023-12-18 11:41:45.471000','Test');
/*!40000 ALTER TABLE `feedback_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goal`
--

DROP TABLE IF EXISTS `goal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goal` (
  `competency_cycle_id` int DEFAULT NULL,
  `employee_id` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `is_approved` tinyint(1) DEFAULT '0',
  `progress` float DEFAULT NULL,
  `weight` float DEFAULT NULL,
  `approved_at` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrvgttla26bfamfxd6ix1kp632` (`competency_cycle_id`),
  KEY `FK52vhw3t1nm8qcp6g2th9v10ow` (`employee_id`),
  CONSTRAINT `FK52vhw3t1nm8qcp6g2th9v10ow` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`),
  CONSTRAINT `FKrvgttla26bfamfxd6ix1kp632` FOREIGN KEY (`competency_cycle_id`) REFERENCES `competency_cycle` (`competency_cycle_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goal`
--

LOCK TABLES `goal` WRITE;
/*!40000 ALTER TABLE `goal` DISABLE KEYS */;
INSERT INTO `goal` VALUES (1,1,1,NULL,NULL,NULL,NULL,NULL,NULL,'Description 1','NOTSTART','Goal 1'),(1,1,2,NULL,NULL,NULL,NULL,NULL,NULL,'Description 2','NOTSTART','Goal 2');
/*!40000 ALTER TABLE `goal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_level`
--

DROP TABLE IF EXISTS `job_level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job_level` (
  `job_level_id` int NOT NULL AUTO_INCREMENT,
  `job_level_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`job_level_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_level`
--

LOCK TABLES `job_level` WRITE;
/*!40000 ALTER TABLE `job_level` DISABLE KEYS */;
INSERT INTO `job_level` VALUES (1,'Junior'),(2,'Senior');
/*!40000 ALTER TABLE `job_level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `performance_cycle`
--

DROP TABLE IF EXISTS `performance_cycle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `performance_cycle` (
  `goal_weightage` float DEFAULT NULL,
  `is_done` bit(1) DEFAULT NULL,
  `performance_cycle_id` int NOT NULL AUTO_INCREMENT,
  `performance_weightage` float DEFAULT NULL,
  `template_id` int DEFAULT NULL,
  `initial_date` datetime(6) DEFAULT NULL,
  `insertion_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modification_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `performance_cycle_end_date` datetime(6) DEFAULT NULL,
  `performance_cycle_start_date` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `performance_cycle_name` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`performance_cycle_id`),
  KEY `FK8wiyrwuiqpaj0qy4tbt6swtsm` (`template_id`),
  CONSTRAINT `FK8wiyrwuiqpaj0qy4tbt6swtsm` FOREIGN KEY (`template_id`) REFERENCES `template` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `performance_cycle`
--

LOCK TABLES `performance_cycle` WRITE;
/*!40000 ALTER TABLE `performance_cycle` DISABLE KEYS */;
INSERT INTO `performance_cycle` VALUES (NULL,_binary '\0',1,NULL,1,NULL,'2023-12-18 04:41:45','2023-12-18 04:41:45',NULL,NULL,NULL,NULL,'Not Start');
/*!40000 ALTER TABLE `performance_cycle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `performance_evaluation`
--

DROP TABLE IF EXISTS `performance_evaluation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `performance_evaluation` (
  `completed_date` date DEFAULT NULL,
  `employee_id` int DEFAULT NULL,
  `final_assessment` float DEFAULT NULL,
  `last_updated` date DEFAULT NULL,
  `performance_cycle_id` int DEFAULT NULL,
  `performance_evaluation_id` int NOT NULL,
  `potential_score` float DEFAULT NULL,
  `self_assessment` float DEFAULT NULL,
  `supervisor_assessment` float DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`performance_evaluation_id`),
  KEY `FK8ep3te5f2k060pamp3kpaqk4` (`employee_id`),
  KEY `FKfnl90lqdriyg21ln1myubnx6g` (`performance_cycle_id`),
  CONSTRAINT `FK8ep3te5f2k060pamp3kpaqk4` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`),
  CONSTRAINT `FKfnl90lqdriyg21ln1myubnx6g` FOREIGN KEY (`performance_cycle_id`) REFERENCES `performance_cycle` (`performance_cycle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `performance_evaluation`
--

LOCK TABLES `performance_evaluation` WRITE;
/*!40000 ALTER TABLE `performance_evaluation` DISABLE KEYS */;
/*!40000 ALTER TABLE `performance_evaluation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `performance_evaluation_seq`
--

DROP TABLE IF EXISTS `performance_evaluation_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `performance_evaluation_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `performance_evaluation_seq`
--

LOCK TABLES `performance_evaluation_seq` WRITE;
/*!40000 ALTER TABLE `performance_evaluation_seq` DISABLE KEYS */;
INSERT INTO `performance_evaluation_seq` VALUES (1);
/*!40000 ALTER TABLE `performance_evaluation_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `performance_range`
--

DROP TABLE IF EXISTS `performance_range`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `performance_range` (
  `max_value` int DEFAULT NULL,
  `min_value` int DEFAULT NULL,
  `order_number` int DEFAULT NULL,
  `performance_range_id` int NOT NULL,
  `text` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`performance_range_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `performance_range`
--

LOCK TABLES `performance_range` WRITE;
/*!40000 ALTER TABLE `performance_range` DISABLE KEYS */;
/*!40000 ALTER TABLE `performance_range` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `performance_range_seq`
--

DROP TABLE IF EXISTS `performance_range_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `performance_range_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `performance_range_seq`
--

LOCK TABLES `performance_range_seq` WRITE;
/*!40000 ALTER TABLE `performance_range_seq` DISABLE KEYS */;
INSERT INTO `performance_range_seq` VALUES (1);
/*!40000 ALTER TABLE `performance_range_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `performance_time_line`
--

DROP TABLE IF EXISTS `performance_time_line`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `performance_time_line` (
  `is_done` bit(1) DEFAULT NULL,
  `performance_cycle_id` int DEFAULT NULL,
  `performance_time_line_id` int NOT NULL AUTO_INCREMENT,
  `due_date` datetime(6) DEFAULT NULL,
  `start_date` datetime(6) DEFAULT NULL,
  `performance_time_line_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`performance_time_line_id`),
  KEY `FK4bcbvuihp16q90hv854mbws11` (`performance_cycle_id`),
  CONSTRAINT `FK4bcbvuihp16q90hv854mbws11` FOREIGN KEY (`performance_cycle_id`) REFERENCES `performance_cycle` (`performance_cycle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `performance_time_line`
--

LOCK TABLES `performance_time_line` WRITE;
/*!40000 ALTER TABLE `performance_time_line` DISABLE KEYS */;
/*!40000 ALTER TABLE `performance_time_line` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `position`
--

DROP TABLE IF EXISTS `position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `position` (
  `has_department` bit(1) DEFAULT NULL,
  `has_level` bit(1) DEFAULT NULL,
  `position_id` int NOT NULL AUTO_INCREMENT,
  `insertion_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modification_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `position_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`position_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `position`
--

LOCK TABLES `position` WRITE;
/*!40000 ALTER TABLE `position` DISABLE KEYS */;
INSERT INTO `position` VALUES (NULL,NULL,1,'2023-12-18 07:09:21','2023-12-18 07:09:21','Software Engineer');
/*!40000 ALTER TABLE `position` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `position_department`
--

DROP TABLE IF EXISTS `position_department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `position_department` (
  `department_id` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `position_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKol7ptqv0lksoofgd132258rod` (`department_id`),
  KEY `FKkfryjb9te3o0fktxxkc42jkpn` (`position_id`),
  CONSTRAINT `FKkfryjb9te3o0fktxxkc42jkpn` FOREIGN KEY (`position_id`) REFERENCES `position` (`position_id`),
  CONSTRAINT `FKol7ptqv0lksoofgd132258rod` FOREIGN KEY (`department_id`) REFERENCES `department` (`department_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `position_department`
--

LOCK TABLES `position_department` WRITE;
/*!40000 ALTER TABLE `position_department` DISABLE KEYS */;
/*!40000 ALTER TABLE `position_department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `position_job_level_skill_set`
--

DROP TABLE IF EXISTS `position_job_level_skill_set`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `position_job_level_skill_set` (
  `id` int NOT NULL AUTO_INCREMENT,
  `job_level_id` int DEFAULT NULL,
  `position_id` int DEFAULT NULL,
  `proficiency_level_id` int DEFAULT NULL,
  `skill_set_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKodmd9vmrtfaatmyds1p9c0fts` (`job_level_id`),
  KEY `FKknk1pqd4bam9ek88ggsi9ip3f` (`position_id`),
  KEY `FKhk02lh24c3jfbkd8twndftaj8` (`proficiency_level_id`),
  KEY `FKj7pvmcetf22v8rkemc3mb87s5` (`skill_set_id`),
  CONSTRAINT `FKhk02lh24c3jfbkd8twndftaj8` FOREIGN KEY (`proficiency_level_id`) REFERENCES `proficiency_level` (`proficiency_level_id`),
  CONSTRAINT `FKj7pvmcetf22v8rkemc3mb87s5` FOREIGN KEY (`skill_set_id`) REFERENCES `skill_set` (`skill_set_id`),
  CONSTRAINT `FKknk1pqd4bam9ek88ggsi9ip3f` FOREIGN KEY (`position_id`) REFERENCES `position` (`position_id`),
  CONSTRAINT `FKodmd9vmrtfaatmyds1p9c0fts` FOREIGN KEY (`job_level_id`) REFERENCES `job_level` (`job_level_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `position_job_level_skill_set`
--

LOCK TABLES `position_job_level_skill_set` WRITE;
/*!40000 ALTER TABLE `position_job_level_skill_set` DISABLE KEYS */;
INSERT INTO `position_job_level_skill_set` VALUES (1,1,1,NULL,1),(2,1,1,NULL,2);
/*!40000 ALTER TABLE `position_job_level_skill_set` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `position_level`
--

DROP TABLE IF EXISTS `position_level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `position_level` (
  `job_level_id` int DEFAULT NULL,
  `position_id` int DEFAULT NULL,
  `position_level_id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`position_level_id`),
  KEY `FKo4o23lua6ulk8nmci7b4uh2mg` (`job_level_id`),
  KEY `FKp1d3h3l8s3srwwxn8amfwpo4m` (`position_id`),
  CONSTRAINT `FKo4o23lua6ulk8nmci7b4uh2mg` FOREIGN KEY (`job_level_id`) REFERENCES `job_level` (`job_level_id`),
  CONSTRAINT `FKp1d3h3l8s3srwwxn8amfwpo4m` FOREIGN KEY (`position_id`) REFERENCES `position` (`position_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `position_level`
--

LOCK TABLES `position_level` WRITE;
/*!40000 ALTER TABLE `position_level` DISABLE KEYS */;
INSERT INTO `position_level` VALUES (1,1,1),(2,1,2);
/*!40000 ALTER TABLE `position_level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `position_level_path`
--

DROP TABLE IF EXISTS `position_level_path`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `position_level_path` (
  `current_position_level_id` int DEFAULT NULL,
  `id` int NOT NULL,
  `next_position_level_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcw7oy0468tdqhpauv4mkok0ie` (`current_position_level_id`),
  KEY `FKb44xabv5ljx0qr25a8v0nreri` (`next_position_level_id`),
  CONSTRAINT `FKb44xabv5ljx0qr25a8v0nreri` FOREIGN KEY (`next_position_level_id`) REFERENCES `position_level` (`position_level_id`),
  CONSTRAINT `FKcw7oy0468tdqhpauv4mkok0ie` FOREIGN KEY (`current_position_level_id`) REFERENCES `position_level` (`position_level_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `position_level_path`
--

LOCK TABLES `position_level_path` WRITE;
/*!40000 ALTER TABLE `position_level_path` DISABLE KEYS */;
INSERT INTO `position_level_path` VALUES (1,1,2);
/*!40000 ALTER TABLE `position_level_path` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `position_level_path_seq`
--

DROP TABLE IF EXISTS `position_level_path_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `position_level_path_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `position_level_path_seq`
--

LOCK TABLES `position_level_path_seq` WRITE;
/*!40000 ALTER TABLE `position_level_path_seq` DISABLE KEYS */;
INSERT INTO `position_level_path_seq` VALUES (51);
/*!40000 ALTER TABLE `position_level_path_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `position_skill_set`
--

DROP TABLE IF EXISTS `position_skill_set`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `position_skill_set` (
  `position_id` int DEFAULT NULL,
  `position_skill_set_id` int NOT NULL AUTO_INCREMENT,
  `skill_set_id` int DEFAULT NULL,
  PRIMARY KEY (`position_skill_set_id`),
  KEY `FKnudiqlqba4617tl6bf054t7wi` (`position_id`),
  KEY `FKt95n09g0cmvuyiqu45aqhubjg` (`skill_set_id`),
  CONSTRAINT `FKnudiqlqba4617tl6bf054t7wi` FOREIGN KEY (`position_id`) REFERENCES `position` (`position_id`),
  CONSTRAINT `FKt95n09g0cmvuyiqu45aqhubjg` FOREIGN KEY (`skill_set_id`) REFERENCES `skill_set` (`skill_set_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `position_skill_set`
--

LOCK TABLES `position_skill_set` WRITE;
/*!40000 ALTER TABLE `position_skill_set` DISABLE KEYS */;
/*!40000 ALTER TABLE `position_skill_set` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proficiency_level`
--

DROP TABLE IF EXISTS `proficiency_level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proficiency_level` (
  `proficiency_level_id` int NOT NULL AUTO_INCREMENT,
  `score` float DEFAULT NULL,
  `proficiency_level_description` varchar(255) DEFAULT NULL,
  `proficiency_level_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`proficiency_level_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proficiency_level`
--

LOCK TABLES `proficiency_level` WRITE;
/*!40000 ALTER TABLE `proficiency_level` DISABLE KEYS */;
/*!40000 ALTER TABLE `proficiency_level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `question_id` int NOT NULL AUTO_INCREMENT,
  `question_weight` float DEFAULT NULL,
  `question_description` varchar(255) DEFAULT NULL,
  `question_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`question_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (1,NULL,NULL,'Question 1');
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ADMIN'),(2,'MANAGER'),(3,'USER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `skill`
--

DROP TABLE IF EXISTS `skill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `skill` (
  `skill_id` int NOT NULL AUTO_INCREMENT,
  `skill_set_id` int DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `skill_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`skill_id`),
  KEY `FKcmr3v47wk1emgw4c1djuvw15c` (`skill_set_id`),
  CONSTRAINT `FKcmr3v47wk1emgw4c1djuvw15c` FOREIGN KEY (`skill_set_id`) REFERENCES `skill_set` (`skill_set_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skill`
--

LOCK TABLES `skill` WRITE;
/*!40000 ALTER TABLE `skill` DISABLE KEYS */;
/*!40000 ALTER TABLE `skill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `skill_set`
--

DROP TABLE IF EXISTS `skill_set`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `skill_set` (
  `competency_id` int DEFAULT NULL,
  `skill_set_id` int NOT NULL AUTO_INCREMENT,
  `insertion_time` datetime(6) DEFAULT NULL,
  `modification_time` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `skill_set_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`skill_set_id`),
  KEY `FKgsg0dkkewidppp6rpwdskcqsf` (`competency_id`),
  CONSTRAINT `FKgsg0dkkewidppp6rpwdskcqsf` FOREIGN KEY (`competency_id`) REFERENCES `competency` (`competency_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skill_set`
--

LOCK TABLES `skill_set` WRITE;
/*!40000 ALTER TABLE `skill_set` DISABLE KEYS */;
INSERT INTO `skill_set` VALUES (NULL,1,'2023-12-18 14:09:20.830000','2023-12-18 14:09:20.830000',NULL,'Java'),(NULL,2,'2023-12-18 14:09:20.830000','2023-12-18 14:09:20.830000',NULL,'Python');
/*!40000 ALTER TABLE `skill_set` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `skill_set_evaluation`
--

DROP TABLE IF EXISTS `skill_set_evaluation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `skill_set_evaluation` (
  `competency_cycle_id` int DEFAULT NULL,
  `employee_id` int DEFAULT NULL,
  `evaluator_score` int DEFAULT NULL,
  `final_score` int DEFAULT NULL,
  `self_score` int DEFAULT NULL,
  `skill_set_evaluation_id` int NOT NULL AUTO_INCREMENT,
  `skill_set_id` int DEFAULT NULL,
  PRIMARY KEY (`skill_set_evaluation_id`),
  KEY `FKnmg9rioid1bcw3g8hfumyxn4h` (`competency_cycle_id`),
  KEY `FK933fk71vc7qjxi72m7lcwbuhx` (`employee_id`),
  KEY `FKd98tikyumq5rcmvqab802758a` (`skill_set_id`),
  CONSTRAINT `FK933fk71vc7qjxi72m7lcwbuhx` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`),
  CONSTRAINT `FKd98tikyumq5rcmvqab802758a` FOREIGN KEY (`skill_set_id`) REFERENCES `skill_set` (`skill_set_id`),
  CONSTRAINT `FKnmg9rioid1bcw3g8hfumyxn4h` FOREIGN KEY (`competency_cycle_id`) REFERENCES `competency_cycle` (`competency_cycle_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skill_set_evaluation`
--

LOCK TABLES `skill_set_evaluation` WRITE;
/*!40000 ALTER TABLE `skill_set_evaluation` DISABLE KEYS */;
INSERT INTO `skill_set_evaluation` VALUES (NULL,1,4,4,4,1,1);
/*!40000 ALTER TABLE `skill_set_evaluation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `skill_set_target`
--

DROP TABLE IF EXISTS `skill_set_target`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `skill_set_target` (
  `competency_cycle_id` int DEFAULT NULL,
  `employee_id` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `skill_set_id` int DEFAULT NULL,
  `target_proficiency_level_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2fac7xagmo0aubyq3lsrwsd3y` (`competency_cycle_id`),
  KEY `FK4kmb0ptqcl2f1kq32666dcit4` (`employee_id`),
  KEY `FKg73qbe18w7qw4cxiiswl2c48u` (`skill_set_id`),
  KEY `FKhwuyrw86kdhb794ycdw0yjoah` (`target_proficiency_level_id`),
  CONSTRAINT `FK2fac7xagmo0aubyq3lsrwsd3y` FOREIGN KEY (`competency_cycle_id`) REFERENCES `competency_cycle` (`competency_cycle_id`),
  CONSTRAINT `FK4kmb0ptqcl2f1kq32666dcit4` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`),
  CONSTRAINT `FKg73qbe18w7qw4cxiiswl2c48u` FOREIGN KEY (`skill_set_id`) REFERENCES `skill_set` (`skill_set_id`),
  CONSTRAINT `FKhwuyrw86kdhb794ycdw0yjoah` FOREIGN KEY (`target_proficiency_level_id`) REFERENCES `proficiency_level` (`proficiency_level_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skill_set_target`
--

LOCK TABLES `skill_set_target` WRITE;
/*!40000 ALTER TABLE `skill_set_target` DISABLE KEYS */;
/*!40000 ALTER TABLE `skill_set_target` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `template`
--

DROP TABLE IF EXISTS `template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `template` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `employee_id` int DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `template_description` varchar(255) DEFAULT NULL,
  `template_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`category_id`),
  KEY `FK7edmxwdrpyvffpxf3wn5psx7p` (`employee_id`),
  CONSTRAINT `FK7edmxwdrpyvffpxf3wn5psx7p` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `template`
--

LOCK TABLES `template` WRITE;
/*!40000 ALTER TABLE `template` DISABLE KEYS */;
INSERT INTO `template` VALUES (1,NULL,'2023-12-18 04:41:45',NULL,'Template 1');
/*!40000 ALTER TABLE `template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `template_category`
--

DROP TABLE IF EXISTS `template_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `template_category` (
  `category_id` int DEFAULT NULL,
  `template_category_id` int NOT NULL AUTO_INCREMENT,
  `template_id` int DEFAULT NULL,
  PRIMARY KEY (`template_category_id`),
  KEY `FKgl1lrowluheivyhjyytvpi0al` (`category_id`),
  KEY `FK1jc4s4h1fucvusdub3mqssrpy` (`template_id`),
  CONSTRAINT `FK1jc4s4h1fucvusdub3mqssrpy` FOREIGN KEY (`template_id`) REFERENCES `template` (`category_id`),
  CONSTRAINT `FKgl1lrowluheivyhjyytvpi0al` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `template_category`
--

LOCK TABLES `template_category` WRITE;
/*!40000 ALTER TABLE `template_category` DISABLE KEYS */;
INSERT INTO `template_category` VALUES (1,1,1),(1,2,1),(1,3,1),(1,4,1),(1,5,1),(1,6,1);
/*!40000 ALTER TABLE `template_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
  `role_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `user_role_id` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`user_role_id`),
  KEY `FKt7e7djp752sqn6w22i6ocqy6q` (`role_id`),
  KEY `FKj345gk1bovqvfame88rcx7yyx` (`user_id`),
  CONSTRAINT `FKj345gk1bovqvfame88rcx7yyx` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FKt7e7djp752sqn6w22i6ocqy6q` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `created_at` date DEFAULT NULL,
  `employee_id` int DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `user_id` int NOT NULL AUTO_INCREMENT,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_d1s31g1a7ilra77m65xmka3ei` (`employee_id`),
  UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`),
  CONSTRAINT `FKfndbe67uw6silwqnlyudtwqmo` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('2023-12-13',NULL,_binary '\0',1,'test','test');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-04 21:23:21
