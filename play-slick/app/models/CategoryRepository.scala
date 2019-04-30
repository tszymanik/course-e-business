package models
import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class CategoryRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class CategoryTable(tag: Tag) extends Table[Category](tag, "Categories") {
    def id = column[Int]("CategoryId", O.PrimaryKey, O.AutoInc)
    def name = column[String]("CategoryName")
    def description = column[String]("CategoryDescription")

    def * = (id, name) <> ((Category.apply _).tupled, Category.unapply)
  }

  val category = TableQuery[CategoryTable]


  def create(name: String): Future[Category] = db.run {
    // We create a projection of just the name and age columns, since we're not inserting a value for the id column
    (category.map(c => (c.name))
      // Now define it to return the id, because we want to know what id was generated for the person
      returning category.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into ((name, id) => Category(id, name))
      // And finally, insert the person into the database
      ) += (name)
  }

  def list(): Future[Seq[Category]] = db.run {
    category.result
  }
}