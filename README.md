# sample-user-crud
Rest Api project to perform various user crud operation along with api for sign in auth using jwt tokens


# OpenSSL RSA Key pair generation

+) openssl genrsa -out jwt_privatekey.pem 2048

+) openssl req -new -x509 -key jwt_privatekey.pem -out jwt_publickey509.pem -subj '/CN=JWT'

## Converting RSA Public key format (.pem to .der)
+) openssl x509 -outform der -in jwt_publickey509.pem -out jwt_publickey509.der

## Converting RSA Private key format (.pem to .der)
+) openssl rsa -outform der -in jwt_privatekey.pem -out jwt_privatekey.der