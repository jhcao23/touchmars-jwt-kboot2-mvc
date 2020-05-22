# Spring Boot 2 Template (Partially done esp. kotlin part) 
1. The project uses `Spring Security 5`ß (MVC only, webflux version will roll into next edition)
    and JWT based on `Filter`, as a standard methodology, to handle the authentication.
2. The project now is written in Java but should use kotlin - kotlin version will replace Java later.
3. It has some dependencies such as `wechat-client-mini-kboot2` to handle special/variant 3rd party OAuth2 authentications. 
4. The project has removed dependency on old `spring-social` project.
5. TODO: versions updates.

## PreCondition: DB Setup!

## Create DB
run [`mysql/schema.sql`](mysql/schema.sql)

## Create User in DB
```
DROP USER 'spring'@'localhost';
DROP USER 'spring'@'%';

CREATE USER 'spring'@'localhost' IDENTIFIED BY 'spring';
CREATE USER 'spring'@'%' IDENTIFIED BY 'spring';

GRANT ALL ON touchmars_spring_template.* TO 'spring'@'localhost';
GRANT ALL ON touchmars_spring_template.* TO 'spring'@'%';

FLUSH PRIVILEGES;
commit;
```

## How to build the project?

`mvn clean install` or 
`mvn clean install deploy -DperformRelease=true -Parchetype && update-github`

`update-github` is self-defined command.

## How to run the project?
1. This is a framework and not an application.
2. If you have to run it as a demo, release/enable the files [`Application.java`](src/main/java/technology/touchmars/template/Application.java.bak) and [`application.yml`](src/main/resources/application.yml.bak),
    then run `mvn spring-boot:run`.

## How to use the framework?

1. Please disable the [`Application.java`](src/main/java/technology/touchmars/template/Application.java) and [`application.yml`](src/main/resources/application.yml) files - they are actually not supposed to be enabled at all.
   - Simply add `.bak` to the file names.
2. Use this framework as a dependency.