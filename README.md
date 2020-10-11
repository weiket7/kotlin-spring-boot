## Environment needed to run:
1. PostgreSQL 13  
Available from https://www.postgresql.org/download/  
Assuming default port 5432, create a database "ecommerce" with password as "password"  
If port, database name and/or password are different, please update in application.yml  
2. Java 11  
One option is to install AdoptOpenJDK 11 with HotSpot JVM from https://adoptopenjdk.net/
3. IntelliJ Community/Ultimate IDE 

##Tech stack
1. Spring Boot 2.3.4, Maven project, Kotlin language, Jar packaging, Java 11  
Spring boot dependencies: Spring Web, Validation, JDBC API, PostgreSQL driver, Flyway migration  
External dependencies: kotlin-csv from https://github.com/doyaaaaaken/kotlin-csv
2. PostgreSQL 13
3. ecommerce.html using Bootstrap, Axios and Vue

## To run application:
1. In IntelliJ, open pom.xml as project
2. Open Maven window and click "Reload All Maven Projects on top left" to restore dependencies
3. At top right, select EcommerceApplicationKt and press F5  
Migrations will run and create the tables
Application will run on default port 8080
4. Double-click ecommerce.html to open
5. Upload data.csv, it took 13-14 seconds in my desktop
6. To do another upload, please open pgAdmin and run the following:
delete from Invoice;  
delete from InvoiceStock;  
delete from Country;  

## To run unit tests:
1. In IntelliJ, right-click src/test/kotlin/com.ecommerce.invoice and select Run 'Tests in 'com.ecommerce.invoice''

##Observations about data.csv
1. InvoiceNo can be numeric or number prefixed with a letter, there are 25900 unique InvoiceNo
2. StockCode can be numeric, number suffixed with letter or other values like gift_0001_30, DCGSSBOY, there are 4070 unique StockCode and 1324 StockCode with more than one unique Description
3. Description can be valid values, empty or values like damaged, ?, missing
4. Quantity can be positive or negative numbers
5. InvoiceDate range from 1 Dec 2010 to 9 Dec 2011 (also written in Kaggle after log-in), possible formats include 12-01-10 8:28 and 10/17/2011 12:33
6. CustomerId can be numeric or empty
7. Country has 38 unique values, 5 of them (RSA, EIRE, Unspecified, European Community, Channel Islands) are not valid countries and United States of America is represented as USA
8. One InvoiceNo has one CustomerId, one Country and 43 InvoiceNo have two InvoiceDate with the second InvoiceDate being one minute later
9. One StockCode can be valid Description value, empty or values like damaged, ?, missing

##Database structure
Given above observations about data.csv, the tables are:
1. Country table having Id, Name
2. Invoice table having Id, InvoiceNo, InvoiceDate, CustomerId, CountryId  
There is index on (InvoiceNo, CountryId)
3. InvoiceStock table having Id, InvoiceNo, StockCode, Description, Quantity, UnitPrice  
There is index on (InvoiceNo)

###Technical details
1. Flyway is used for migration 
2. kotlin-csv is used to parse CSV instead of not using a library and trying to meet RFC 4180 standard which is tedious  
It can read data.csv containing 541910 rows within 2-3 seconds (or more depending on system)
3. Invoice and InvoiceStock in entities are domain objects and can contain business logic while InvoiceDto and InvoiceStockDto in dtos are database objects.  
Although they are similar and it's possible to merge them, the differentiation is there to not have database structure leak into domain and response structure.
4. Dependency injection is used and concrete class to resolve is annotated using @Primary
5. Application writes log into external file spring.log
6. Custom exceptions inherit ApiException and will be caught by ApiExceptionHandler which will return ApiExceptionResponse

##Solution details
1. ecommerce.html demonstrates how frontend can send a unique id (Date.now()) during upload and get the progress by calling /api/invoice/uploadProgress?id=id
2. For search and list, the response structure is an array of Invoice and each Invoice contains one or more InvoiceStock

##In a real application and/or given more time I would:
1. Add API authentication
2. Ask PO/team whether need to validate file mime type
3. Ask PO for more details about search, eg whether search by date, search by customer etc
4. Ask team for feedback about implementation, best practices etc
5. Learn and use an ORM
6. Validate request in Controller, perhaps by using @Valid @NotBlank annotations
7. Enhance solution to use background thread