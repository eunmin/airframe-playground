package com.eunmin

import wvlet.airframe._
import javax.annotation.{PostConstruct, PreDestroy}

trait Connection {
  val name: String
}
class ConnectionImpl extends Connection {
  override val name: String = "test"

  @PostConstruct
  def start: Unit = {
    println("start connection")
  }

  @PreDestroy
  def stop: Unit = {
    println("start connection")
  }
}

trait Database {
  val conn = bind[Connection]
  def start
  def stop
}
class DatabaseImpl extends Database {
  override def start: Unit = {
    println(s"start database : ${conn.name}")
  }

  override def stop: Unit = {
    println("stop database")
  }
}

trait DatabaseService {
  val db = bind[Database].onInit(_.start).onShutdown(_.stop)
}

object Main {
  def main(args: Array[String]): Unit = {
    val design = newDesign
      .bind[Connection].to[ConnectionImpl]
      .bind[Database].to[DatabaseImpl]

    design.build[DatabaseService] { service: DatabaseService =>
      println(s"service: ${service.db.conn.name}")
    }
  }
}
