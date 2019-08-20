package com.amdelamar.akkahttpunit

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpEntity, HttpMethods, HttpRequest, HttpResponse, StatusCodes}
import akka.testkit.TestKit
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FlatSpecLike, Matchers}

import scala.concurrent.Future

class HttpClientTest extends TestKit(ActorSystem("test"))
  with FlatSpecLike
  with Matchers
  with MockFactory
  with ScalaFutures {

  "HttpClient.getList()" should "return all items" in {
    implicit val mockSendRequest = mockFunction[HttpRequest, Future[HttpResponse]]
    mockSendRequest.expects(*).onCall({req: HttpRequest =>
      if (req.method == HttpMethods.GET && req.uri.toString.endsWith("/list")) {
        Future.successful(HttpResponse(StatusCodes.OK, entity = HttpEntity("ok")))
      } else {
        Future.successful(HttpResponse(StatusCodes.BadRequest, entity = HttpEntity("bad request")))
      }
    })

    val httpClient = new HttpClient("http://example.com/list", Http())
    val res = httpClient.getList().futureValue
    res.right.getOrElse("").contains("ok") shouldBe true
  }

  "HttpClient.postListItem()" should "return all items" in {
    implicit val mockSendRequest = mockFunction[HttpRequest, Future[HttpResponse]]
    mockSendRequest.expects(*).onCall({req: HttpRequest =>
      if (req.method == HttpMethods.POST && req.uri.toString.endsWith("/list?key=1&value=hello")) {
        Future.successful(HttpResponse(StatusCodes.OK, entity = HttpEntity("Created")))
      } else {
        Future.successful(HttpResponse(StatusCodes.BadRequest, entity = HttpEntity("bad request")))
      }
    })

    val httpClient = new HttpClient("http://example.com/list", Http())
    val res = httpClient.postListItem("1", "hello").futureValue
    res.right.getOrElse("").contains("Created") shouldBe true
  }

  "HttpClient.putListItem()" should "return all items" in {
    implicit val mockSendRequest = mockFunction[HttpRequest, Future[HttpResponse]]
    mockSendRequest.expects(*).onCall({req: HttpRequest =>
      if (req.method == HttpMethods.PUT && req.uri.toString.endsWith("/list?key=1&value=goodbye")) {
        Future.successful(HttpResponse(StatusCodes.OK, entity = HttpEntity("Updated")))
      } else {
        Future.successful(HttpResponse(StatusCodes.BadRequest, entity = HttpEntity("bad request")))
      }
    })

    val httpClient = new HttpClient("http://example.com/list", Http())
    val res = httpClient.putListItem("1", "goodbye").futureValue
    res.right.getOrElse("").contains("Updated") shouldBe true
  }

  "HttpClient.deleteListItem()" should "return all items" in {
    implicit val mockSendRequest = mockFunction[HttpRequest, Future[HttpResponse]]
    mockSendRequest.expects(*).onCall({req: HttpRequest =>
      if (req.method == HttpMethods.DELETE && req.uri.toString.endsWith("/list?key=1")) {
        Future.successful(HttpResponse(StatusCodes.OK, entity = HttpEntity("Deleted")))
      } else {
        Future.successful(HttpResponse(StatusCodes.BadRequest, entity = HttpEntity("bad request")))
      }
    })

    val httpClient = new HttpClient("http://example.com/list", Http())
    val res = httpClient.deleteListItem("1").futureValue
    res.right.getOrElse("").contains("Deleted") shouldBe true
  }

}
