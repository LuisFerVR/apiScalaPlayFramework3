package models

import play.api.libs.json._

case class Jugador(id: Option[Int], nombre: String, posicion: String, dorsal: Int, equipo_id: Int)

object Jugador {
  implicit val jugadorFormat: OFormat[Jugador] = Json.format[Jugador]
}
