# loan-service

Use "docker-compose up -d" to setup database.



API Curls :
curl --location 'http://localhost:8080/loans/add' \
--header 'Content-Type: application/json' \
--data '{
    "customerId": 3,
    "lenderId": 2,
    "amount": 3000.00,
    "remainingAmount": 1000.00,
    "interest": 10,
    "penalty": 1,
    "paymentDate": "2022-11-06T23:00:00.000Z",
    "dueDate": "2028-10-06T23:00:00.000Z"
}'

curl --location 'http://localhost:8080/loans' \
--header 'Content-Type: application/json'

curl --location 'http://localhost:8080/loans/lender/1' \
--header 'Content-Type: application/json'

curl --location 'http://localhost:8080/loans/aggregate/customer/2' \
--header 'Content-Type: application/json'

curl --location 'http://localhost:8080/loans/aggregate/lender/1' \
--header 'Content-Type: application/json'
