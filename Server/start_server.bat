cd ProcessImageServer

FOR /F "TOKENS=2* DELIMS=:" %%A IN ('IPCONFIG ^| FIND "IPv4"') DO FOR %%B IN (%%A) DO SET IPADDR=%%B

php public\gcm_.php %IPADDR%

php artisan serve --host %IPADDR% --port 5000
