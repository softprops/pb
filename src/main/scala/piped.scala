package pb

import java.io.InputStream
import java.util.Scanner

object Piped {
  def apply(in: InputStream)(f: String => Int): Int = {
    @annotation.tailrec
    def check: Int = System.in.available match {
      case n if(n > 0) =>
        @annotation.tailrec
        def consume(scan: Scanner, buf: StringBuffer): Int = {
          if (scan.hasNextLine()) {
            buf.append(scan.nextLine)
            buf.append("\n")
            consume(scan, buf)
          } else f(buf.toString)
        }
        consume(new Scanner(in), new StringBuffer())
      case _ =>
        Thread.sleep(100)
        check
    }
    check
  }
}
