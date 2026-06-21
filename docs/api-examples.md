# API Examples

Full demo flow using `localhost:8080`.

## 1. Create Merchant

```bash
curl -X POST http://localhost:8080/api/merchants \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "Northwind Trading",
    "externalReference": "MER-2026-001"
  }'
```

## 2. Create Customer

```bash
curl -X POST http://localhost:8080/api/customers \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "Contoso Retail GmbH",
    "email": "accounts-payable@contoso-retail.example"
  }'
```

## 3. Create Invoice

```bash
curl -X POST http://localhost:8080/api/invoices \
  -H 'Content-Type: application/json' \
  -d '{
    "merchantId": 1,
    "customerId": 1,
    "invoiceNumber": "INV-2026-0001",
    "amount": 2480.75,
    "currency": "EUR",
    "issueDate": "2026-06-21",
    "dueDate": "2026-07-21"
  }'
```

## 4. Create Settlement Case

```bash
curl -X POST http://localhost:8080/api/settlements \
  -H 'Content-Type: application/json' \
  -d '{
    "invoiceId": 1
  }'
```

## 5. Approve Settlement

```bash
curl -X PATCH http://localhost:8080/api/settlements/1/status \
  -H 'Content-Type: application/json' \
  -d '{
    "status": "APPROVED"
  }'
```

## 6. Simulate Payment Pending

```bash
curl -X PATCH http://localhost:8080/api/settlements/1/status \
  -H 'Content-Type: application/json' \
  -d '{
    "status": "PAYMENT_PENDING"
  }'
```

## 7. Mark Settlement As Paid

```bash
curl -X POST http://localhost:8080/api/payments/simulations \
  -H 'Content-Type: application/json' \
  -d '{
    "settlementCaseId": 1,
    "successful": true
  }'
```

## 8. Retrieve Settlement History

```bash
curl http://localhost:8080/api/settlements/1/history
```

## 9. Retrieve Audit Logs

```bash
curl http://localhost:8080/api/audit-logs
```
