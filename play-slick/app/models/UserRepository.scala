package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{Future, ExecutionContext}


@Singleton
class UserRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class UserTable(tag: Tag) extends Table[User](tag, "users") {
    def id = column[Int]("UserId", O.PrimaryKey, O.AutoInc)

    def email = column[String]("Email")
    def password = column[String]("Password")
    def address = column[String]("Address")
    def city = column[String]("City")
    def postalCode = column[String]("PostalCode")
    def country = column[String]("Country")
    def phone = column[String]("Phone")

    def * = (id, email, password, address, city, postalCode, country, phone) <> ((User.apply _).tupled, User.unapply)
  }

  private val user = TableQuery[UserTable]

  def create(email: String, password: String, address: String, city: String, postalCode: String, country: String, phone: String): Future[User] = db.run {
    // We create a projection of just the name and age columns, since we're not inserting a value for the id column
    (user.map(p => (p.email, p.password, p.address, p.city, p.postalCode, p.country, p.phone))
      // Now define it to return the id, because we want to know what id was generated for the person
      returning user.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into { case ((email, password, address, city, postalCode, country, phone), id) => User(id, email, password, address, city, postalCode, country, phone) }
      // And finally, insert the person into the database
      ) += (email, password, address, city, postalCode, country, phone)
  }

  def list(): Future[Seq[User]] = db.run {
    user.result
  }
}