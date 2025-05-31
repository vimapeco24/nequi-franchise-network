# Terraform IaaC

## Overview

This repository contains Terraform code to provision and manage infrastructure resources on various cloud platforms. The code is organized into modules, each responsible for a specific resource or set of resources.

## Prerequisites

- [Terraform](https://www.terraform.io/downloads.html) installed on your local machine.
- Access to the cloud provider's API (e.g., AWS, Azure, GCP) with appropriate permissions.
- Authentication credentials for the cloud provider (e.g., AWS access key and secret key, Azure service principal, GCP service account key).
- Terraform CLI installed and configured on your local machine.


## Directory Structure

```plaintext
terraform/
├── README.md
├── main.tf
├── variables.tf
├── outputs.tf
...
├── modules/
```

## Running Terraform

1. **Initialize Terraform**: Run the following command to initialize the Terraform working directory. This command downloads the necessary provider plugins and sets up the backend configuration.

   ```bash
   terraform init
   ```
   
2. **Plan the Infrastructure**: Run the following command to create an execution plan. This command shows what actions Terraform will take to create or update the infrastructure.

   ```bash
    terraform plan -var-file=".tfvars"
    ```
   
3. **Apply the Changes**: Run the following command to apply the changes required to reach the desired state of the configuration. This command creates or updates the infrastructure resources.

    ```bash
    terraform apply -var-file=".tfvars"
    ```
   
4. **Destroy the Infrastructure**: Run the following command to destroy the infrastructure resources created by Terraform. This command removes all resources defined in the configuration.

    ```bash
    terraform destroy -var-file=".tfvars"
    ```
   
5. **Validate the Configuration**: Run the following command to validate the Terraform configuration files. This command checks for syntax errors and other issues in the configuration.

    ```bash
    terraform validate
    ```

### Author

- **[Raul Bolivar Navas](https://www.linkedin.com/in/rasysbox)** - [GitHub](https://github.com/raulrobinson/franchise-network-management)

### License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.