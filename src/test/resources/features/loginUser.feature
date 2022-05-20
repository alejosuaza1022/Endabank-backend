Feature: Login Endabank

  As an Endabank user
  I want to access to the Endabank platform
  To use its services

  @Login
  Scenario Outline: Login to the Endabank platform

    When the user logs in using its credentials
      | email    | <email>    |
      | password | <password> |
    Then the user should be allowed to access to the application
      | field | <field> |

    Examples:
      | email          | password  | field      |
      | test@gmail.com | Alexis17* | isApproved |
