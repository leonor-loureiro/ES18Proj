# Adventure Builder [![Build Status](https://travis-ci.com/tecnico-softeng/es18tg_06-project.svg?token=TzeQEgtqUtSfbxXNgbUu&branch=develop)](https://travis-ci.com/tecnico-softeng/es18tg_06-project) [![codecov](https://codecov.io/gh/tecnico-softeng/es18tg_06-project/branch/develop/graph/badge.svg?token=14SYbuaeEv)](https://codecov.io/gh/tecnico-softeng/es18tg_06-project)

To run tests execute: mvn clean install

To see the coverage reports, go to <module name>/target/site/jacoco/index.html.


|   Number   |          Name           |            Email                     |   GitHub Username  | Group |
| ---------- | ----------------------- | ------------------------------------ | ------------------ | ----- |
|   84736    | Leonor Loureiro         | leonor.filipa@tecnico.ulisboa.pt     | leonor-loureiro    |   1   |
|   84709    | Diogo Andrade           | diogo.freire.dandrade@gmail.com      | dfAndrade          |   1   |
|   84698    | André Fonseca           | andre.filipe.alves.fonseca@ist.utl.pt| Anfifo             |   1   |
|   84747    | Miguel Viegas           | miguel.s.viegas@tecnico.ulisboa.pt   | JellyWellyBelly    |   2   |
|   84702    | Antonio Terra           | antonio.terra@tecnico.ulisboa.pt     | wimpytoaster       |   2   |
|   84774    | Tomás Carrasco          | tomasbccarrasco@gmail.com            | CarrascoII         |   2   |


### Infrastructure

This project includes the persistent layer, as offered by the FénixFramework.
This part of the project requires to create databases in mysql as defined in `resources/fenix-framework.properties` of each module.

See the lab about the FénixFramework for further details.

#### Docker (Alternative to installing Mysql in your machine)

To use a containerized version of mysql, follow these stesp:

```
docker-compose -f local.dev.yml up -d
docker exec -it mysql sh
```

Once logged into the container, enter the mysql interactive console

```
mysql --password
```

And create the 7 databases for the project as specified in
the `resources/fenix-framework.properties`.
