# Vault
All logic is in the File `VelocityLimitService.java`

In finance, it's common for accounts to have so-called "velocity limits". In this task, you'll create a Java Spring boot application that accepts or declines attempts to load funds into customers' accounts in real-time.

Each attempt to load funds will come as a single-line JSON payload, structured as follows:

{
  "id": "1234",
  "customer_id": "1234",
  "load_amount": "$123.45",
  "time": "2018-01-01T00:00:00Z"
}
Each customer is subject to three limits:

A maximum of $5,000 can be loaded per day
A maximum of $20,000 can be loaded per week
A maximum of 3 loads can be performed per day, regardless of amount
As such, a user attempting to load $3,000 twice in one day would be declined on the second attempt, as would a user attempting to load $400 four times in a day.
