# Terraform

## Overview

ste repositorio contiene código de Terraform para aprovisionar y gestionar recursos de infraestructura en diversas plataformas en la nube. El código está organizado en módulos, cada uno responsable de un recurso específico o de un conjunto de recursos.
## Prerequisites

- [Terraform](https://www.terraform.io/downloads.html) Instalado en tu máquina local.
- Acceso a la API del proveedor de nube (por ejemplo, AWS, Azure, GCP) con los permisos adecuados.
- Credenciales de autenticación para el proveedor de nube (por ejemplo, clave de acceso y clave secreta de AWS, service principal de Azure, clave de cuenta de servicio de GCP).
- La CLI de Terraform instalada y configurada en tu máquina local.


## Estructura del directorio

```plaintext
terraform/
├── README.md
├── main.tf
├── variables.tf
├── outputs.tf
...
├── modules/
```

## Ejecución de Terraform

1. **Inicializar Terraform**: Ejecuta el siguiente comando para inicializar el directorio de trabajo de Terraform. Este comando descarga los plugins necesarios del proveedor y configura el backend.

   ```bash
   terraform init
   ```
   
2. **Planificar la infraestructura**: Ejecuta el siguiente comando para crear un plan de ejecución. Este comando muestra qué acciones realizará Terraform para crear o actualizar la infraestructura.

   ```bash
    terraform plan -var-file=".tfvars"
    ```
   
3. **Aplicar los cambios**: jecuta el siguiente comando para aplicar los cambios necesarios para alcanzar el estado deseado de la configuración. Este comando crea o actualiza los recursos de infraestructura.

    ```bash
    terraform apply -var-file=".tfvars"
    ```
   
4. **Destruir la infraestructura**: Ejecuta el siguiente comando para destruir los recursos de infraestructura creados por Terraform. Este comando elimina todos los recursos definidos en la configuración.

    ```bash
    terraform destroy -var-file=".tfvars"
    ```
   
5. **Validar la configuración**: Ejecuta el siguiente comando para validar los archivos de configuración de Terraform. Este comando verifica errores de sintaxis y otros problemas en la configuración.

    ```bash
    terraform validate
    ```

### Author

- **[Victor Manuel Perez](https://www.linkedin.com/in/rasysbox)** - [GitHub](https://github.com/vimapeco24/nequi-franchise-network)

### License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.