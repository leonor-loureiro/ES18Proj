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

### **Work Division :**

| Issue |                      Description                     | Dependencies | Group | Assignee |
|:-----:|:----------------------------------------------------:|:------------:|:-----:|:--------:|
|  #192 |     Fix Load test 100 reads - fix initializations    |              |   2   | Carrasco |
|  #193 |  Fix Load test 100 reads - add tax and car instances |     #192     |   2   |  António |
|  #194 |     Fix Load test 30 writes - fix initializations    |              |   3   |  Miguel  |
|  #195 |  Fix Load test 30 writes - add tax and car instances |     #194     |   3   |  Andrade |
|  #196 |    Fix Load test 100 writes - fix initializations    |              |   1   |  Leonor  |
|  #197 | Fix Load test 100 writes - add tax and car instances |     #196     |   1   |   André  |
|  #198 |     Create Test - Success Sequence without hotel     |              |   3   |  Miguel  |
|  #199 |      Create Test - Success Sequence without car      |              |   3   |  Miguel  |
|  #200 | Create Test - Success Sequence without hotel and car |              |   3   |  Andrade |
|  #201 |    Create Test - Unsuccess Sequence fail activity    |              |   2   |  António |
|  #202 |      Create Test - Unsuccess Sequence fail hotel     |              |   2   | Carrasco |
|  #203 |       Create Test - Unsuccess Sequence fail car      |              |   2   | Carrasco |
|  #204 |     Create Test - Unsuccess Sequence fail payment    |              |   1   |  Leonor  |
|  #205 |       Create Test - Unsuccess Sequence fail tax      |              |   1   |   André  |
|  #206 |                     Create Report                    |      All     |   1   |          |
|  #207 |        Fix Load Test Success Sequence Asserts        |              |   1   |   André  |

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

