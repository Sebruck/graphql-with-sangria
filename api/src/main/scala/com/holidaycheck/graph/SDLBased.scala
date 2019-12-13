package com.holidaycheck.graph

import io.circe.Json
import sangria.execution.Executor
import sangria.marshalling.circe._
import sangria.schema._
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.optics.JsonPath.root

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

object SDLBased extends App {
  val context = new HcContext {
    override def hotelRepo: HotelRepository             = new HotelRepository
    override def destinationRepo: DestinationRepository = new DestinationRepository
  }

  import sangria.macros._
  val schemaAst =
    gql"""
        type Destination {
          id: String!
          name: String!
        }

        type Hotel {
          id: String!
          name: String! 
          city: Destination
        }

        type Query {
          hotel(id: String!): Hotel
          hotels(limit: Int!): [Hotel!]!
        }
"""

  val builder = AstSchemaBuilder.resolverBased[HcContext](
    FieldResolver.map(
      "Query" -> Map(
        "hotel" -> { c =>
          val hotelId = c.arg[String]("id")
          c.ctx.hotelRepo.get(hotelId).map(_.asJson)
        },
        "hotels" -> { c =>
          val limit = c.arg[Int]("limit")
          c.ctx.hotelRepo
            .getAll(limit)
            .map(_.map(_.asJson))
        }
      ),
      "Hotel" -> Map(
        "city" -> { c =>
          val hotelJson     = c.value.asInstanceOf[Json]
          val destinationId = root.cityId.string.getOption(hotelJson).get
          c.ctx.destinationRepo.get(destinationId).map(_.asJson)
        }
      ),
    ),
    FieldResolver.defaultInput
  )

  val schema = Schema.buildFromAst(schemaAst, builder.validateSchemaWithException(schemaAst))

  val query =
    gql"""
    {
      hotel(id: "123") {
        ...FullHotel
      }
      hotels(limit: 3) {
        ...FullHotel
      }
    }
      
    fragment FullHotel on Hotel {
        id
        name
        city {
          id
          name
        }
    }
  """

  val execution = Executor.execute(
    schema,
    query,
    userContext = context
  )

  val result = Await.result(execution, Duration.Inf)
  println(result)
}
