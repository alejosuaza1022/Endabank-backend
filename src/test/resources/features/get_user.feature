Feature: Consultar informacion de usuario
    Yo como automatizador
    Necesito consultar la informacion de un usuario
    Para aprender automatizacion de API's

    @serviceGet
    Scenario Outline: Consultar usuario por ID
        Given Establezco la base <url>
        When Deseo consultar la informacion del <usuario> por <id>
        Then Valido que el nombre sea <nombre>

        Examples:
            | url                   | usuario           | id   | nombre              |
            | https://gorest.co.in/ | public-api/users/ | 2381 | "Leela Namboothiri" |