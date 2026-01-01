CREATE DATABASE  IF NOT EXISTS `yurt_otomasyon` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `yurt_otomasyon`;
-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: yurt_otomasyon
-- ------------------------------------------------------
-- Server version	8.0.44

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
-- Table structure for table `kullanicilar`
--

DROP TABLE IF EXISTS `kullanicilar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kullanicilar` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tc` varchar(11) NOT NULL,
  `sifre` varchar(50) NOT NULL,
  `ad` varchar(50) DEFAULT NULL,
  `soyad` varchar(50) DEFAULT NULL,
  `tip` varchar(10) DEFAULT NULL,
  `oda_no` varchar(10) DEFAULT NULL,
  `yurtta_mi` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `tc` (`tc`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kullanicilar`
--

LOCK TABLES `kullanicilar` WRITE;
/*!40000 ALTER TABLE `kullanicilar` DISABLE KEYS */;
INSERT INTO `kullanicilar` VALUES (1,'12345678901','123','Ahmet','Yılmaz','IDARE',NULL,1),(2,'11111111111','111','Mehmet','Demir','OGRENCI','101',1),(3,'10091231962','111','Yeni','Öğrenci','OGRENCI','102',1);
/*!40000 ALTER TABLE `kullanicilar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `makineler`
--

DROP TABLE IF EXISTS `makineler`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `makineler` (
  `id` int NOT NULL AUTO_INCREMENT,
  `makine_no` int DEFAULT NULL,
  `bitis_zamani` datetime DEFAULT NULL,
  `dolu_mu` tinyint(1) DEFAULT '0',
  `ogr_ad` varchar(50) DEFAULT NULL,
  `ogr_soyad` varchar(50) DEFAULT NULL,
  `ogr_tel` varchar(15) DEFAULT NULL,
  `ogr_oda` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `makine_no` (`makine_no`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `makineler`
--

LOCK TABLES `makineler` WRITE;
/*!40000 ALTER TABLE `makineler` DISABLE KEYS */;
INSERT INTO `makineler` VALUES (1,8,'2025-12-29 01:30:52',1,'ABDULLAH Kdir','eroğlu','05510282321','b202'),(2,15,'2025-12-28 20:45:33',1,'KADİR','EROĞLU','5510282321','B202'),(4,7,'2025-12-29 14:11:42',1,'jjnk',NULL,NULL,'b202'),(5,1,'2025-12-30 17:36:08',1,'efe',NULL,NULL,'1'),(6,13,'2025-12-30 17:36:13',1,'efe',NULL,NULL,'2'),(7,19,'2025-12-30 17:36:16',1,'3r3',NULL,NULL,'efe'),(8,22,'2025-12-30 17:36:21',1,'efe',NULL,NULL,'23'),(9,30,'2025-12-30 17:36:24',1,'ef',NULL,NULL,'232'),(10,26,'2025-12-30 17:36:28',1,'efe',NULL,NULL,'3434'),(11,10,'2025-12-30 17:36:32',1,'efe',NULL,NULL,'324'),(12,3,'2025-12-30 17:36:56',1,'efe',NULL,NULL,'123'),(13,11,'2025-12-30 17:59:55',1,'efe',NULL,NULL,'123');
/*!40000 ALTER TABLE `makineler` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teknik_talepler`
--

DROP TABLE IF EXISTS `teknik_talepler`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teknik_talepler` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ogrenci_id` int DEFAULT NULL,
  `kategori` varchar(50) DEFAULT NULL,
  `aciklama` text,
  `durum` varchar(20) DEFAULT 'Beklemede',
  PRIMARY KEY (`id`),
  KEY `ogrenci_id` (`ogrenci_id`),
  CONSTRAINT `teknik_talepler_ibfk_1` FOREIGN KEY (`ogrenci_id`) REFERENCES `kullanicilar` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teknik_talepler`
--

LOCK TABLES `teknik_talepler` WRITE;
/*!40000 ALTER TABLE `teknik_talepler` DISABLE KEYS */;
INSERT INTO `teknik_talepler` VALUES (1,3,'İnternet','wifi gsb cekmıyot','Beklemede'),(2,3,'Elektrik','WİFİ GSB KOTU','Beklemede'),(3,3,'Mobilya','ewtw4tr','Beklemede'),(4,3,'Mobilya Kırığı','bvjvjhvhjvjhvj','Beklemede'),(5,3,'Mobilya Kırığı','ss','Beklemede'),(6,3,'Banyo Arızası','dusakabın kırık','Beklemede');
/*!40000 ALTER TABLE `teknik_talepler` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `urunler`
--

DROP TABLE IF EXISTS `urunler`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `urunler` (
  `id` int NOT NULL AUTO_INCREMENT,
  `isim` varchar(100) DEFAULT NULL,
  `fiyat` double DEFAULT NULL,
  `resim_yolu` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `urunler`
--

LOCK TABLES `urunler` WRITE;
/*!40000 ALTER TABLE `urunler` DISABLE KEYS */;
INSERT INTO `urunler` VALUES (12,'PATOS',45,'images/kantin-patospng.png'),(15,'KUMRU',35,'images/kantin-kumru.png'),(16,'2023 MODEL AŞİRET PAKET PASSAT',900,'images/kantin-passat.png'),(17,'DUR TABELASI',250,'images/kantin-durtabelası.png'),(18,'KARIŞIK TOST',40,'images/kantin-karısıktost.png'),(19,'KETTLE',199,'images/kantin-kettle.png'),(20,'PEUGEOT ÖN FAR SIFIR AYARINDA',6500,'images/kantin-peugeotfar.png'),(21,'AVOMETRE',599,'images/kantin-avometre.png'),(23,'PATSO',31,'images/kantin-patso.png'),(24,'7 NOLU OTOBÜS',24,'images/kantin-7nolu.png'),(27,'java',10,'');
/*!40000 ALTER TABLE `urunler` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `yemek_menusu`
--

DROP TABLE IF EXISTS `yemek_menusu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `yemek_menusu` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tarih` date DEFAULT NULL,
  `liste` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tarih` (`tarih`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `yemek_menusu`
--

LOCK TABLES `yemek_menusu` WRITE;
/*!40000 ALTER TABLE `yemek_menusu` DISABLE KEYS */;
INSERT INTO `yemek_menusu` VALUES (1,'2025-12-28','Yayla Çorbası, Tavuk Haşlama, Bulgur Pilavı, Meyve'),(2,'2025-12-29','Yayla Çorbası, Tavuk Haşlama, Bulgur Pilavı, Meyve'),(3,'2025-12-30','auerc apejrc ');
/*!40000 ALTER TABLE `yemek_menusu` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-01 21:28:27
