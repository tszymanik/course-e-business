package models

import java.sql.Timestamp

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class OrderTable(tag: Tag) extends Table[Order](tag, "Orders") {
    def id = column[Int]("OrderId", O.PrimaryKey, O.AutoInc)
    def userId = column[Int]("OrderId")
    def orderDate = column[Timestamp]("OrderDate")
    def shippedDate = column[Timestamp]("ShippedDate")

    def * = (id, userId, orderDate, shippedDate) <> ((Order.apply _).tupled, Order.unapply)
  }

  private val order = TableQuery[OrderTable]

  def create(userId: Int, orderDate: Timestamp, shippedDate: Timestamp): Future[Order] = db.run {
    // We create a projection of just the name and age columns, since we're not inserting a value for the id column
    (order.map(p => (p.userId, p.orderDate, p.shippedDate))
      // Now define it to return the id, because we want to know what id was generated for the person
      returning order.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into { case ((userId, orderDate, shippedDate), id) => Order(id, userId, orderDate, shippedDate) }
      // And finally, insert the person into the database
      ) += (userId, orderDate, shippedDate)
  }

  def list(): Future[Seq[Order]] = db.run {
    order.result
  }
}
