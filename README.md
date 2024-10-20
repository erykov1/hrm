# HRM
## launching
  * create folder certs in resources and in this folder:
    - create file private.pem - add private key HS256
    - create file public.pem - add public key HS256
    
### application.properties
  * fill application.properties with:
    - rsa.private-key=classpath:certs/private.pem,
    - rsa.public-key=classpath:certs/public.pem,
    - spring.datasource.driver-class-name=org.postgresql.Driver,
    - spring.datasource.password=<password>,
    - spring.datasource.url=<url>,
    - spring.datasource.username=<username>,
    - spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect,
    - spring.liquibase.change-log=classpath:/db/changelog/changelog.xml,
    - token.decoder=<public_content_key>,
    - spring.profiles.active=<dev> or <test> (to run application use dev to run tests use test),
    - spring.mail.properties.mail.smtp.starttls.required=true,
    - spring.mail.host=smtp.gmail.com,
    - spring.mail.port=587,
    - spring.mail.properties.mail.smtp.auth=true,
    - spring.mail.properties.mail.transport.protocol=smtp,
    - spring.mail.properties.mail.smtp.starttls.enable=true,
    - spring.mail.password=<password>,
    - spring.user.mail=<mail>
   
  * to launch structurizr and generate diagram:
    - create account https://structurizr.com/signup
    - create workspace
    - run class StructurizrMain with env variables:
      a) workspace.id = <workspace_id>
      b) api.key = <api_key>
      c) api.secret = <secret_key>

## current architecture
![structurizr-90179-context diagram](https://github.com/user-attachments/assets/732621d6-301e-4d7f-9ee4-9403a427b7e4)

![structurizr-90179-components (4)](https://github.com/user-attachments/assets/76ecfd97-aca2-4df5-8c84-b660a0939555)

## release 0.0.4
  - admin creates/deletes/modifies category,
  - admin assigned task to category,
  - extended assignments analytic with category name,
  - notification for assigned user (status changed, removed, added) through email

## release 0.0.3
  - admin creates/deletes/modifies task,
  - employee gets task,
  - admin creates/deletes assignment,
  - employee completes assignment,
  - admin gets analytic data for assignments

## release 0.0.2
  - admin creates admin/employee account
  - admin deletes admin/employee account
  - admin/employee modifies his data
  - admin views user data
