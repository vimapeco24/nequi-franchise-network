variable "app_name" {
  default = "-nequi-franchise-network"
}

variable "ecr_image" {
  description = "ECR image URL"
}

variable "db_password" {
  description = "RDS Postgres password"
  sensitive   = true
}
