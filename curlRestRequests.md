#### Get all meals  
'curl http://localhost:8080/topjava/rest/meals'

#### Get meal by id  
'curl http://localhost:8080/topjava/rest/meals/100002'

#### Delete meal by id  
'curl -X DELETE http://localhost:8080/topjava/rest/meals/100002'

#### Create new meal  
'curl -H "Content-Type: application/json" -X POST -d '{"dateTime":"2020-06-02T18:00:00","description":"Созданный ужин","calories":300}' http://localhost:8080/topjava/rest/meals'

#### Get meals filtered by date and time  
'curl "http://localhost:8080/topjava/rest/meals/filters?startDate=2020-01-30&endDate=2020-01-31&startTime=00:00&endTime=13:01"'