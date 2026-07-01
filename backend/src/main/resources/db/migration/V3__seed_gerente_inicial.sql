-- Seed do gerente inicial (papel de topo / admin do haras).
-- Necessário para o bootstrap: é a partir dele que se cadastram os demais usuários.
-- Senha em hash BCrypt. Credenciais iniciais:
--   email: gerente@haras.local
--   senha: admin123   (trocar após o primeiro acesso)
USE `haras_db`;

INSERT INTO `haras_db`.`Pessoa` (`nome`, `data_nascimento`, `cpf`, `is_gerente`, `email`, `senha`)
SELECT 'Gerente Inicial', '1990-01-01', '00000000000', 1, 'gerente@haras.local',
       '$2a$10$Lvivr.6yo36fIMNH83bWX.xHtBejToii3.BNgpYjCwdLyTmHZ4Hp.'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM `haras_db`.`Pessoa` WHERE `email` = 'gerente@haras.local'
);
