package com.amdelamar.akkahttpunit

import java.util.concurrent.Executors

import akka.actor.ActorSystem
import akka.http.scaladsl.HttpExt
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse, StatusCodes}
import akka.stream.ActorMaterializer

import scala.concurrent.{ExecutionContext, Future}

class HttpClient(url: String, http: HttpExt)
                (implicit sendHttpRequest: HttpRequest => Future[HttpResponse] = http.singleRequest(_)) {

  implicit val system = ActorSystem("client")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(16))

  def getList() = {
    sendHttpRequest(HttpRequest(HttpMethods.GET, url + "/list"))
      .map { res =>
        if (res.status == StatusCodes.OK) {
          Right(res.entity.toString)
        } else {
          Left(s"Bad request response: ${res.status}")
        }
      }.recover {
      case ex: Throwable =>
        Left(s"Error when fetching list: ${ex.getMessage}")
    }
  }

  def postListItem(key: String, value: String) = {
    sendHttpRequest(HttpRequest(HttpMethods.POST, url + "/list" + s"?key=$key" + s"&value=$value"))
      .map { res =>
        if (res.status == StatusCodes.OK) {
          Right(res.entity.toString)
        } else {
          Left(s"Bad request response: ${res.status}")
        }
      }.recover {
      case ex: Throwable =>
        Left(s"Error when fetching list: ${ex.getMessage}")
    }
  }

  def putListItem(key: String, value: String) = {
    sendHttpRequest(HttpRequest(HttpMethods.PUT, url + "/list" + s"?key=$key" + s"&value=$value"))
      .map { res =>
        if (res.status == StatusCodes.OK) {
          Right(res.entity.toString)
        } else {
          Left(s"Bad request response: ${res.status}")
        }
      }.recover {
      case ex: Throwable =>
        Left(s"Error when fetching list: ${ex.getMessage}")
    }
  }

  def deleteListItem(key: String) = {
    sendHttpRequest(HttpRequest(HttpMethods.DELETE, url + "/list" + s"?key=$key"))
      .map { res =>
        if (res.status == StatusCodes.OK) {
          Right(res.entity.toString)
        } else {
          Left(s"Bad request response: ${res.status}")
        }
      }.recover {
      case ex: Throwable =>
        Left(s"Error when fetching list: ${ex.getMessage}")
    }
  }

}
