package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{Future, ExecutionContext}

@Singleton
class UserRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class UserTable(tag: Tag) extends Table[User](tag, "Users") {
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

  private val users = TableQuery[UserTable]

  def getUsers(): Future[Seq[User]] = db.run {
    users.result
  }

  def getUserById(userId: Int): Future[Option[User]] = db.run {
    users.filter(_.id === userId).result.headOption
  }

  def addUser(
               email: String,
               password: String,
               address: String,
               city: String,
               postalCode: String,
               country: String,
               phone: String
             ): Future[User] = db.run {
    (users.map(p => (p.email, p.password, p.address, p.city, p.postalCode, p.country, p.phone))
      returning users.map(_.id)
      into { case ((
      email: String,
      password: String,
      address: String,
      city: String,
      postalCode: String,
      country: String,
      phone: String
      ), id) => User(id, email, password, address, city, postalCode, country, phone)
    }
      ) += (email, password, address, city, postalCode, country, phone)
  }

  def updateUser(user: User) = db.run {
    users.insertOrUpdate(user)
  }

  def deleteUser(id: Int): Future[Unit] = db.run {
    users.filter(_.id === id).delete.map(_ => ())
  }
}