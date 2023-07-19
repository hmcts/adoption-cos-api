variable "product" {}

variable "component" {}

variable "raw_product" {
  default = "adoption"
}

variable "location" {
  default = "UK South"
}

variable "env" {}

variable "subscription" {}

variable "common_tags" {
  type = map(string)
}
