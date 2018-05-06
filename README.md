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



## Work division:

| Module   | Issue | Description                                                                        | Dependencies | Group | Name     |
|----------|-------|------------------------------------------------------------------------------------|--------------|-------|----------|
| Activity | #145   | Suppliers' creation must include suppliers' nif and iban                           |              | 2     | Miguel   |
| Activity | #146   | Suppliers' listing must include suppliers' nif and iban                            | #145          | 2     | Miguel   |
| Activity | #147   | Offer creation UI must iclude cost                                                 |              | 2     | Miguel   |
| Activity | #148   | add Reservation UI - creating a reservation based on a offer                       | #147          | 2     | Miguel   |
| Activity | #149   | add to reservation UI listing of all reservations associated to an offer.          | #148          | 2     | Miguel   |
| Bank     | #150   | Find and implement needed adjustments and adaptations.                             |              | 1     | André    |
| Broker   | #151   | Broker's creation must include nif and iban                                        |              | 1     | Leonor   |
| Broker   | #152   | Broker's listings must include nif and iban                                        | #151          | 1     | Leonor   |
| Broker   | #153   | add Create Client UI in broker's context                                           | #151          | 1     | Leonor   |
| Broker   | #154   | Adventure Creation UI must occur in client's context                               | #153          | 1     | Leonor   |
| Broker   | #155   | Bulk Reservation UI must include nif and iban                                      | #151          | 1     | Leonor   |
| Broker   | #156   | Bulk Reservation listings must include nif and iban                                | #155          | 1     | André    |
| Car      | #157   | create and setup file structure for Car Module                                     |              | 1     | Andrade  |
| Car      | #158   | add RentACar UI - creates rentACars                                                | #157          | 1     | Andrade  |
| Car      | #159   | add to rentACar UI rentACars' listing                                              | #158          | 1     | Andrade  |
| Car      | #160   | add Vehicle UI - create car and mortocycle                                         | #158          | 1     | Andrade  |
| Car      | #161   | add to Vehicle UI car's listing and motorcycle's listing                           | #160          | 1     | Andrade  |
| Car      | #162   | add Renting UI - create renting for each vehicle                                   | #161          | 1     | André    |
| Car      | #163   | add rentings' listing for each Vehicle                                             | #162          | 1     | André    |
| Car      | #164   | add checkout for each car on Renting UI                                            | #162          | 1     | André    |
| Hotel    | #165   | Hotel Creation UI must include nif, iban and prices                                |              | 2     | António  |
| Hotel    | #166   | Hotels' listings must include nif, iban and prices                                 | #165          | 2     | António  |
| Hotel    | #167   | Room Reservation UI must include nif and iban                                      |              | 2     | António  |
| Hotel    | #168   | Room Reservation's listings must include nif and iban (not mentioned in enunciado) | #167          | 2     | António  |
| Tax      | #169   | create and setup file structure for Tax module                                     |              | 2     | Carrasco |
| Tax      | #170   | add TaxPayer UI - creates Taxpayers                                                | #169          | 2     | Carrasco |
| Tax      | #171   | add to TaxPayer UI taxpayers' listing                                              | #170          | 2     | Carrasco |
| Tax      | #172   | add itemType UI - creates itemTypes                                                | #170          | 2     | Carrasco |
| Tax      | #173   | add to itemTypeUI itemTypes' listing                                               | #172          | 2     | Carrasco |
| Tax      | #174   | add Invoice UI - creates Invoices                                                  | #170          | 2     | Carrasco |
| Tax      | #175   | add to invoice UI invoices' listing for each Taxpayer                              | #174          | 2     | Carrasco |
| Activity | #177   | implement rest invocations on remote interfaces                                    | #150, #175     | 2     | Miguel   |
| Broker   | #178   | implement rest invocations on remote interfaces                                    | #150, #175     | 1     | Leonor   |
| Car      | #179   | implement rest invocations on remote interfaces                                    | #150, #175     | 1     | André    |
| Hotel    | #180   | implement rest invocations on remote interfaces                                    | #150, #175     | 2     | António  |
