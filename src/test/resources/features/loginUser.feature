Feature: Login Endabank

  As an Endabank user
  I want to access to the Endabank platform
  To use its services

  @Login
  Scenario Outline: Login to the Endabank platform
    Given the user is on the Login page
      | Actor | <Actor> |
      | URL   | <URL>   |
      | Path  | <Path>  |
    When the user logs in using its credentials
      | user     | <user>     |
      | password | <password> |
    Then the user should be allowed to access to the application
      | field | <field> |

    Examples:
      | Actor    | URL                              | Path   | user           | password  | field      |
      | endabank | http://35.202.242.69:3000/api/v1 | /login | test@gmail.com | Alexis17* | isApproved |
