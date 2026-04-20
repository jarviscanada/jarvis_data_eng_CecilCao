-- Show table schema 
\d+ retail;
-- RETAIL:  invoice_no, stock_code, description, quantity, invoice_date, unit_price, customer_id, country

-- Show first 10 rows
SELECT * FROM retail limit 10;

-- Check # of records
SELECT COUNT(invoice_no) AS total_records FROM retail;

-- number of clients (e.g. unique client ID)
SELECT COUNT(DISTINCT customer_id) FROM retail;

-- invoice date range (e.g. max/min dates)
SELECT MAX(invoice_date), MIN(invoice_date) FROM retail;

-- number of SKU/merchants (e.g. unique stock code)
SELECT COUNT(DISTINCT stock_code) FROM retail;

-- Calculate average invoice amount excluding invoices with a negative amount 
-- (e.g. canceled orders have negative amount)
SELECT AVG(total)
FROM (
	SELECT invoice_no, SUM(quantity * unit_price) AS total
	FROM retail
	GROUP BY invoice_no
	HAVING SUM(quantity * unit_price) > 0
) sq;

-- Calculate total revenue (e.g. sum of unit_price * quantity)
SELECT SUM(unit_price * quantity) FROM retail;

-- Calculate total revenue by YYYYMM
SELECT 
	TO_CHAR(DATE_TRUNC('month', invoice_date), 'YYYY-MM') AS year_month, 
	SUM(unit_price * quantity)
FROM retail
GROUP BY DATE_TRUNC('month', invoice_date)
ORDER BY DATE_TRUNC('month', invoice_date) ASC;