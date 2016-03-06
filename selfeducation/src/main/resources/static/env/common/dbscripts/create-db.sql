CREATE TABLE IF NOT EXISTS users (
  username VARCHAR(45) NOT NULL ,
  password VARCHAR(45) NOT NULL ,
  enabled TINYINT NOT NULL DEFAULT 1 ,
  PRIMARY KEY (username));

CREATE TABLE IF NOT EXISTS authorities (
  user_authority_id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(45) NOT NULL,
  authority varchar(45) NOT NULL,
  PRIMARY KEY (user_authority_id),
  UNIQUE  (authority,username),
  CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES users (username));
