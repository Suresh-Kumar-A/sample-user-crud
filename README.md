# Project: sample-user-crud

### Description:
Rest Api project to perform various user crud operation along with api for sign in auth using jwt tokens


### RSA Key Generation Steps (using OpenSSL):

#### Generate RSA Private Key (2048 bits)
+) openssl genrsa -out jwt_privatekey.pem 2048

#### Extract Public Key from Private Key in .pem format | (Optional step)
+) openssl req -new -x509 -key jwt_privatekey.pem -out jwt_publickey509.pem -subj '/CN=JWT'

#### Extract Public Key from Private Key in .der format
+) openssl rsa -outform der -in jwt_privatekey.pem -out jwt_publickey.der -pubout

#### Convert RSA Private key format (.pem to .der)
+) openssl pkcs8 -topk8 -inform PEM -outform DER -in jwt_privatekey.pem -out jwt_privatekey.der -nocrypt
