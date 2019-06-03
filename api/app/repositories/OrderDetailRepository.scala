package repositories

import javax.inject.{Inject, Singleton}
import models.OrderDetail
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderDetailRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, orderRepository: OrderRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val orderDetails = TableQuery[OrderDetailsTable]

  def getOrdersDetails(): Future[Seq[OrderDetail]] = db.run {
    orderDetails.result
  }

  def getOrderDetailById(orderDetailId: Int): Future[Option[OrderDetail]] = db.run {
    orderDetails.filter(_.id === orderDetailId).result.headOption
  }

  def addOrderDetail(
                      orderId: Int,
                      productId: Int,
                      unitPrice: Double,
                      quantity: Int
                    ): Future[OrderDetail] = db.run {
    (orderDetails.map(orderDetails => (
      orderDetails.orderId,
      orderDetails.productId,
      orderDetails.unitPrice,
      orderDetails.quantity
    ))
      returning orderDetails.map(_.id)
      into { case ((orderId: Int, productId: Int, unitPrice: Double, quantity: Int), id) => OrderDetail(id, orderId, productId, unitPrice, quantity) }
      ) += (orderId, productId, unitPrice, quantity)
  }

  def updateOrderDetail(orderDetail: OrderDetail) = db.run {
    orderDetails.insertOrUpdate(orderDetail)
  }

  def deleteOrderDetail(id: Int): Future[Unit] = db.run {
    orderDetails.filter(_.orderId === id).delete.map(_ => ())
  }

  private class OrderDetailsTable(tag: Tag) extends Table[OrderDetail](tag, "Order Details") {
    def * = (id, orderId, productId, unitPrice, quantity) <> ((OrderDetail.apply _).tupled, OrderDetail.unapply)

    def id = column[Int]("OrderDetailId", O.PrimaryKey, O.AutoInc)

    def orderId = column[Int]("OrderId")

    def productId = column[Int]("ProductId")

    def unitPrice = column[Double]("UnitPrice")

    def quantity = column[Int]("Quantity")
  }

}