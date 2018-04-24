# Adventure Builder [![Build Status](https://travis-ci.com/tecnico-softeng/es18tg_06-project.svg?token=TzeQEgtqUtSfbxXNgbUu&branch=develop)](https://travis-ci.com/tecnico-softeng/es18tg_06-project) [![codecov](https://codecov.io/gh/tecnico-softeng/es18tg_06-project/branch/master/graph/badge.svg?token=14SYbuaeEv)](https://codecov.io/gh/tecnico-softeng/es18tg_06-project)

To run tests execute: mvn clean install

To see the coverage reports, go to <module name>/target/site/jacoco/index.html.


|   Number   |          Name           |            Email        |   GitHub Username  | Group |
| ---------- | ----------------------- | ----------------------- | -------------------| ----- |
|            |                         |                         |                    |   1   |
|            |                         |                         |                    |   1   |
|            |                         |                         |                    |   1   |
|            |                         |                         |                    |   2   |
|            |                         |                         |                    |   2   |
|            |                         |                         |                    |   2   |

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

And create the 7 databases for the project as specified in
the `resources/fenix-framework.properties`.
