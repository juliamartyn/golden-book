# Book store app

## SetUp

Add all the following environment variables:
 * DB 
   - MYSQL_URL
   - MYSQL_USERNAME
   - MYSQL_PASSWORD
 * JWT_SECRET
 * Mail
   - MAIL_HOST
   - MAIL_USERNAME
   - MAIL_PASSWORD
   - MAIL_PORT
 * INVOICE_REPOSITORY - repository where will be stored the generated invoices
 * REPORT_REPOSITORY - repository where will be stored the generated reports
 * AWS S3 (for E-Book feature)
    - S3_ACCESSKEY
    - S3_SECRETKEY
    - S3_BUCKET_NAME
    - S3_FOLDER_NAME
 * DOWNLOAD_LINK - http://localhost:8081/e-orders/download/ (basic E-Book download link)


## Start application

Run this app add client side app - [GoldenBook React App](https://github.com/juliamartyn/golden-book-client)

Open [http://localhost:8081](http://localhost:8081) to view it in the browser.
