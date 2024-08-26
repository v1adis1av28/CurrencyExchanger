# Currency Exchange Project

## Overview

The **Currency Exchange Project** is a REST API for managing currencies and exchange rates. It allows users to view and edit lists of currencies and exchange rates and perform conversions of arbitrary amounts from one currency to another.

**Note:** This project is backend-only, with no web interface provided.

## Project Motivation

This project aims to:

- Introduce the MVC (Model-View-Controller) pattern.
- Familiarize with REST API design, proper resource naming, and HTTP response codes.
- Practice basic SQL syntax, including table creation.
- Understand the use of databases in web applications, specifically SQLite and JDBC.

## Technologies Used

- **Java**: Collections, OOP
- **MVC Pattern**
- **Maven**
- **Backend**: Java Servlets, REST API, JSON
- **Database**: SQLite, JDBC
- **Deployment**: Tomcat
- **HTTP**: GET and POST requests, response codes

## Database Structure

### Currencies Table

| Column   | Type    | Comment                                        |
|----------|---------|------------------------------------------------|
| ID       | int     | Currency ID, auto-increment, primary key       |
| Code     | varchar | Currency code                                  |
| FullName | varchar | Full name of the currency                      |
| Sign     | varchar | Currency symbol                                |

**Example entry for Australian dollar:**

| ID | Code | FullName           | Sign |
|----|------|--------------------|------|
| 1  | RUB  | RUSSIAN RUBLE  | ₽   |

**Indexes:**

- Primary key on `ID`
- Unique index on `Code` to ensure currency uniqueness and speed up searches

### ExchangeRates Table

| Column           | Type         | Comment                                                             |
|------------------|--------------|---------------------------------------------------------------------|
| ID               | int          | Exchange rate ID, auto-increment, primary key                       |
| BaseCurrencyId   | int          | ID of the base currency, foreign key to Currencies.ID               |
| TargetCurrencyId | int          | ID of the target currency, foreign key to Currencies.ID             |
| Rate             | Decimal(6)   | Exchange rate from base currency to target currency                 |

**Indexes:**

- Primary key on `ID`
- Unique index on `BaseCurrencyId` and `TargetCurrencyId` to ensure the uniqueness of the currency pair and speed up searches

## REST API

The REST API provides a CRUD interface over the database, allowing users to create (C), read (R), and update (U) data. Deletion (D) is omitted for simplicity.

### Currencies Endpoints

- **GET /currencies**: Retrieve the list of currencies.
  - **Example Request**:
    ```
    GET http://localhost:8080/currencies
    Accept: application/json
    ```
  - **Example Response**:
    ```json
    [
      {
        "ID": 840,
        "Code": "USD",
        "FullName": "US DOLLAR",
        "Sign": "$"
      },
      {
        "ID": 643,
        "Code": "RUB",
        "FullName": "RUSSIAN RUBLE",
        "Sign": "₽"
      },
      ...
    ]
    ```

- **GET /currency/{code}**: Retrieve a specific currency by its code.
  - **Example Request**:
    ```
    GET http://localhost:8080/currency/RUB
    ```
  - **Example Response**:
    ```json
    {
      "ID": 643,
      "Code": "RUB",
      "FullName": "RUSSIAN RUBLE",
      "Sign": "₽"
    }
    ```

- **POST /currencies**: Add a new currency to the database.
  - **Example Request**:
    ```
    POST http://localhost:8080/currencies
    Content-Type: application/x-www-form-urlencoded

    code=SSSGG&name=SSSSS&sign=S
    ```
  - **Example Response(in not unique case)**:
    ```json
    {
      "error": "Currencies not unique"
    }
    ```

### Exchange Rates Endpoints

- **GET /exchangeRates**: Retrieve the list of all exchange rates.
  - **Example Request**:
    ```
    GET http://localhost:8080/exchangeRates
    Accept: application/json
    ```
  - **Example Response**:
    ```json
    [
      {
        "ID": 1,
        "BaseCurrencyId": 840,
        "TargetCurrencyId": 978,
        "Rate": 0.909852,
        "BaseCurrencyCode": "USD",
        "TargetCurrencyCode": "EUR"
      },
      ...
    ]
    ```

- **GET /exchangeRate/{baseCurrencyCode}{targetCurrencyCode}**: Retrieve a specific exchange rate.
  - **Example Request**:
    ```
    GET http://localhost:8080/exchangeRate/USDEUR
    Accept: application/json
    ```
  - **Example Response**:
    ```json
    {
      "ID": 0,
      "BaseCurrencyId": 840,
      "TargetCurrencyId": 978,
      "Rate": 0.4523
    }
    ```

- **POST /exchangeRates**: Add a new exchange rate to the database.
  - **Example Request**:
    ```
    POST http://localhost:8080/exchangeRates
    Content-Type: application/x-www-form-urlencoded

    baseCurrencyCode=RUB&targetCurrencyCode=USD&rate=332
    ```
  - **Example Response**:
    ```json
    {
      "error": "409"
    }
    ```

- **PATCH /exchangeRate/{baseCurrencyCode}{targetCurrencyCode}**: Update an existing exchange rate.
  - **Example Request**:
    ```
    PATCH http://localhost:8080/exchangeRate/USDEUR
    Content-Type: application/x-www-form-urlencoded

    rate=0.87
    ```
  - **Example Response**:
    ```json
    {
      "ID": 1,
      "BaseCurrencyId": 840,
      "TargetCurrencyId": 978,
      "Rate": 0.87
    }
    ```

### Currency Exchange Endpoint

- **GET /exchange?from={BASE_CURRENCY_CODE}&to={TARGET_CURRENCY_CODE}&amount={AMOUNT}**: Convert an amount from one currency to another.
  - **Example Request**:
    ```
    GET http://localhost:8080/exchange?from=USD&to=EUR&amount=5
    Accept: application/json
    ```
  - **Example Response**:
    ```json
    {
      "BaseCurrencyId": 840,
      "TargetCurrencyId": 978,
      "ExchangeRate": 0.45,
      "ConvertedAmount": 2.25,
      "amount": 5
    }
    ```

## Error Handling

- **404 Not Found**: Returned when a requested resource or exchange rate is not found.
  - **Example Response**:
    ```json
    {
      "error": "404"
    }
    ```

- **409 Conflict**: Returned when trying to create a resource that already exists.
  - **Example Response**:
    ```json
    {
      "error": "409"
    }
    ```
