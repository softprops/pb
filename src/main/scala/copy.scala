package pb

import java.awt.datatransfer.{StringSelection, Transferable, ClipboardOwner, Clipboard, DataFlavor }
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
            val ss = new StringSelection(s)
            val cb = Toolkit.getDefaultToolkit()
                            .getSystemClipboard()
            println("clipboard: %s" format cb)
            cb.setContents(ss, new ClipboardOwner() {
              def lostOwnership(b: Clipboard, t: Transferable) {
                println("%s lost ownership to %s" format(b,t))
              }
            })
            println("contents: %s" format cb.getContents(null).getTransferData(DataFlavor.stringFlavor))
          } fold({ e => println("opps");e.printStackTrace();1 }, { _ => println("ok");0 })
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
