package dbook

import java.io.{File, PrintWriter}

import scala.io.Source

/*
 * This class saves and loads dbook.Diary instances from file as plaintext
 */

class PlaintextIO extends IO {
  private var _loadDiary: Diary = _
  private var _loadEntry: Entry = _

  def save (diary: Diary): Boolean = {
    val fp = _file + ".dbook"
    val file = new File(fp)
    var success = false

    if (!file.createNewFile()) println("error: can't create file " + fp)

    if (file.canWrite) {
      val fd = new PrintWriter(file)

      diary.getAllItems.foreach(i => fd.write(format(i)))

      fd.close()
    } else {
      println("error: could not open file "+fp+" for writing")
    }

    success
  }

  def format (item: DiaryItem): String = {
    def format_body (body: String): String = {
      body.split("\n").map(s => "N"+s+"\n").mkString
    }

    def format_tags (tags: List[String]): String = {
      "T"
    }

    item match {
      case e: Entry => "e:"+e.id+":"+
        e.timeCreated+"\n"+
        format_body(e.body)+"\n"+
        format_tags(e.tags)+"\n"+
        "F\n"
      case c: Chapter => "c"+c.id+":"+c.timeCreated+"\n"
      case _ => ""
    }
  }

  def load (diary: Diary): Boolean = {
    _loadDiary = diary
    val fp = _file + ".dbook"
    val file = Source.fromFile(fp)
    var success = false

    if (file.reader().ready()) {
      val fd = file.bufferedReader()
      fd.lines().forEach(loadLine)
    } else {
      println("error: could not open file " + fp + " for reading")
    }

    success
  }

  def loadLine (s: String): Unit = {
    if (s.isEmpty) {}
    else if (s.charAt(0) == 'e') loadNewEntry(s)
    else if (s.charAt(0) == 'N') loadBodyLine(s)
    else if (s.charAt(0) == 'T') loadTagLine(s)
    else if (s.charAt(0) == 'F') addEntry()
  }

  def loadNewEntry (s: String): Unit = {
    val vals = s split ":"
    _loadEntry = new Entry(vals(1).toInt, "", vals(2).toLong, "")
  }

  def loadBodyLine (str: String): Unit = {
    _loadEntry.body = _loadEntry.body + str.substring(1) + "\n"
  }

  def loadTagLine (str: String): Unit = {
    println("TODO load tag")
  }

  def addEntry (): Unit = {
    _loadDiary.addItem(_loadEntry)
  }
}
