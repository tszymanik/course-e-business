package models

import play.api.libs.json._

case class User(
                 id: Int,
                 email: String,
                 password: String,
                 address: String,
                 city: String,
                 postalCode: String,
                 country: String,
                 phone: String
               )

object User {
  implicit val userFormat = Json.format[User]
}
