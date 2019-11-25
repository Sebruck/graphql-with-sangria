package com.holidaycheck.graph

import sangria.macros.derive.{ReplaceField, deriveObjectType}
import sangria.schema.{
  Argument,
  Context,
  Field,
  IntType,
  ListType,
  ObjectType,
  OptionType,
  Schema,
  StringType,
  fields
}
import Fetchers._

trait HcContext {
  def hotelRepo: HotelRepository
  def destinationRepo: DestinationRepository
}

object SchemaDefinition {
  val ID    = Argument("id", StringType)
  val Limit = Argument("limit", IntType)

  val DestinationType = deriveObjectType[Unit, Destination]()

  val cityOfHotel = Field(
    "city",
    OptionType(DestinationType),
    resolve = (c: Context[HcContext, Hotel]) => destinationFetcher.defer(c.value.cityId)
  )

  val HotelType = deriveObjectType[HcContext, Hotel](
    ReplaceField("cityId", cityOfHotel)
  )

  val hotel = Field(
    "hotel",
    OptionType(HotelType),
    arguments = List(ID),
    resolve = (c: Context[HcContext, Unit]) => c.ctx.hotelRepo.get(c.arg(ID))
  )

  val hotels = Field(
    "hotels",
    ListType(HotelType),
    arguments = List(Limit),
    resolve = (c: Context[HcContext, Unit]) => c.ctx.hotelRepo.getAll(c.arg(Limit))
  )

  val Query = ObjectType(
    "Query",
    fields(hotel, hotels)
  )

  val HolidayCheckSchema = Schema(Query)
}
