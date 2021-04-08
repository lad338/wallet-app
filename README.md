# Wallet App

## Overview

This wallet app backend is written in Java with Spring Boot.
The backend is shipped with Docker.
To test the backend, a postman collection is provided to test the API.

## Start up

As the initial build and start up may take some time, it is suggested to start up the server while continue reading the rest.
The prerequisite of the provided scripts require docker and docker-compose installed and ports 8080 and 27017 available.
For the initial build and start up, please use `sh initial_run.sh`
The script will start up a mongoDB and the Spring Boot server.
As we will need to use transaction in the server, master and salves of mongoDB is required.
However, in local environment, a single mongo instance is created, and the script is used to mimic the aforementioned situation with one instance only.
The `initial_run.sh` is only required for the first run. Future startups after shall only require `docker-compose up`.
The server successfully starts up with the server log of `Started WalletApplication in x.xxx seconds`
`Ctrl + C` shall stop the backend.
As for any coding updates, use `docker-compose build` to rebuild the docker images.

## Design and planning

The design is based on simplicity but also expressing all requirements needed.
The backend supports 3 currencies `HKD`, `USD` and `EUR`.
The database consists of 2 collections - `user` and `transaction`.
Under `user`, it consists of the login credentials and the wallet itself. The wallet is presented as 3 fields `hkd`, `usd` and `eur` as the 3 supported currency in the server.
There shall be `CREATE`, `READ` and `UPDATE` actions for the `user` collection.
Under `transaction`, it includes all the transactions by all users as the log and history.
There shall only be `CREATE` and `READ` actions for the `transaction` collection.
It uses JWT as user authentication.

The required actions a user can do are presented by an API for each action.
A postman collection is provided for reference and checking.

For exchange rates among currencies, it is currently hard coded in the program with a random number to present different exchange rate at different moment. (Please refer to `ExchangeRateRepository.java`)

## Backend explanation and highlights

The core functions of the backend shall be found under the service package `./src/main/java/com/example/walletapp/service`
The 4 implementations highlight the 4 major components of the backend `Registration`, `Login`, `Transaction` and `User`
`Registration` and `Login` are self-explanatory.
`Transaction` includes retrieving transaction history and executing a transaction which also writes a transaction record.
`User` includes finding a user and updating a user wallet with transactions.

To handle transactions, it uses optimistic lock with the @Version annotation supported by Spring.
An optimistic lock exception will be thrown if version of the account record changes during process.
Any exception thrown within a @Transactional annotated block shall result in a rollback.

To deal with changing exchange rates, it shall snap-shot an exchange rate upon receiving an `EXCHANGE` transaction request. (Please refer to `TransactionServiceImpl.java`)
All calculations are using the `BigDecimal` class.

On the other hand, the controllers includes the APIs provided by the backend.
The APIs are designed in RESTful.
The APIs request and response shall be further discussed the postman section side-by-side.

Using Java as a multi-paradigm programming language, the coding borrows concepts and characteristics from different paradigms.
In terms of Object-Oriented Programming, the models are designed with encapsulation (Please refer to `src/main/java/com/example/walletapp/model`)
Getter and Setter functions are created with the lombok annotation @Data.
In terms of Functional Programming, the backend uses Immutability (constants are declared with `final` ), higher-order functions (JAVA 8 APIs such as `map`, etc) and recursive functions (e.g. `UserServiceImpl::updateAccount`).

## Testing with postman

Please find `WalletApp.postman_collection.json` to import the postman collection.
To use the testing script for `jwt` in the postman collection, please kindly create a new environment. 
The testing script shall apply the jwt from login responses to the environment.
Examples are also provided for the APIs.

### Health Check
`GET localhost:8080/actuator/health`
The health check API only returns a simple response of `{"status":"UP"}` to indicating that the API server is available and accepting API calls.
It uses Spring Actuator.

### Register User
`POST localhost:8080/users`
The register user API creates a user record. 
For the request body, `username` and `password` are required.
Optional request parameter of `hkd`, `usd` and `eur` are supported for initial deposit transactions.
Register is also consider as logged in.
The response shall return a jwt for authentication on other user action based APIs.

FYI
The passwords are stored in the DB with the encryption of Spring Security `BCryptPasswordEncoder`.
The username field is marked unique in DB level and indexed.


You may create a user for the following testing.

### Login User
`POST localhost:8080/login`
For the request body, `username` and `password` are required.
The response shall return a jwt for authentication.
You may also use this as a refresh token action.

FYI, the jwt expires in 30 minutes.

For the following requests, jwt is required for authentication.

### My Wallet
`GET localhost:8080/users/me`
The response shall include `hkd`, `usd` and `eur` as the 3 amounts for the 3 currencies.
If no initial deposit is included when creating the user, they should show `0`.

You may come back to this API after calling transaction APIs to verify the wallet amount.

### My Transaction History
`GET localhost:8080/users/me/transactions`
The response shall include a list of transaction records.
If there is an initial deposit when creating the user, there should be `DESPOSIT` records.

You may come back to this API after calling transaction APIs to verify the transaction history.

FYI 
Transaction history includes all `DEPOSIT`, `WITHDRAW`, `EXCHANGE`, `TRANSFER` and transfers from other users' `TRANSFER` which us marked as `RECEIVE`.
In DB level, `userId` and `targerId` are indexed.

### Transaction
`POST localhost:8080/users/me/transactions`
The transaction APIs share the same method and url but with a different value of `action` in the request body.
The supported actions are `DEPOSIT`, `WITHDRAW`, `EXCHANGE` and `TRANSFER`
The supported currencies are `HKD`, `USD` and `EUR`
`amount`, `currency` and `action` are required for all actions.
`targetCurrency` is required for `EXCHANGE`.
`targetUsername` is required for `TRANSFER`.

For `EXCHANGE`, the `amount` to be exchanged from is from the `currency`, i.e. shall deduct `currency` account and add `targetCurrency` account
For `TRANSFER`, it is assumed that the same currency is used. The user shall first exchange then transfer if applicable.

You may go back to register user to create another user to test `TRANSFER`

The response shall include a transaction record ID.

## Current limitations and potential improvements

The current backend is bounded by 3 currencies only, and the currency field for account is hardcoded.
This design is due to simplifying the multi-currency model, yet still displaying the ability to exchange money in different currencies.
To support potential different amounts of currencies, a better solution is to include another collection, for instance `account` for each currency where `userId` and `currency` shall be a unique pair across the collection.
As a result, it shall reduce hardcoded fields, and the services shall easily integrate with systems with another currency schema.

The API are currently used as a "testing" tool; therefore, an assumption is that the user shall input valid inputs in the request bodies of the APIs.
e.g. different usernames for registering different accounts, transaction amount shall be a positive amount.
As a result, when invalid input are received, not all situations provide meaningful error messages.
In addition, server logging is also lacking.

To simplifying dealing with decimal places, it currently rounds to 4 d.p. which may not be desired for all systems.
A rounding method shall be included when applying this backend.

Auditing can be added. The wallet final amount can be verified by the sum of all related transaction records as a cross-check.

## Time Spent

|Process| Time |
|:----- | :------------------------------------------  |
| Design and planning | 15 mins |
| Setup Spring Boot project | 15 mins |
| Setup docker and prepare initializing scripts | 30 mins |
| Implementing modeling | 15 mins |
| Implementing register user | 30 mins |
| Implementing jwt and login | 30 mins |
| Implementing transactions | 1 hour 30 mins |
| Postman example | 30 mins |
| Project description and explanation | 1 hour 30 mins |

Yes, I spend more time than I expected to write this README. Hope the README helps.






