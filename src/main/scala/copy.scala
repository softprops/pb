package pb

import java.awt.datatransfer.StringSelection
import java.awt.Toolkit

import xsbti.{ AppMain, AppConfiguration }
import scala.util.control.Exception.allCatch

object Copy {
  def apply(a: Array[String]) =
    a match {
      case Array("--version") =>
        println("0.1.0")
        0
      case _ =>
        Piped(System.in) { s =>
          allCatch.either {
            Toolkit.getDefaultToolkit()
               .getSystemClipboard()
               .setContents(new StringSelection(s), null)
          } fold({ _ => 1 }, { _ => 0 })
       }
    }
}

object Main {
  def main(args: Array[String]) {
    System.exit(Copy(args))
  }
}

class Script extends AppMain {
  def run(conf: AppConfiguration) =
    new Exit(Copy(conf.arguments))
}

class Exit(val code: Int) extends xsbti.Exit
