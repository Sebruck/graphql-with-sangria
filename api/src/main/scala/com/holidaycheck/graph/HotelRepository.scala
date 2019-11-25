package com.holidaycheck.graph

import scala.concurrent.Future

case class Hotel(id: String, name: String, cityId: String)

class HotelRepository {
  private def hotel(id: String) =
    Hotel(id, "Dana Beach Resort", "94773a8c-b71d-3be6-b57e-db9d8740bb98")

  def get(id: String): Future[Option[Hotel]] =
    Future.successful(Some(hotel(id)))

  def getAll(limit: Int): Future[List[Hotel]] =
    Future.successful((1 to limit).map(i => hotel(i.toString)).toList)
}
