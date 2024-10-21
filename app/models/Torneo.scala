package models

import play.api.libs.json._

case class Torneo(id: Option[Int], nombre: String)

object Torneo {
  implicit val torneoFormat: OFormat[Torneo] = Json.format[Torneo]
}
