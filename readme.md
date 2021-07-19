# Simple banking app

This application provides REST api methods to control accounts and make transfer between them.
The service utilize PostgreSQL database as the main datasource.

The application allows creating accounts and make transfers between them. It also can return the account data with 
all the related transactions or just an account transaction list in desc order.

## Build and run

You can launch the app locally or using docker.
### Docker

First you need to build docker image. Use gradle command `dockerBuildImage` to build and publish image in your 
local image registry.
```bash
./gradlew dockerBuildImage 
```
After successful image creation you can use `docker-compose` command to launch
both PostgreSQL and application instances.
```shell
docker-compose up
```

### Local environment
For local environment you need to use profile `local`
```shell
./gradlew bootRun --args='--spring.profiles.active=local'
```
However, for local environment you need PostgreSQL accessible from your machine.
You can also customize credentials in `application-local.yaml` configuration file.

## Rest endpoints

The REST application is running on port 8080 by default. The service exposes 4 API methods to control account 
and transfer data.

1. Get account by *id*

Returns account data with transfers list sorted by transfer date in descending order.
```shell
GET /account/{id}
```
Response example
```json
{
    "id": 52,
    "balance": 31.00,
    "transfers": [
        {
            "id": 70,
            "amount": 1.00,
            "transferDate": "2021-07-20T00:54:41.393979+03:00",
            "from": 52,
            "to": 53
        }
    ]
}
```

2. Create Account

Creates a new account with initial balance.
```shell
POST /account
```
Body example
```json
{
  "balance" : 50
}
```
Response example
```json
{
    "id": 53,
    "balance": 50,
    "transfers": []
}
```

3. Get account transfers by account *id*

Returns list of transfers related to an account.
```shell
GET /account/{id}/transfers
```
Response example
```json
[
    {
        "id": 70,
        "amount": 60.10,
        "transferDate": "2021-07-20T00:54:41.393979+03:00",
        "from": 52,
        "to": 53
    }
]
```

4. Make transfer

Creates a transfer record and updates related accounts data
```shell
POST /transfer
```
Body example
```json
{
    "amount" : 1.23,
    "from" : 50,
    "to" : 51
}
```
Response example
```json
{
    "id": 50,
    "amount": 10.50,
    "transferDate": "2021-07-20T01:28:25.737262+03:00",
    "from": 50,
    "to": 51
}
```

## Tests

The application contains a few test scenarios. I created just end-to-end tests to cover main features.
To launch unit tests please use 
```shell
./gradlew test
```

## Data model

1. Account

| Field         | Type          | DB Type       | Description                   | 
|:--------------|:-------------:|:-------------:|:------------------------------|
| id            | Long          | Bigint        | Unique account identifier     |
| balance       | BigDecimal    | Decimal       | Actual account balance        |
| transfers     | List<Transfer>| -             | List of all the incoming and outgoing transactions related to the account |

2. Transfer

| Field         | Type          | DB Type       | Description                   |
|:--------------|:-------------:|:-------------:|:------------------------------|
| id            | Long          | Bigint        | Unique transaction identifier |
| amount        | BigDecimal    | Decimal       | The transaction money amount  |
| transferDate  | ZonedDateTime | Timestamp with time zone | Date and time when the transfer was registered |
| from          | Long          | Bigint        | Transfer source account id    | 
| to            | Long          | Bigint        | Transfer destination account id |
