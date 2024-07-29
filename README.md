# HRM
## launching
  * create folder certs in resources and in this folder:
    - create file private.pem - add private key HS256
    - create file public.pem - add public key HS256

  * to launch structurizr and generate diagram:
    - create account https://structurizr.com/signup
    - create workspace
    - run class StructurizrMain with env variables:
      a) workspace.id = <workspace_id>
      b) api.key = <api_key>
      c) api.secret = <secret_key>

## current architecture
![structurizr-90179-context diagram](https://github.com/user-attachments/assets/732621d6-301e-4d7f-9ee4-9403a427b7e4)

![structurizr-90179-components](https://github.com/user-attachments/assets/51de8a13-2380-4867-853f-fbc4813cc2d0)

## release 0.0.2
  - admin creates admin/employee account
  - admin deletes admin/employee account
  - admin/employee modifies his data
  - admin views user data
