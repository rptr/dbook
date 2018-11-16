package dbook

import java.io.{File, PrintWriter}

import scala.io.Source

/*
 * This class saves and loads dbook.Diary instances from file as plaintext
 */

class PlaintextIO extends IO {

  def save (diary: Diary): Boolean = {
    val fp = _file + ".dbook"
    val file = new File(fp)
    var success = false

    if (!file.createNewFile()) println("error: can't create file " + fp)

    if (file.canWrite) {
      val fd = new PrintWriter(file)

      diary.getAllItems().foreach(i => fd.write(format(i)))

      fd.close()
    } else {
      println("error: could not open file "+fp+" for writing")
    }

    success
  }

  def format (item: DiaryItem): String = {
    item match {
      case e: Entry => "e:"+e.id+":"+e.timeCreated+":"+e.body+":"+e.tags+"\n"
      case c: Chapter => "c"+c.id+":"+c.timeCreated+"\n"
      case _ => ""
    }
  }

  def load (diary: Diary): Boolean = {
    val fp = _file + ".dbook"
    val file = Source.fromFile(fp)
    var success = false

    if (file.reader().ready()) {
      val fd = file.bufferedReader()
      fd.lines().forEach((s: String) => diary.addItem(fromString(s)))
    } else {
      println("error: could not open file " + fp + " for reading")
    }

    success
  }

  def fromString (s: String): DiaryItem = {
    val vals = s split ":"

    new Entry(vals(1).toInt, "", vals(2).toLong, vals(3))
    // TODO load tags
  }
}
