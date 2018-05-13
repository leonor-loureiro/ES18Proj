# Adventure Builder [![Build Status](https://travis-ci.com/tecnico-softeng/es18tg_06-project.svg?token=TzeQEgtqUtSfbxXNgbUu&branch=develop)](https://travis-ci.com/tecnico-softeng/prototype-2018) [![codecov](https://codecov.io/gh/tecnico-softeng/prototype-2018/branch/master/graph/badge.svg?token=OPjXGqoNEm)](https://codecov.io/gh/tecnico-softeng/prototype-2018)

To run tests execute: mvn clean install

To see the coverage reports, go to <module name>/target/site/jacoco/index.html.


|   Number   |          Name           |            Email                     |   GitHub Username  | Group |
| ---------- | ----------------------- | ------------------------------------ | ------------------ | ----- |
|   84736    | Leonor Loureiro         | leonor.filipa@tecnico.ulisboa.pt     | leonor-loureiro    |   1   |
|   84698    | André Fonseca           | andre.filipe.alves.fonseca@ist.utl.pt| Anfifo             |   1   |
|   84702    | Antonio Terra           | antonio.terra@tecnico.ulisboa.pt     | wimpytoaster       |   2   |
|   84774    | Tomás Carrasco          | tomasbccarrasco@gmail.com            | CarrascoII         |   2   |
|   84709    | Diogo Andrade           | diogo.freire.dandrade@gmail.com      | dfAndrade          |   3   |
|   84747    | Miguel Viegas           | miguel.s.viegas@tecnico.ulisboa.pt   | JellyWellyBelly    |   3   |

- **Group 1:**
- **Group 2:**

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

And create the 6 databases for the project as specified in
the `resources/fenix-framework.properties`.

To launch a server execute in the module's top directory: mvn clean spring-boot:run

To launch all servers execute in bin directory: startservers

To stop all servers execute: bin/shutdownservers

To run jmeter (nogui) execute in project's top directory: mvn -Pjmeter verify. Results are in target/jmeter/results/, open the .jtl file in jmeter, by associating the appropriate listeners to WorkBench and opening the results file in listener context

