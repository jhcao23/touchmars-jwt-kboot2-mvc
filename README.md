`mvn clean install deploy -DperformRelease=true -Parchetype && update-github`

## Create DB
`CREATE DATABASE spring_social_test;`

## Create User in DB
`DROP USER 'spring'@'localhost';`

`DROP USER 'spring'@'%';`

`CREATE USER 'spring'@'localhost' IDENTIFIED BY 'spring';`

`GRANT ALL ON spring_social_test.* TO 'spring'@'localhost';`

`CREATE USER 'spring'@'%' IDENTIFIED BY 'spring';`

`GRANT ALL ON spring_social_test.* TO 'spring'@'%';`

`FLUSH PRIVILEGES;`

`commit;`
