package org.zalando.jsonapi.json.akka.http

import akka.http.scaladsl.marshalling._
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.unmarshalling._
import org.zalando.jsonapi.json.sprayjson.SprayJsonJsonapiProtocol
import org.zalando.jsonapi.model._
import org.zalando.jsonapi._
import org.zalando.jsonapi.{JsonapiRootObjectReader, JsonapiRootObjectWriter}
import spray.json._

trait AkkaHttpJsonapiSupport extends SprayJsonJsonapiProtocol with DefaultJsonProtocol {
  def akkaHttpJsonapiMarshaller[T: JsonapiRootObjectWriter]: ToEntityMarshaller[T] =
    Marshaller.StringMarshaller.wrap(`application/vnd.api+json`)(_.rootObject.toJson.compactPrint)

  def akkaHttpJsonapiUnmarshaller[T: JsonapiRootObjectReader]: FromEntityUnmarshaller[T] =
    Unmarshaller.stringUnmarshaller
      .forContentTypes(`application/vnd.api+json`)
      .map(_.parseJson.convertTo[RootObject].jsonapi[T])
}

object AkkaHttpJsonapiSupport extends AkkaHttpJsonapiSupport {
  implicit def akkaHttpJsonapiMarshallerImplicit[T: JsonapiRootObjectWriter]: ToEntityMarshaller[T] =
    akkaHttpJsonapiMarshaller
  implicit def akkaHttpJsonapiUnmarshallerImplicit[T: JsonapiRootObjectReader]: FromEntityUnmarshaller[T] =
    akkaHttpJsonapiUnmarshaller
}
