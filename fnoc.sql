-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : dim. 07 avr. 2024 à 13:24
-- Version du serveur : 8.0.31
-- Version de PHP : 8.0.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `fnoc`
--

-- --------------------------------------------------------

--
-- Structure de la table `projet`
--

DROP TABLE IF EXISTS `projet`;
CREATE TABLE IF NOT EXISTS `projet` (
  `id` int NOT NULL AUTO_INCREMENT,
  `mail` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `projet`
--

INSERT INTO `projet` (`id`, `mail`, `password`) VALUES
(1, 'test@gmail.com', 'basket26');

-- --------------------------------------------------------

--
-- Structure de la table `reclamation`
--

DROP TABLE IF EXISTS `reclamation`;
CREATE TABLE IF NOT EXISTS `reclamation` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) NOT NULL,
  `prenom` varchar(255) NOT NULL,
  `telephone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `reclamation` varchar(255) NOT NULL,
  `Date` varchar(255) NOT NULL,
  `Numero_du_tickets` varchar(255) NOT NULL,
  `Etat_du_ticket` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `reclamation`
--

INSERT INTO `reclamation` (`id`, `nom`, `prenom`, `telephone`, `reclamation`, `Date`, `Numero_du_tickets`, `Etat_du_ticket`) VALUES
(33, 'mathis', '', 'Ne fonctionne plus', '', '2024-02-25', '719', 'Ouvert'),
(32, 'darde', '', 'Ne fonctionne plus', '', '2024-02-25', '463', 'Ouvert'),
(31, 'darde', '', 'Ne fonctionne plus', '', '2024-02-25', '200', 'Ouvert'),
(28, 'darde', 'mathis', 'Ne S\'allume plus', 'probleme d\'ordi', '2023-12-11', '541', 'Ouvert'),
(29, 'darde', 'mathis', 'Ne S\'allume plus', 'probleme d\'ordi', '2023-12-11', '542', 'Ouvert');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
