Feature: Transaction processing

  As an Endabank user
  I want to send certain amount of money to someone else

  Background:
    Given the user is using the endpoint
    And logs in using the credentials

  @firstScenario
  Scenario: The user should not be allowed to send money to himself
    Given the user has written the POST request using the endpoint to send money
    When the user types the necessary information into the body request
    Then the user should see the next body response
