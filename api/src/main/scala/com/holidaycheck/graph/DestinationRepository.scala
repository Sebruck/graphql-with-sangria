package com.holidaycheck.graph

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

case class Destination(id: String, name: String)

class DestinationRepository {
  def get(id: String): Future[Option[Destination]] =
    Future.successful(Some(Destination(id, "Hurghada")))

  def getByIds(ids: Seq[String]): Future[Seq[Destination]] =
    Future.sequence(ids.map(get)).map(_.flatten)

}
