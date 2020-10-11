create table if not exists Invoice (
  Id serial primary key,
  InvoiceNo varchar(10) not null,
  InvoiceDate timestamp not null,
  CustomerId int null,
  CountryId int not null
);

CREATE INDEX IX_Invoice_InvoiceNo ON Invoice (InvoiceNo, CountryId);

create table if not exists InvoiceStock (
  Id serial primary key,
  InvoiceNo varchar(10) not null,
  StockCode varchar(15) not null,
  Description varchar(100) null,
  Quantity int not null,
  UnitPrice decimal(9,2) not null
);

CREATE INDEX IX_InvoiceStock_InvoiceNo ON InvoiceStock (InvoiceNo);

create table if not exists Country (
  Id serial primary key,
  Name varchar(50) not null
);
