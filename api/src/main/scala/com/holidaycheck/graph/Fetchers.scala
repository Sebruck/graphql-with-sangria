package com.holidaycheck.graph

import sangria.execution.deferred.{DeferredResolver, Fetcher, FetcherConfig, HasId}

object Fetchers {

  val destinationFetcher =
    Fetcher.caching(
      (ctx: HcContext, ids: Seq[String]) => ctx.destinationRepo.getByIds(ids),
      FetcherConfig.maxBatchSize(30)
    )(HasId(_.id))

  val fetchers = DeferredResolver.fetchers(destinationFetcher)
}
