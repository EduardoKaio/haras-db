-- Amplia a coluna de senha da Pessoa para armazenar hash (ex: BCrypt = 60 chars).
-- VARCHAR(255) mantém a coluna agnóstica ao algoritmo de hash usado no futuro.
USE `haras_db`;

ALTER TABLE `haras_db`.`Pessoa`
  MODIFY COLUMN `senha` VARCHAR(255) NOT NULL;
