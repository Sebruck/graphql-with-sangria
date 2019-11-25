package com.holidaycheck.graph

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.sebruck.akka.http.graphql.GraphEndpoint

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

object Boot extends App {
  implicit val system: ActorSystem             = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContextExecutor    = system.dispatcher

  val context = new HcContext {
    override def hotelRepo: HotelRepository             = new HotelRepository
    override def destinationRepo: DestinationRepository = new DestinationRepository
  }

  val graphQL = GraphEndpoint(
    SchemaDefinition.HolidayCheckSchema,
    context,
    deferredResolver = Fetchers.fetchers,
    graphQLPath = "graphql",
    graphQLPlaygroundResourcePath = Some("assets/playground.html")
  )

  println("Binding...")
  Http()
    .bindAndHandle(graphQL.route, "0.0.0.0", 7070)
    .onComplete {
      case Success(bound) => println(s"Bound to" + bound.localAddress)
      case Failure(e)     => println("Failed to bind http service: " + e.getMessage)
    }
}
