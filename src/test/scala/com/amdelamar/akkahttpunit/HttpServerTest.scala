package com.amdelamar.akkahttpunit

import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.amdelamar.akkahttpunit.HttpServer.ListServlet
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers}

import scala.collection.mutable

class HttpServerTest extends FlatSpec
  with Matchers
  with BeforeAndAfterEach
  with ScalatestRouteTest {

  val db = mutable.Map[String, String]()
  val routes = new ListServlet(db).routes

  override def beforeEach(): Unit = {
    super.beforeEach()
    db.clear()
  }

  "GET /list" should "return all items" in {
    db.put("1", "hello")
    db.put("2", "goodbye")

    Get("/list") ~> routes ~> check {
      status.intValue() shouldBe 200
      responseAs[String].contains(db.get("1").get) shouldBe true
      responseAs[String].contains(db.get("2").get) shouldBe true
    }
  }

  "POST /list?key=1&value=hello" should "create new item" in {
    Post("/list?key=1&value=hello") ~> routes ~> check {
      status.intValue() shouldBe 200
      responseAs[String] shouldBe "Created"

      db.get("1") shouldBe Some("hello")
    }
  }

  "PUT /list?key=1&value=goodbye" should "create new item" in {
    db.put("1", "hello")

    Put("/list?key=1&value=goodbye") ~> routes ~> check {
      status.intValue() shouldBe 200
      responseAs[String] shouldBe "Updated"

      db.get("1") shouldBe Some("goodbye")
    }
  }

  "DELETE /list?key=1" should "delete item" in {
    db.put("1", "hello")
    db.put("2", "goodbye")

    Delete("/list?key=1") ~> routes ~> check {
      status.intValue() shouldBe 200
      responseAs[String] shouldBe "Deleted"

      db.get("1") shouldBe None
      db.get("2") shouldBe Some("goodbye")
    }
  }

}
