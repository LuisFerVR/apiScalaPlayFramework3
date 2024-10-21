package models.daos

import models.Equipo
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class EquipoDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
                          (implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  // Tabla de equipos
  class EquiposTable(tag: Tag) extends Table[Equipo](tag, "equipo") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def nombre = column[String]("nombre")

    def * = (id.?, nombre) <> ((Equipo.apply _).tupled, Equipo.unapply)
  }

  private val equipos = TableQuery[EquiposTable]

  def createEquipo(nombre: String): Future[Int] = {
    val equipo = Equipo(None, nombre)
    db.run((equipos returning equipos.map(_.id)) += equipo)
  }

  def getAllEquipos(): Future[Seq[Equipo]] = {
    db.run(equipos.result)
  }

  def getEquipoById(id: Int): Future[Option[Equipo]] = {
    db.run(equipos.filter(_.id === id).result.headOption)
  }

  def deleteEquipo(id: Int): Future[Int] = {
    db.run(equipos.filter(_.id === id).delete)
  }

  def updateEquipo(id: Int, equipo: Equipo): Future[Int] = {
    db.run(equipos.filter(_.id === id).update(equipo))
  }
}
