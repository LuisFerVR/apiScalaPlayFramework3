package models

import play.api.libs.json._

case class Equipo(id: Option[Int], nombre: String)

object Equipo {
  implicit val equipoFormat: OFormat[Equipo] = Json.format[Equipo]
}