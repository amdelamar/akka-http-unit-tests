package com.amdelamar.akkahttpunit

import java.util.concurrent.Executors

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.collection.mutable
import scala.concurrent.ExecutionContext

object HttpServer {

  implicit val system = ActorSystem("server")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(16))

  def main(args: Array[String]): Unit = {

    val db = mutable.Map[String, String]()
    val list = new ListServlet(db)

    Http().bindAndHandle(list.routes, "localhost", 8080)
      .map { _ =>
        println(s"Server is running at http://localhost:8080/")
      }
  }

  class ListServlet(db: mutable.Map[String, String]) {
    val routes: Route =
      path("list") {
        get {
          complete(db.toString)
        } ~
        post {
          parameters("key", "value") { (key, value) =>
            db.put(key, value)
            complete("Created")
          }
        } ~
        put {
          parameters("key", "value") { (key, value) =>
            db.put(key, value)
            complete("Updated")
          }
        } ~
        delete {
          parameter("key") { key =>
            db.remove(key)
            complete("Deleted")
          }
        }
      }
  }

}
