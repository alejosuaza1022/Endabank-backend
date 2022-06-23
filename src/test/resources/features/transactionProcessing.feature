Feature: Transaction processing

  As an Endabank user
  I want to send certain amount of money to someone else

  Background:
    Given the user is using the endpoint
    And logs in using the credentials

  @firstScenario
  Scenario Outline: The user should not be allowed to send money to himself
    Given the user has written the POST request using the endpoint to send money
    When the user types the information into the body request
      | amount      | <amount>                    |
      | bANIssuer   | <bankAccountNumberIssuer>   |
      | bANReceiver | <bankAccountNumberReceiver> |
      | description | <description>               |
      | address     | <address>                   |

    Then the user should see the next body response
      | statusCode | <statusCode>        |
      | name       | <ResponseJSONName>  |
      | value      | <ResponseJSONValue> |
    Examples:
      | amount  | bankAccountNumberIssuer | bankAccountNumberReceiver | description  | address       | statusCode | ResponseJSONName          | ResponseJSONValue                                           |
      | 500     | 2022053111472379        | 2022053112010811          | test payment | 181.57.222.58 | 201        | stateDescription          | The transaction was successfully completed                  |
      | -500    | 2022053111472379        | 2022053112010811          | test payment | 181.57.222.58 | 422        | message                   | The amount to be transferred has to be greater than 0.      |
      | 5000000 | 2022053111472379        | 2022053112010811          | test payment | 181.57.222.58 | 201        | stateDescription          | The user has not balance enough to make the transaction.    |
      | null    | 2022053111472379        | 2022053112010811          | test payment | 181.57.222.58 | 400        | amount                    | must not be null                                            |
      | jaime   | 2022053111472379        | 2022053112010811          | test payment | 181.57.222.58 | 500        | message                   |                                                             |
      | 500     | 202205311147237         | 2022053112010811          | test payment | 181.57.222.58 | 404        | message                   | The bank account provided does not exist.                   |
      | 500     | 2022053112010811        | 2022053111472379          | test payment | 181.57.222.58 | 422        | message                   | The user not owns the bank account used.                    |
      | 500     | null                    | 2022053112010811          | test payment | 181.57.222.58 | 400        | bankAccountNumberIssuer   | must not be null                                            |
      | 500     | federico                | 2022053112010811          | test payment | 181.57.222.58 | 500        | message                   |                                                             |
      | 500     | 2022053111472379        | 202253112010811           | test payment | 181.57.222.58 | 404        | message                   | The bank account provided does not exist.                   |
      | 500     | 2022053111472379        | 2022053111472379          | test payment | 181.57.222.58 | 422        | message                   | The account to be transferred is the same as the one used." |
      | 500     | 2022053111472379        | null                      | test payment | 181.57.222.58 | 400        | bankAccountNumberReceiver | must not be null                                            |
      | 500     | 2022053111472379        | alejandro                 | test payment | 181.57.222.58 | 500        | message                   |                                                             |
      | 500     | 2022053111472379        | 2022053112010811          | null         | 181.57.222.58 | 201        | stateDescription          | The transaction was successfully completed                  |
      | 500     | 2022053111472379        | 2022053112010811          | test payment | null          | 400        | address                   | size must be between 6 and 15                               |
      | 500     | 2022053111472379        | 2022053112010811          | test payment | hello world   | 201        | stateDescription          | The transaction was successfully completed                  |
      | 500     | 2022053111472379        | 2022053112010811          | test payment | 123.456.789   | 201        | stateDescription          | The transaction was successfully completed                  |
