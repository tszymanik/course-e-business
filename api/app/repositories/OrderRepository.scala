package repositories

import java.sql.Timestamp

import javax.inject.{Inject, Singleton}
import models.Order
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, userRepository: UserRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class OrderTable(tag: Tag) extends Table[Order](tag, "Orders") {
    def id = column[Int]("OrderId", O.PrimaryKey, O.AutoInc)

    def userId = column[Int]("UserId")

    def orderDate = column[Timestamp]("OrderDate")

    def shippedDate = column[Timestamp]("ShippedDate")

    def * = (id, userId, orderDate, shippedDate) <> ((Order.apply _).tupled, Order.unapply)
  }

  private val orders = TableQuery[OrderTable]

  def getOrders(): Future[Seq[Order]] = db.run {
    orders.result
  }

  def getOrderById(orderId: Int): Future[Option[Order]] = db.run {
    orders.filter(_.id === orderId).result.headOption
  }

  def addOrder(userId: Int, orderDate: Timestamp, shippedDate: Timestamp): Future[Order] = db.run {
    (orders.map(p => (p.userId, p.orderDate, p.shippedDate))
      returning orders.map(_.id)
      into { case ((userId, orderDate, shippedDate), id) => Order(id, userId, orderDate, shippedDate) }
      ) += (userId, orderDate, shippedDate)
  }

  def updateOrder(order: Order) = db.run {
    orders.insertOrUpdate(order)
  }

  def deleteOrder(id: Int): Future[Unit] = db.run {
    orders.filter(_.id === id).delete.map(_ => ())
  }
}
