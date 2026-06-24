
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

CREATE SCHEMA IF NOT EXISTS `haras_db` DEFAULT CHARACTER SET utf8 ;
USE `haras_db` ;

CREATE TABLE IF NOT EXISTS `haras_db`.`Pessoa` (
  `id_pessoa` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(100) NOT NULL,
  `data_nascimento` DATE NOT NULL,
  `cpf` VARCHAR(11) NOT NULL,
  `is_gerente` TINYINT NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `senha` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_pessoa`),
  UNIQUE INDEX `uq_pessoa_cpf` (`cpf` ASC),
  UNIQUE INDEX `uq_pessoa_email` (`email` ASC))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`Equino` (
  `id_equino` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NOT NULL,
  `raca` VARCHAR(45) NOT NULL,
  `peso` DOUBLE NOT NULL,
  `funcao` VARCHAR(25) NOT NULL,
  `data_nascimento` DATE NOT NULL,
  `status` VARCHAR(25) NOT NULL,
  `registro` VARCHAR(25) NOT NULL,
  `registro_pai` VARCHAR(25) NULL,
  `registro_mae` VARCHAR(25) NULL,
  `pelagem` VARCHAR(25) NOT NULL,
  PRIMARY KEY (`id_equino`),
  UNIQUE INDEX `uq_equino_registro` (`registro` ASC))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`MedicoVeterinario` (
  `id_pessoa` INT NOT NULL,
  `num_crmv` VARCHAR(5) NOT NULL,
  `uf_crmv` VARCHAR(2) NOT NULL,
  UNIQUE INDEX `uq_medico_crmv` (`num_crmv` ASC, `uf_crmv` ASC),
  PRIMARY KEY (`id_pessoa`),
  CONSTRAINT `fk_MedicoVeterinario_Pessoa1`
    FOREIGN KEY (`id_pessoa`)
    REFERENCES `haras_db`.`Pessoa` (`id_pessoa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`Atendimento` (
  `id_atendimento` INT NOT NULL AUTO_INCREMENT,
  `valor` DOUBLE NOT NULL,
  `data_hora` DATETIME NOT NULL,
  `diagnostico` TINYTEXT NOT NULL,
  `motivo` VARCHAR(200) NOT NULL,
  `id_pessoa` INT NOT NULL,
  `id_equino` INT NOT NULL,
  PRIMARY KEY (`id_atendimento`),
  INDEX `fk_Atendimento_MedicoVeterinario1_idx` (`id_pessoa` ASC) VISIBLE,
  INDEX `fk_Atendimento_Equino1_idx` (`id_equino` ASC) VISIBLE,
  CONSTRAINT `fk_Atendimento_MedicoVeterinario1`
    FOREIGN KEY (`id_pessoa`)
    REFERENCES `haras_db`.`MedicoVeterinario` (`id_pessoa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Atendimento_Equino1`
    FOREIGN KEY (`id_equino`)
    REFERENCES `haras_db`.`Equino` (`id_equino`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`Colaborador` (
  `id_pessoa` INT NOT NULL,
  PRIMARY KEY (`id_pessoa`),
  CONSTRAINT `fk_Colaborador_Pessoa1`
    FOREIGN KEY (`id_pessoa`)
    REFERENCES `haras_db`.`Pessoa` (`id_pessoa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`Contrato` (
  `id_contrato` INT NOT NULL AUTO_INCREMENT,
  `data_fim` DATE NULL,
  `data_inicio` DATE NOT NULL,
  `salario` DOUBLE NOT NULL,
  `id_pessoa` INT NOT NULL,
  PRIMARY KEY (`id_contrato`),
  INDEX `fk_Contrato_Colaborador1_idx` (`id_pessoa` ASC) VISIBLE,
  CONSTRAINT `fk_Contrato_Colaborador1`
    FOREIGN KEY (`id_pessoa`)
    REFERENCES `haras_db`.`Colaborador` (`id_pessoa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`Limpeza` (
  `id_limpeza` INT NOT NULL AUTO_INCREMENT,
  `data_hora` DATETIME NOT NULL,
  PRIMARY KEY (`id_limpeza`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`Proprietario` (
  `id_pessoa` INT NOT NULL,
  PRIMARY KEY (`id_pessoa`),
  CONSTRAINT `fk_Proprietario_Pessoa1`
    FOREIGN KEY (`id_pessoa`)
    REFERENCES `haras_db`.`Pessoa` (`id_pessoa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`Competicao` (
  `id_competicao` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(100) NOT NULL,
  `data_inicio` DATE NOT NULL,
  `cidade` VARCHAR(45) NOT NULL,
  `uf` VARCHAR(2) NOT NULL,
  `data_fim` DATE NULL,
  PRIMARY KEY (`id_competicao`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`Categoria` (
  `id_categoria` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(25) NOT NULL,
  PRIMARY KEY (`id_categoria`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`Competidor_Treinador` (
  `id_pessoa` INT NOT NULL,
  PRIMARY KEY (`id_pessoa`),
  CONSTRAINT `fk_Competidor_Treinador_Colaborador1`
    FOREIGN KEY (`id_pessoa`)
    REFERENCES `haras_db`.`Colaborador` (`id_pessoa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`Competidor` (
  `id_pessoa` INT NOT NULL,
  PRIMARY KEY (`id_pessoa`),
  CONSTRAINT `fk_Competidor_Competidor_Treinador1`
    FOREIGN KEY (`id_pessoa`)
    REFERENCES `haras_db`.`Competidor_Treinador` (`id_pessoa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`Senha` (
  `id_senha` INT NOT NULL AUTO_INCREMENT,
  `valor` DOUBLE NOT NULL,
  `numero` INT NOT NULL,
  `id_pessoa` INT NOT NULL,
  `id_competicao` INT NOT NULL,
  `id_categoria` INT NOT NULL,
  `id_equino` INT NOT NULL,
  PRIMARY KEY (`id_senha`),
  INDEX `fk_Senha_Competicao1_idx` (`id_competicao` ASC) VISIBLE,
  INDEX `fk_Senha_Categoria1_idx` (`id_categoria` ASC) VISIBLE,
  INDEX `fk_Senha_Equino1_idx` (`id_equino` ASC) VISIBLE,
  INDEX `fk_Senha_Competidor1_idx` (`id_pessoa` ASC) VISIBLE,
  CONSTRAINT `fk_Senha_Competicao1`
    FOREIGN KEY (`id_competicao`)
    REFERENCES `haras_db`.`Competicao` (`id_competicao`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Senha_Categoria1`
    FOREIGN KEY (`id_categoria`)
    REFERENCES `haras_db`.`Categoria` (`id_categoria`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Senha_Equino1`
    FOREIGN KEY (`id_equino`)
    REFERENCES `haras_db`.`Equino` (`id_equino`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Senha_Competidor1`
    FOREIGN KEY (`id_pessoa`)
    REFERENCES `haras_db`.`Competidor` (`id_pessoa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`Resultado` (
  `id_resultado` INT NOT NULL AUTO_INCREMENT,
  `valor_premiacao` DOUBLE NOT NULL,
  `colocacao` INT NULL,
  `id_senha` INT NOT NULL,
  PRIMARY KEY (`id_resultado`),
  INDEX `fk_Resultado_Senha1_idx` (`id_senha` ASC) VISIBLE,
  CONSTRAINT `fk_Resultado_Senha1`
    FOREIGN KEY (`id_senha`)
    REFERENCES `haras_db`.`Senha` (`id_senha`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`Tratamento` (
  `tratamento` TINYTEXT NOT NULL,
  `id_atendimento` INT NOT NULL,
  PRIMARY KEY (`id_atendimento`, `tratamento`(60)),
  CONSTRAINT `fk_Tratamento_Atendimento1`
    FOREIGN KEY (`id_atendimento`)
    REFERENCES `haras_db`.`Atendimento` (`id_atendimento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`ItemEstoque` (
  `id_item_estoque` INT NOT NULL AUTO_INCREMENT,
  `unidade_medida` VARCHAR(2) NOT NULL,
  `data_validade` DATE NOT NULL,
  `nome` VARCHAR(100) NOT NULL,
  `quantidade_atual` DOUBLE NOT NULL,
  PRIMARY KEY (`id_item_estoque`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`Movimentacao` (
  `id_movimentacao` INT NOT NULL AUTO_INCREMENT,
  `valor_unitario` DOUBLE NOT NULL,
  `quantidade_movimentada` DOUBLE NOT NULL,
  `data_hora` DATETIME NOT NULL,
  `tipo_movimento` VARCHAR(10) NOT NULL,
  `motivo_movimento` VARCHAR(50) NOT NULL,
  `id_item_estoque` INT NOT NULL,
  PRIMARY KEY (`id_movimentacao`),
  INDEX `fk_Movimentacao_ItemEstoque1_idx` (`id_item_estoque` ASC) VISIBLE,
  CONSTRAINT `fk_Movimentacao_ItemEstoque1`
    FOREIGN KEY (`id_item_estoque`)
    REFERENCES `haras_db`.`ItemEstoque` (`id_item_estoque`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`Telefone` (
  `telefone` VARCHAR(11) NOT NULL,
  `id_pessoa` INT NOT NULL,
  PRIMARY KEY (`telefone`),
  CONSTRAINT `fk_Telefone_Pessoa`
    FOREIGN KEY (`id_pessoa`)
    REFERENCES `haras_db`.`Pessoa` (`id_pessoa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`Treinador` (
  `id_pessoa` INT NOT NULL,
  PRIMARY KEY (`id_pessoa`),
  CONSTRAINT `fk_Treinador_Competidor_Treinador1`
    FOREIGN KEY (`id_pessoa`)
    REFERENCES `haras_db`.`Competidor_Treinador` (`id_pessoa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`Treino` (
  `id_treino` INT NOT NULL AUTO_INCREMENT,
  `status` VARCHAR(45) NOT NULL,
  `observacoes` VARCHAR(45) NULL,
  `data_hora_fim` DATETIME NULL,
  `data_hora_inicio` DATETIME NOT NULL,
  `desempenho` DOUBLE NULL,
  `id_equino` INT NOT NULL,
  `id_pessoa` INT NOT NULL,
  PRIMARY KEY (`id_treino`),
  INDEX `fk_Treino_Equino1_idx` (`id_equino` ASC) VISIBLE,
  INDEX `fk_Treino_Treinador1_idx` (`id_pessoa` ASC) VISIBLE,
  CONSTRAINT `fk_Treino_Equino1`
    FOREIGN KEY (`id_equino`)
    REFERENCES `haras_db`.`Equino` (`id_equino`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Treino_Treinador1`
    FOREIGN KEY (`id_pessoa`)
    REFERENCES `haras_db`.`Treinador` (`id_pessoa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`Limpeza_has_Equino` (
  `id_limpeza` INT NOT NULL,
  `id_equino` INT NOT NULL,
  PRIMARY KEY (`id_limpeza`, `id_equino`),
  INDEX `fk_Limpeza_has_Equino_Equino1_idx` (`id_equino` ASC) VISIBLE,
  INDEX `fk_Limpeza_has_Equino_Limpeza1_idx` (`id_limpeza` ASC) VISIBLE,
  CONSTRAINT `fk_Limpeza_has_Equino_Limpeza1`
    FOREIGN KEY (`id_limpeza`)
    REFERENCES `haras_db`.`Limpeza` (`id_limpeza`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Limpeza_has_Equino_Equino1`
    FOREIGN KEY (`id_equino`)
    REFERENCES `haras_db`.`Equino` (`id_equino`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`Medicamento` (
  `id_item_estoque` INT NOT NULL,
  INDEX `fk_Alimento_ItemEstoque1_idx` (`id_item_estoque` ASC) VISIBLE,
  PRIMARY KEY (`id_item_estoque`),
  CONSTRAINT `fk_Alimento_ItemEstoque10`
    FOREIGN KEY (`id_item_estoque`)
    REFERENCES `haras_db`.`ItemEstoque` (`id_item_estoque`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`Atendimento_has_Medicamento` (
  `id_atendimento` INT NOT NULL,
  `id_item_estoque` INT NOT NULL,
  `dosagem` DOUBLE NOT NULL,
  PRIMARY KEY (`id_atendimento`, `id_item_estoque`),
  INDEX `fk_Atendimento_has_Medicamento_Atendimento1_idx` (`id_atendimento` ASC) VISIBLE,
  INDEX `fk_Atendimento_has_Medicamento_Medicamento1_idx` (`id_item_estoque` ASC) VISIBLE,
  CONSTRAINT `fk_Atendimento_has_Medicamento_Atendimento1`
    FOREIGN KEY (`id_atendimento`)
    REFERENCES `haras_db`.`Atendimento` (`id_atendimento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Atendimento_has_Medicamento_Medicamento1`
    FOREIGN KEY (`id_item_estoque`)
    REFERENCES `haras_db`.`Medicamento` (`id_item_estoque`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`Tratador` (
  `id_pessoa` INT NOT NULL,
  PRIMARY KEY (`id_pessoa`),
  INDEX `fk_Tratador_Colaborador1_idx` (`id_pessoa` ASC) VISIBLE,
  CONSTRAINT `fk_Tratador_Colaborador1`
    FOREIGN KEY (`id_pessoa`)
    REFERENCES `haras_db`.`Colaborador` (`id_pessoa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`Tratador_has_Limpeza` (
  `id_pessoa` INT NOT NULL,
  `id_limpeza` INT NOT NULL,
  PRIMARY KEY (`id_pessoa`, `id_limpeza`),
  INDEX `fk_Tratador_has_Limpeza_Limpeza1_idx` (`id_limpeza` ASC) VISIBLE,
  INDEX `fk_Tratador_has_Limpeza_Tratador1_idx` (`id_pessoa` ASC) VISIBLE,
  CONSTRAINT `fk_Tratador_has_Limpeza_Tratador1`
    FOREIGN KEY (`id_pessoa`)
    REFERENCES `haras_db`.`Tratador` (`id_pessoa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Tratador_has_Limpeza_Limpeza1`
    FOREIGN KEY (`id_limpeza`)
    REFERENCES `haras_db`.`Limpeza` (`id_limpeza`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`Proprietario_has_Equino` (
  `id_pessoa` INT NOT NULL,
  `id_equino` INT NOT NULL,
  PRIMARY KEY (`id_pessoa`, `id_equino`),
  INDEX `fk_Proprietario_has_Equino_Equino1_idx` (`id_equino` ASC) VISIBLE,
  INDEX `fk_Proprietario_has_Equino_Proprietario1_idx` (`id_pessoa` ASC) VISIBLE,
  CONSTRAINT `fk_Proprietario_has_Equino_Proprietario1`
    FOREIGN KEY (`id_pessoa`)
    REFERENCES `haras_db`.`Proprietario` (`id_pessoa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Proprietario_has_Equino_Equino1`
    FOREIGN KEY (`id_equino`)
    REFERENCES `haras_db`.`Equino` (`id_equino`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`Alimento` (
  `id_item_estoque` INT NOT NULL,
  INDEX `fk_Alimento_ItemEstoque1_idx` (`id_item_estoque` ASC) VISIBLE,
  PRIMARY KEY (`id_item_estoque`),
  CONSTRAINT `fk_Alimento_ItemEstoque1`
    FOREIGN KEY (`id_item_estoque`)
    REFERENCES `haras_db`.`ItemEstoque` (`id_item_estoque`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `haras_db`.`Alimentacao` (
  `id_alimentacao` INT NOT NULL AUTO_INCREMENT,
  `quantidade` DOUBLE NOT NULL,
  `data_hora` DATETIME NOT NULL,
  `id_equino` INT NOT NULL,
  `id_item_estoque` INT NOT NULL,
  PRIMARY KEY (`id_alimentacao`),
  INDEX `fk_Alimentacao_Equino1_idx` (`id_equino` ASC) VISIBLE,
  INDEX `fk_Alimentacao_Alimento1_idx` (`id_item_estoque` ASC) VISIBLE,
  CONSTRAINT `fk_Alimentacao_Equino1`
    FOREIGN KEY (`id_equino`)
    REFERENCES `haras_db`.`Equino` (`id_equino`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Alimentacao_Alimento1`
    FOREIGN KEY (`id_item_estoque`)
    REFERENCES `haras_db`.`Alimento` (`id_item_estoque`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
